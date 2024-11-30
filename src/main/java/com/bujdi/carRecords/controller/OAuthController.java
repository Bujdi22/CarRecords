package com.bujdi.carRecords.controller;

import com.bujdi.carRecords.mapping.GoogleProfile;
import com.bujdi.carRecords.model.User;
import com.bujdi.carRecords.service.GoogleService;
import com.bujdi.carRecords.service.UserService;
import com.bujdi.carRecords.utils.UrlGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class OAuthController {

    @Autowired
    private GoogleService googleService;

    @Autowired
    private UserService userService;

    @GetMapping("/google-redirect-uri")
    public ResponseEntity<Object> googleRedirect(@RequestParam("code") String code, @RequestParam("scope") String scope, @RequestParam("authuser") String authUser, @RequestParam("prompt") String prompt) {
        String token = googleService.getAccessTokenFromCode(code);

        if (token == null) {
            return new ResponseEntity<>("Sorry - something went wrong with the Google authentication flow.", HttpStatus.NOT_FOUND);
        }

        GoogleProfile googleProfile = googleService.getGoogleProfile(token);

        if (googleProfile == null) {
            return new ResponseEntity<>("Sorry - something went wrong with the Google authentication flow.", HttpStatus.NOT_FOUND);
        }

        User user = googleService.getUserFromGoogleProfile(googleProfile);

        String authToken = userService.generateTokenForUser(user, false);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(UrlGenerator.generateUrl("/google-auth-success?token=" + authToken)));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

}
