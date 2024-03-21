package com.presence.testpresence.controllers;
import com.presence.testpresence.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

import java.io.File;

@RestController
@RequestMapping("/files")
public class FileController {


    @Autowired
    FileService fileService;

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        // Vérifier si le fichier est vide
        return this.fileService.uploadFile(file);
    }

    @GetMapping("/files")
    public ResponseEntity<Resource> getFile(@RequestParam String fileName) throws IOException {
        Resource resource = this.fileService.getFile(fileName);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

}