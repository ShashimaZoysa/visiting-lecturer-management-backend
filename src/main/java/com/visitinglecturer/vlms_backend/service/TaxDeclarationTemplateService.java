package com.visitinglecturer.vlms_backend.service;

import com.visitinglecturer.vlms_backend.entity.TaxDeclarationTemplate;
import com.visitinglecturer.vlms_backend.entity.User;
import com.visitinglecturer.vlms_backend.repository.TaxDeclarationTemplateRepository;
import com.visitinglecturer.vlms_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

@Service
public class TaxDeclarationTemplateService {

    private final TaxDeclarationTemplateRepository taxDeclarationTemplateRepository;
    private final UserRepository userRepository;

    @Value("${file.upload.tax-declaration-template}")  // Load directory path from application.properties
    private String uploadDir;

    public TaxDeclarationTemplateService(TaxDeclarationTemplateRepository taxDeclarationTemplateRepository, UserRepository userRepository) {
        this.taxDeclarationTemplateRepository = taxDeclarationTemplateRepository;
        this.userRepository = userRepository;
    }

    // Method to upload a new Tax Declaration Template
    public String uploadTaxDeclarationTemplate(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty.");
        }

        User adminUser = getAuthenticatedAdmin();

        // Remove existing template if exists
        Optional<TaxDeclarationTemplate> existingTemplate = taxDeclarationTemplateRepository.findAll().stream().findFirst();
        existingTemplate.ifPresent(template -> {
            Path oldFilePath = Path.of(uploadDir, template.getFileName());
            try {
                Files.deleteIfExists(oldFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            taxDeclarationTemplateRepository.delete(template);
        });

        // Save new file
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Path.of(uploadDir, fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Save template details to database
        TaxDeclarationTemplate template = new TaxDeclarationTemplate();
        template.setFileName(fileName);
        template.setUploadedBy(adminUser);
        template.setUploadedAt(LocalDateTime.now());
        taxDeclarationTemplateRepository.save(template);

        return "Tax Declaration Template uploaded successfully.";
    }

    // Method to edit the existing Tax Declaration Template
    public String editTaxDeclarationTemplate(MultipartFile file) throws IOException {
        // Check if the template exists in the system
        Optional<TaxDeclarationTemplate> existingTemplate = taxDeclarationTemplateRepository.findAll().stream().findFirst();
        if (existingTemplate.isEmpty()) {
            throw new IllegalArgumentException("No existing template to edit.");
        }

        // Remove old template if it exists
        TaxDeclarationTemplate template = existingTemplate.get();
        Path oldFilePath = Path.of(uploadDir, template.getFileName());
        try {
            Files.deleteIfExists(oldFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save new file (similar to upload)
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Path.of(uploadDir, fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Update template details in the database
        template.setFileName(fileName);
        template.setUploadedAt(LocalDateTime.now());
        taxDeclarationTemplateRepository.save(template);

        return "Tax Declaration Template updated successfully.";
    }

    // Method to get the latest Tax Declaration Template (non-PDF)
    public Resource getLatestTaxDeclarationTemplate() throws IOException {
        Path dirPath = Paths.get(uploadDir);
        if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
            throw new IllegalArgumentException("Template directory not found.");
        }

        Optional<Path> latestFile = Files.list(dirPath)
                .filter(Files::isRegularFile)
                .max(Comparator.comparingLong(f -> f.toFile().lastModified()));

        if (latestFile.isPresent()) {
            Path filePath = latestFile.get();
            Resource fileResource = new UrlResource(filePath.toUri());
            if (fileResource.exists() && fileResource.isReadable()) {
                return fileResource;
            }
        }
        throw new IllegalArgumentException("No tax declaration template found.");
    }

    // Method to check if a template already exists
    public boolean checkIfTemplateExists() throws IOException {
        Path dirPath = Paths.get(uploadDir);
        if (Files.exists(dirPath) && Files.isDirectory(dirPath)) {
            return Files.list(dirPath).anyMatch(Files::isRegularFile);
        }
        return false;
    }

    // Method to get the latest PDF tax declaration template
    public Resource getLatestTaxDeclarationPdf() throws IOException {
        File dir = new File(uploadDir);

        if (!dir.exists() || !dir.isDirectory()) {
            throw new FileNotFoundException("Directory does not exist or is not a valid directory: " + uploadDir);
        }

        File[] files = dir.listFiles((d, name) -> name.endsWith(".pdf"));

        if (files == null || files.length == 0) {
            throw new FileNotFoundException("No PDF files found in directory: " + uploadDir);
        }

        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
        File latestFile = files[0];

        Path filePath = latestFile.toPath();
        return new UrlResource(filePath.toUri());
    }

    // Helper method to get the authenticated admin user
    private User getAuthenticatedAdmin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userRepository.findByUsername(userDetails.getUsername())
                    .filter(user -> user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN")))
                    .orElseThrow(() -> new SecurityException("Only admins can upload or edit the Tax Declaration Template."));
        }
        throw new SecurityException("Unauthorized access.");
    }
}








