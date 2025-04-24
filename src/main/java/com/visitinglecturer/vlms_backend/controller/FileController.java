package com.visitinglecturer.vlms_backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:5173")
public class FileController {

    @Value("${file.upload.tax-declaration-template}")
    private String UPLOAD_DIR;

    @GetMapping("/pdf/tax-declaration/latest")
    public ResponseEntity<Resource> getLatestTaxDeclarationPdf() throws IOException {
        File dir = new File(UPLOAD_DIR);

        if (!dir.exists() || !dir.isDirectory()) {
            return ResponseEntity.badRequest().body(null);
        }

        File[] files = dir.listFiles((d, name) -> name.endsWith(".pdf"));

        if (files == null || files.length == 0) {
            return ResponseEntity.notFound().build();
        }

        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
        File latestFile = files[0];

        Path filePath = latestFile.toPath();
        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + latestFile.getName())
                .body(resource);
    }
}

