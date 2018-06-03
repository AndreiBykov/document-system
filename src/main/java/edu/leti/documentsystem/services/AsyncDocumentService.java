package edu.leti.documentsystem.services;

import edu.leti.documentsystem.domain.Document;
import edu.leti.documentsystem.domain.DocumentMetadata;
import edu.leti.documentsystem.domain.Shingle;
import edu.leti.documentsystem.repositories.DocumentMetadataRepository;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import javax.transaction.Transactional;

@Service
public class AsyncDocumentService {

    @Autowired
    private TextExtractorService textExtractorService;

    @Autowired
    private DocumentMetadataRepository documentMetadataRepository;

    @Autowired
    private PlagiarismService plagiarismService;

    @Async
    @Transactional
    public void processDocument(Path path, DocumentMetadata documentMetadata)
        throws IOException, TikaException, SAXException {

        try (InputStream inputStream = Files.newInputStream(path)) {

            String text = textExtractorService.retrieveDocumentContent(inputStream);
            documentMetadata.setDocument(new Document(text));

            Set<Shingle> shingles = plagiarismService.getShingles(text, documentMetadata);
            documentMetadata.setShingles(shingles);

            Float plagiarism = plagiarismService.calculatePlagiarism(shingles, documentMetadata);
            documentMetadata.setPlagiarism(plagiarism);

            documentMetadataRepository.saveAndFlush(documentMetadata);
        }
    }
}
