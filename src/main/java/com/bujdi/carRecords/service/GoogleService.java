package com.bujdi.carRecords.service;

import com.bujdi.carRecords.exception.AccountNotVerified;
import com.bujdi.carRecords.mapping.GoogleProfile;
import com.bujdi.carRecords.model.User;
import com.bujdi.carRecords.repository.UserRepository;
import com.bujdi.carRecords.utils.UrlGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class GoogleService {

    @Value("${google.oauth.client}")
    private String googleClientId;

    @Value("${google.oauth.secret}")
    private String googleSecret;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public String getAccessTokenFromCode(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = getTokenExchangeParams(code);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        String url = "https://oauth2.googleapis.com/token";
        String response = restTemplate.postForObject(url, requestEntity, String.class);

        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, Object> responseMap = mapper.readValue(response, Map.class);
            return responseMap.get("access_token").toString();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GoogleProfile getGoogleProfile(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);

        String url = "https://www.googleapis.com/oauth2/v2/userinfo";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            System.out.println("Something went wrong with google auth");
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> map = mapper.readValue(response.getBody(), Map.class);
            GoogleProfile profile = new GoogleProfile();
            profile.setId(map.get("id").toString());
            profile.setEmail(map.get("email").toString());
            profile.setVerifiedEmail((Boolean) map.get("verified_email"));
            profile.setName(map.get("name").toString());
            profile.setGivenName(map.get("given_name").toString());
            profile.setFamilyName(map.get("family_name").toString());
            profile.setPicture(map.get("picture").toString());

            return profile;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User getUserFromGoogleProfile(GoogleProfile googleProfile) throws AccountNotVerified {
        User userByGoogleId = userRepository.findByGoogleId(googleProfile.getId());

        if (userByGoogleId != null) {
            return userByGoogleId;
        }

        User userByUsername = userRepository.findByUsername(googleProfile.getEmail());

        if (userByUsername != null) {
            userByUsername.setGoogleId(googleProfile.getId());
            userRepository.save(userByUsername);
            return userByUsername;
        }

        return registerNewUser(googleProfile);
    }

    private @NotNull MultiValueMap<String, String> getTokenExchangeParams(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("code", code);
        params.add("redirect_uri", UrlGenerator.generateServerUrl("/google-redirect-uri"));
        params.add("client_id", googleClientId);
        params.add("client_secret", googleSecret);
        params.add("scope", "https://www.googleapis.com/auth/userinfo.profile");
        params.add("scope", "https://www.googleapis.com/auth/userinfo.email");
        params.add("scope", "openid");
        params.add("grant_type", "authorization_code");

        return params;
    }

    private User registerNewUser(GoogleProfile googleProfile) throws AccountNotVerified
    {
        if (!googleProfile.isVerifiedEmail())  {
            throw new AccountNotVerified("The account e-mail is not verified");
        }
        User user = new User();
        user.setUsername(googleProfile.getEmail());
        user.setDisplayName(googleProfile.getName());
        user.setCreatedAt(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setGoogleId(googleProfile.getId());
        user.setEmailVerified(true);
        userRepository.save(user);
        userService.sendWelcomeEmail(user);

        return user;
    }
}
