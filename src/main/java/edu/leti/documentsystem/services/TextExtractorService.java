package edu.leti.documentsystem.services;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

@Service
public class TextExtractorService {

    public String retrieveDocumentContent(InputStream inputStream) throws IOException, TikaException, SAXException {
        BodyContentHandler handler = new BodyContentHandler(1_000_000);

        AutoDetectParser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();

        parser.parse(inputStream, handler, metadata);

        return handler.toString();
    }
}
