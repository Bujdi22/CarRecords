package com.bujdi.carRecords.controller;

import com.bujdi.carRecords.dto.UserDto;
import com.bujdi.carRecords.model.User;
import com.bujdi.carRecords.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/api/register")
    public ResponseEntity<Object> register(@Valid @RequestBody UserDto dto)
    {
        service.register(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/api/login")
    public ResponseEntity<String> login(@RequestBody User user)
    {
        String token = service.verify(user);
        return token == null
                ? new ResponseEntity<>("You are not authorised", HttpStatus.FORBIDDEN)
                : new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/api/fetch-account")
    public ResponseEntity<User> fetchAccount()
    {
        return new ResponseEntity<>(service.getAuthUser(), HttpStatus.OK);
    }

    @DeleteMapping("/api/delete-account")
    public ResponseEntity<Object> deleteAccount() {
        User user = service.getAuthUser();
        service.deleteUser(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
