package com.kadod.fingerprint.controllers;
import com.kadod.fingerprint.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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