package com.visitinglecturer.vlms_backend.security;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CorsInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Allow all origins
        response.setHeader("Access-Control-Allow-Origin", "*");

        // Allow specific HTTP methods (or all methods)
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");

        // Allow specific headers (or all headers)
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");

        // Allow credentials if needed (can be "true" or "false")
        response.setHeader("Access-Control-Allow-Credentials", "true");

        // Handle preflight OPTIONS request
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);  // Respond with 200 OK for OPTIONS
            return false;  // Don't proceed with the request, just return a response
        }

        return true; // Continue with the request for non-OPTIONS requests
    }
}
