package com.presence.testpresence.controllers;
import com.presence.testpresence.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        // Process the uploaded file
        return "File uploaded successfully: " + file.getOriginalFilename();
    }

    @GetMapping("/{filename}")
    public String getFileDetails(@PathVariable String filename) {
        // Retrieve information about the file
        return "Details for file: " + filename;
    }
}