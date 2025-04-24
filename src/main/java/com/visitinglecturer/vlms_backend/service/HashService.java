package com.visitinglecturer.vlms_backend.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;

@Service
public class HashService {

    // Method to generate SHA-256 hash
    public String hashString(String input) throws NoSuchAlgorithmException {
        // Create a MessageDigest instance for SHA-256
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // Perform the hash calculation
        byte[] encodedHash = digest.digest(input.getBytes());

        // Convert the byte array to a hexadecimal string
        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedHash) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString();  // Return the hex string as the hashed output
    }

    // Method to compare if a hash matches the input string
    public boolean matchHash(String input, String existingHash) throws NoSuchAlgorithmException {
        // Generate hash for the input string
        String hashedInput = hashString(input);

        // Compare the generated hash with the existing hash
        return hashedInput.equals(existingHash);
    }
}