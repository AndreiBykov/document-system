package edu.leti.documentsystem.services;

import edu.leti.documentsystem.domain.DocumentMetadata;
import edu.leti.documentsystem.domain.Shingle;
import edu.leti.documentsystem.repositories.ShinglesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlagiarismService {

    @Autowired
    private ShinglesRepository shinglesRepository;

    private static final String STOP_SYMBOLS[] = {".", ",", "!", "?", ":", ";", "-", "\\", "/", "*", "(", ")", "+", "@",
                                                  "#", "$", "%", "^", "&", "=", "'", "\"", "[", "]", "{", "}", "|"};
    private static final String STOP_WORDS[] = {"это", "как", "так", "и", "в", "над", "к", "до", "не", "на", "но",
                                                "за", "то", "с", "ли", "а", "во", "от", "со", "для", "о", "же",
                                                "ну", "вы", "бы", "что", "кто", "он", "она"};
    private static final int SHINGLE_LEN = 5;

    public Set<Shingle> getShingles(String text, DocumentMetadata documentMetadata) {
        text = canonize(text);
        String[] words = text.split(" ");

        int shinglesNumber = words.length - SHINGLE_LEN;

        Set<String> hashes = new HashSet<>(shinglesNumber);

        for (int i = 0; i <= shinglesNumber; i++) {
            StringBuilder shingle = new StringBuilder();

            for (int j = 0; j < SHINGLE_LEN; j++) {
                shingle.append(words[i + j]).append(" ");
            }

            hashes.add(DigestUtils.md5DigestAsHex(shingle.toString().getBytes()));
        }

        return hashes.stream()
            .map(hash -> shinglesRepository.findByShingle(hash).orElse(new Shingle(hash)))
            .collect(Collectors.toSet());
    }


    public Float calculatePlagiarism(DocumentMetadata documentMetadata) {

        Set<Shingle> shingles = documentMetadata.getShingles();
        return calculatePlagiarism(shingles, documentMetadata);
    }

    public Float calculatePlagiarism(Set<Shingle> shingles, DocumentMetadata documentMetadata) {

        HashSet<Shingle> allShingles =
            new HashSet<>(shinglesRepository.findByDocumentMetadatas_IdNot(documentMetadata.getId()));

        allShingles.retainAll(shingles);

        return 100.0f * allShingles.size() / shingles.size();
    }

    private String canonize(String text) {
        String string = text.toLowerCase();

        for (String stopSymbol : STOP_SYMBOLS) {
            string = string.replace(stopSymbol, " ");
        }

        for (String stopWord : STOP_WORDS) {
            string = string.replace("" + stopWord + " ", " ");
        }

        string = string.trim().replaceAll(" +", " ");

        return string;
    }
}
