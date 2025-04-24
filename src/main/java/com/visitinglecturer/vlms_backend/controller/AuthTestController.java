package com.visitinglecturer.vlms_backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class AuthTestController {

    // Method accessible by ROLE_ADMIN
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminAccess() {
        return "Access granted to ROLE_ADMIN.";
    }

    // Method accessible by ROLE_VISITING_LECTURER
    @GetMapping("/visiting-lecturer")
    @PreAuthorize("hasRole('VISITING_LECTURER')")
    public String visitingLecturerAccess() {
        return "Access granted to ROLE_VISITING_LECTURER.";
    }

    // Method accessible by both ROLE_ADMIN and ROLE_VISITING_LECTURER
    @GetMapping("/both")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VISITING_LECTURER')")
    public String bothRolesAccess() {
        return "Access granted to both ROLE_ADMIN and ROLE_VISITING_LECTURER.";
    }

}
