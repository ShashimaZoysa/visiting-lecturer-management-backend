package com.visitinglecturer.vlms_backend.controller;

import com.visitinglecturer.vlms_backend.service.TaxDeclarationTemplateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;

@RestController
@RequestMapping("/api/tax-declaration-template")
@CrossOrigin(origins = "http://localhost:5173")
public class TaxDeclarationTemplateController {

    private final TaxDeclarationTemplateService taxDeclarationTemplateService;

    public TaxDeclarationTemplateController(TaxDeclarationTemplateService taxDeclarationTemplateService) {
        this.taxDeclarationTemplateService = taxDeclarationTemplateService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadTaxDeclarationTemplate(@RequestParam("file") MultipartFile file) {
        try {
            String message = taxDeclarationTemplateService.uploadTaxDeclarationTemplate(file);
            return ResponseEntity.ok(message);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading the template: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid file: " + e.getMessage());
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<String> editTaxDeclarationTemplate(@RequestParam("file") MultipartFile file) {
        try {
            String message = taxDeclarationTemplateService.editTaxDeclarationTemplate(file);
            return ResponseEntity.ok(message);
        } catch (IOException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error editing the template: " + e.getMessage());
        }
    }

    @GetMapping("/latest")
    public ResponseEntity<Resource> getLatestTaxDeclarationTemplate() {
        try {
            Resource resource = taxDeclarationTemplateService.getLatestTaxDeclarationTemplate();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkIfTemplateExists() {
        try {
            boolean exists = taxDeclarationTemplateService.checkIfTemplateExists();
            return ResponseEntity.ok(exists);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
    @Value("${file.upload.tax-declaration-template}")
    private String UPLOAD_DIR;

    @GetMapping("/pdf/latest")
    public ResponseEntity<Resource> getLatestTaxDeclarationPdf() {
        try {
            // Get the resource from your service
            Resource resource = taxDeclarationTemplateService.getLatestTaxDeclarationPdf();

            // If the file exists, return it as a resource with appropriate headers
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF) // Set MIME type for PDF
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + resource.getFilename())
                    .body(resource); // Return the file as the body of the response

        } catch (IOException e) {
            // If the file doesn't exist or an error occurs, return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



}







