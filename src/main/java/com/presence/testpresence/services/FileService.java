package com.presence.testpresence.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileService {

    private static final String FILE_DIRECTORY = "src/main/resources/images/";

    public String uploadFile(MultipartFile file){
        if (file.isEmpty()) {
            return "Veuillez sélectionner un fichier à uploader.";
        }

        try {
            // Enregistrer le fichier sur le serveur
            String filePath = FILE_DIRECTORY + file.getOriginalFilename();
            File dest = new File(filePath);
            file.transferTo(dest);
            return "Fichier uploadé avec succès : " + filePath;
        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur lors de l'upload du fichier.";
        }
    }

    public Resource getFile(String fileName) throws IOException {

        Path filePath = Paths.get(FILE_DIRECTORY, fileName);
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
//            return ResponseEntity.ok()
//                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                    .body(resource);
        } else {
            // Gérer le cas où le fichier n'existe pas ou n'est pas accessible
            return resource;
        }
    }

}

