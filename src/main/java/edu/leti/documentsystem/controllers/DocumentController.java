package edu.leti.documentsystem.controllers;

import edu.leti.documentsystem.domain.DocumentMetadata;
import edu.leti.documentsystem.repositories.DocumentMetadataRepository;
import edu.leti.documentsystem.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.util.List;

@Controller
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    DocumentService documentService;

    @Autowired
    DocumentMetadataRepository documentMetadataRepository;

    @PostMapping("/upload")
    public String upload(MultipartFile file) {

        try {
            documentService.uploadDocument(file);
            return "redirect:/";
        } catch (Exception ignored) {
        }
        return "error";
    }

    @GetMapping("find")
    @ResponseBody
    public List<DocumentMetadata> findDocuments(String query) {
        return documentService.findDocumentsByContent(query);
    }

    @GetMapping(value = "/{docId}",produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public FileSystemResource getFile(
        @PathVariable
            String docId) {

        return new FileSystemResource(documentService.getFileById(Long.valueOf(docId)));
    }
}
