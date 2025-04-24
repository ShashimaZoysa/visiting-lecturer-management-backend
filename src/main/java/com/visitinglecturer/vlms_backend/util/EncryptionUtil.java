package com.visitinglecturer.vlms_backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class EncryptionUtil {

    @Value("${app.encryption.secret}")
    private String secretKeyEncoded; // From application.properties

    private SecretKeySpec secretKey;
    private static final String ALGORITHM = "AES";

    @PostConstruct
    public void init() {
        byte[] decodedKey = Base64.getDecoder().decode(secretKeyEncoded);
        this.secretKey = new SecretKeySpec(decodedKey, ALGORITHM);
    }

    // Encrypt method with exception handling
    public String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            // Handle the exception, maybe log it
            e.printStackTrace();
            throw new RuntimeException("Encryption failed", e);  // Optionally, rethrow as a runtime exception
        }
    }

    // Decrypt method with exception handling
    public String decrypt(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decoded = Base64.getDecoder().decode(encryptedText);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted);
        } catch (Exception e) {
            // Handle the exception, maybe log it
            e.printStackTrace();
            throw new RuntimeException("Decryption failed", e);  // Optionally, rethrow as a runtime exception
        }
    }
}


