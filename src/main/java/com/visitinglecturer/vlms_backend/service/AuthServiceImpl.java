package com.visitinglecturer.vlms_backend.service;

import com.visitinglecturer.vlms_backend.entity.Role;
import com.visitinglecturer.vlms_backend.entity.User;
import com.visitinglecturer.vlms_backend.payload.request.LoginRequest;
import com.visitinglecturer.vlms_backend.payload.response.LoginResponse;
import com.visitinglecturer.vlms_backend.repository.RoleRepository;
import com.visitinglecturer.vlms_backend.repository.UserRepository;
import com.visitinglecturer.vlms_backend.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HashService hashService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public LoginResponse login(LoginRequest loginRequest) throws NoSuchAlgorithmException {

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElse(null);

        if (user == null) {
            return null;
        }

        String hashedPassword = hashService.hashString(loginRequest.getPassword());
        if (!user.getPassword().equals(hashedPassword)) {
            return null;
        }

        String token = jwtTokenUtil.generateToken(user.getUsername());

        //LoginResponse loginResponse = new LoginResponse(token, user);

        user.setPassword("");

        return new LoginResponse(token, user);
    }


    @Override
    public String createHardCordedUsers() {
        Role adminRole = roleRepository.findById(1L).orElse(null);
        Role  vlRole = roleRepository.findById(2L).orElse(null);

        User user1 = userRepository.findByUsername("admin").orElse(null);

        if(user1 == null){
            try {
                User user = new User();
                user.setUsername("admin");
                String hashedPassword = hashService.hashString("admin123");
                user.setPassword(hashedPassword);
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                user.setRoles(roles);
                userRepository.save(user);
            } catch (Exception e) {
                System.out.println("Error while creating hard corded users : " + e.getMessage());
                return ("Error while creating hard corded users : " + e.getMessage());
            }

        }

        User user2 = userRepository.findByUsername("testuser").orElse(null);

        if(user2 == null){
            try {
                User user = new User();
                user.setUsername("testuser");
                String hashedPassword = hashService.hashString("user123");
                user.setPassword(hashedPassword);
                Set<Role> roles = new HashSet<>();
                roles.add(vlRole);
                user.setRoles(roles);
                userRepository.save(user);
            } catch (Exception e) {
                System.out.println("Error while creating hard corded users : " + e.getMessage());
                return ("Error while creating hard corded users : " + e.getMessage());
            }

        }

        return "Users created successfully";
    }


}

