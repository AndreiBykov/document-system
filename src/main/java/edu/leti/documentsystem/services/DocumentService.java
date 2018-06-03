package edu.leti.documentsystem.services;

import edu.leti.documentsystem.domain.Document;
import edu.leti.documentsystem.domain.DocumentMetadata;
import edu.leti.documentsystem.repositories.DocumentMetadataRepository;
import edu.leti.documentsystem.repositories.DocumentRepository;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    @Autowired
    private TextExtractorService textExtractorService;

    @Autowired
    private SaveDocumentService saveDocumentService;

    @Autowired
    private DocumentMetadataRepository documentMetadataRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private AsyncDocumentService asyncDocumentService;

    public void uploadDocument(MultipartFile file) throws IOException, TikaException, SAXException {
        DocumentMetadata documentMetadata = new DocumentMetadata(file.getOriginalFilename(), LocalDateTime.now());
        documentMetadata = documentMetadataRepository.save(documentMetadata);

        Path path = saveDocumentService.saveDocument(file, documentMetadata.getId());

        asyncDocumentService.processDocument(path, documentMetadata);
    }

    public List<DocumentMetadata> findDocumentsByContent(String content) {
        return documentRepository.findByContentContainingIgnoreCase(content)
            .stream()
            .map(Document::getDocumentMetadata)
            .collect(Collectors.toList());
    }

    public List<DocumentMetadata> findAllDocuments() {
        return documentMetadataRepository.findAll();
    }

    public File getFileById(Long id) {
        String filename = documentMetadataRepository.findById(id)
            .map(DocumentMetadata::getFilename)
            .orElseThrow(IllegalStateException::new);
        File file = new File("document-system/" + id + "/" + filename);
        System.out.println(file);
        return file;
    }
}
