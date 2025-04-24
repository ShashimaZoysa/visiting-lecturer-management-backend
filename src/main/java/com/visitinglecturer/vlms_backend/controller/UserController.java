package com.visitinglecturer.vlms_backend.controller;

import com.visitinglecturer.vlms_backend.entity.User;
import com.visitinglecturer.vlms_backend.payload.request.UserRequest;
import com.visitinglecturer.vlms_backend.payload.response.UserResponse;
import com.visitinglecturer.vlms_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Handle the submission of user details.
     * Supports both Admin and Visiting Lecturer roles.
     *
     * @param user The user object submitted via request body.
     * @return ResponseEntity containing the saved User object.
     */
    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody UserRequest userRequest) {
        try {
            // Hash the password in the service layer before saving
            User savedUser = userService.saveUser(userRequest);

            if (savedUser != null) {
                return ResponseEntity.ok(savedUser);
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        } catch (Exception e) {
            // Log the error (optional)
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    /**
     * Retrieve user details by username.
     * @param username The username of the user.
     * @return ResponseEntity containing UserResponse.
     */
    //@GetMapping("/view/{username}")
    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        try {
            UserResponse userResponse = userService.viewUser(username);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}

