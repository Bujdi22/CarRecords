package com.bujdi.carRecords.service;

import com.bujdi.carRecords.dto.UpdateAccountDTO;
import com.bujdi.carRecords.dto.UserDto;
import com.bujdi.carRecords.exception.AccountNotVerified;
import com.bujdi.carRecords.utils.UrlGenerator;
import com.bujdi.carRecords.model.SecureToken;
import com.bujdi.carRecords.model.User;
import com.bujdi.carRecords.model.UserPrincipal;
import com.bujdi.carRecords.repository.SecureTokenRepository;
import com.bujdi.carRecords.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserRepository repo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SecureTokenRepository tokenRepo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public void register(UserDto dto) {
        User user = createUserFromDto(dto);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        repo.save(user);
        sendWelcomeEmail(user);
    }

    public void sendWelcomeEmail(User user) {
        String content = "<p>We hope you enjoy our application and welcome to the team!</p>";
        String subject = "Welcome to AutoJournal";


        if (user.getGoogleId() == null) {
            SecureToken token = new SecureToken();
            token.setUser(user);
            token.setPurpose("verify-email");
            token.setExpiryDate(LocalDateTime.now().plusDays(14));
            tokenRepo.save(token);
            String verifyLink = UrlGenerator.generateUrl("/verifyEmail?token=" + token.getToken());
            content += "<p>Please verify your e-mail by clicking the link below</p>"
                        +"<p><a href='" + verifyLink + "'> Verify your e-mail</a></p>";
            subject = "Please verify your e-mail";
        }

        emailService.sendEmail(user, subject, content);
    }


    public String verify(User user) throws AccountNotVerified, BadCredentialsException{
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        User loggedInUser = repo.findByUsername(user.getUsername());
        if (!loggedInUser.isEmailVerified()) {
            throw new AccountNotVerified("Your account is not verified");
        }
        loggedInUser.setLastLogin(LocalDateTime.now());
        repo.save(loggedInUser);
        return this.generateTokenForUser(user, true);
    }

    public String generateTokenForUser(User user, Boolean isLong) {
        return jwtService.generateToken(user.getUsername(), isLong);
    }

    public User getAuthUser() {
        return ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
    }

    public void sendForgotPassword(String username) {
        User user = repo.findByUsername(username);

        if (user != null) {
            SecureToken token = new SecureToken();
            token.setUser(user);
            token.setPurpose("forgot-password");
            token.setExpiryDate(LocalDateTime.now().plusMinutes(30));
            tokenRepo.save(token);

            String resetUrl = UrlGenerator.generateUrl("/resetPassword?token=" + token.getToken());

            emailService.sendEmail(user,
                    "Forgot password",
                    "<p>You requested a password reset.</p>" +
                            "<p>Please click the link below to reset your password.</p>" +
                            "<p>This link will only work for 30 minutes</p>" +
                            "<p>If this was not done by you, you can ignore this email.</p>" +
                            "<p><a href='"+ resetUrl +"'>Click here to reset your password</a></p>"
            );
        }

    }

    private User createUserFromDto(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setDisplayName(dto.getDisplayName());

        return user;
    }

    public void deleteUser(User user) {
        user.setDeletedAt(LocalDateTime.now());
        repo.save(user);
    }

    public boolean resetPassword(String password, String token) {
        SecureToken secureToken = tokenRepo.findToken(token);

        if (secureToken == null) {
            return false;
        }
        if (!secureToken.getPurpose().equals("forgot-password")) {
            return false;
        }
        if (secureToken.isExpired()) {
            return false;
        }

        User user = secureToken.getUser();
        user.setPassword(encoder.encode(password));
        repo.save(user);
        tokenRepo.delete(secureToken);

        return true;
    }

    public boolean verifyEmail(String token) {
        SecureToken secureToken = tokenRepo.findToken(token);
        if (secureToken == null) {
            return false;
        }
        if (!secureToken.getPurpose().equals("verify-email")) {
            return false;
        }
        if (secureToken.isExpired()) {
            return false;
        }

        User user = secureToken.getUser();
        user.setEmailVerified(true);
        repo.save(user);

        tokenRepo.delete(secureToken);

        return true;
    }

    public User updateUserAccount(UpdateAccountDTO dto) throws BadCredentialsException
    {
        User user = getAuthUser();

        user.setDisplayName(dto.getDisplayName());

        if (dto.getPassword() != null) {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), dto.getCurrentPassword()));
            user.setPassword(encoder.encode(dto.getPassword()));
        }

        return repo.save(user);
    }
}
