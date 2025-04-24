package com.visitinglecturer.vlms_backend.service;

import com.visitinglecturer.vlms_backend.entity.User;
import com.visitinglecturer.vlms_backend.payload.request.LoginRequest;
import com.visitinglecturer.vlms_backend.payload.response.LoginResponse;

import java.security.NoSuchAlgorithmException;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest) throws NoSuchAlgorithmException;

    String createHardCordedUsers();


}
