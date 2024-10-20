package com.bujdi.carRecords.service;

import com.bujdi.carRecords.dto.UserDto;
import com.bujdi.carRecords.model.User;
import com.bujdi.carRecords.model.UserPrincipal;
import com.bujdi.carRecords.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
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

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public void register(UserDto dto) {
        User user = createUserFromDto(dto);
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
    }


    public String verify(User user) {
        try {
            Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            return jwtService.generateToken(user.getUsername());
        } catch (Exception e) {
            System.out.println("Exception caught in verify");
            System.out.println(e);
            throw e;
//            return null;
        }
    }

    public User getAuthUser() {
        return ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
    }

    private User createUserFromDto(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());

        return user;
    }

    public void deleteUser(User user) {
        user.setDeletedAt(LocalDateTime.now());
        repo.save(user);
    }
}
