package com.kadod.fingerprint.services;

import com.kadod.fingerprint.util.ImageUtils;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.DataFormatException;

@Component
public class FileService {

    private static final String FILE_DIRECTORY = "src/main/resources/images/";

    // Define the directory where the uploaded files will be stored
    private static final String UPLOAD_DIR = "/app/uploads/";


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

    public byte[] downloadImage(byte[] imageName) {
        try {
                return ImageUtils.decompressImage(imageName);
            } catch (DataFormatException | IOException exception) {
                throw new ContextedRuntimeException("Error downloading an image", exception);
            }
    }

    public String imageUpload(MultipartFile file) {

        try {
            // Create the upload directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Get the file's original filename and create a new file path
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            Path filePath = uploadPath.resolve(fileName);
            // Save the file locally on the server
            Files.write(filePath, file.getBytes());
            System.out.print(filePath);
            return  fileName;
//            return new ResponseEntity<>("File uploaded successfully: " + fileName, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
//            return new ResponseEntity<>("Could not upload the file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
//        @PostMapping("/upload")
//        public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
//            if (file.isEmpty()) {
//                return new ResponseEntity<>("Please select a file to upload!", HttpStatus.BAD_REQUEST);
//            }
//        }
    }


}

