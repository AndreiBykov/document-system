package edu.leti.documentsystem.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class SaveDocumentService {

    private static final String FOLDER = "document-system";

    public Path saveDocument(MultipartFile file, Long id) throws IOException {
        File filePath = new File(FOLDER + "/" + id);
        filePath.mkdirs();

        byte[] bytes = file.getBytes();
        Path path = Paths.get(filePath.getAbsolutePath() + "/" + file.getOriginalFilename());
        Files.write(path, bytes);
        return path;
    }
}
