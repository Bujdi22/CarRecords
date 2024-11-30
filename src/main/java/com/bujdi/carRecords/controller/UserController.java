package com.bujdi.carRecords.controller;

import com.bujdi.carRecords.dto.ForgotPasswordDto;
import com.bujdi.carRecords.dto.ResetPasswordDto;
import com.bujdi.carRecords.dto.UserDto;
import com.bujdi.carRecords.mapping.UserAccountMapping;
import com.bujdi.carRecords.model.User;
import com.bujdi.carRecords.service.RecaptchaService;
import com.bujdi.carRecords.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private RecaptchaService recaptchaService;

    @PostMapping("/api/register")
    public ResponseEntity<Object> register(@Valid @RequestBody UserDto dto)
    {
        if (!recaptchaService.verifyRecaptcha(dto.getRecaptchaToken())) {
            return new ResponseEntity<>("Bad recaptcha", HttpStatus.BAD_REQUEST);
        }

        service.register(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/api/login")
    public ResponseEntity<String> login(@RequestBody User user)
    {
        String token = service.verify(user);
        return token == null
                ? new ResponseEntity<>("Bad credentials", HttpStatus.FORBIDDEN)
                : new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/api/fetch-account")
    public ResponseEntity<Map<String, Object>> fetchAccount()
    {
        User user = service.getAuthUser();
        Map<String, Object> response = UserAccountMapping.mapUserToResponse(user);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/api/delete-account")
    public ResponseEntity<Object> deleteAccount() {
        User user = service.getAuthUser();
        service.deleteUser(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/refresh-token")
    public ResponseEntity<String> refreshToken()
    {
        User user = service.getAuthUser();
        String token = service.generateTokenForUser(user, true);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/api/forgot-password-request")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordDto dto)
    {
        this.service.sendForgotPassword(dto.getUsername());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/forgot-password")
    public ResponseEntity<Map<String, Map<String, String>>> resetPassword(@Valid @RequestBody ResetPasswordDto dto)
    {
        boolean success = this.service.resetPassword(dto.getPassword(), dto.getToken());

        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Map.of(
                    "errors", Map.of("Rejected", "Your reset link might have expired.")
            ), HttpStatus.BAD_REQUEST);
        }
    }


}
