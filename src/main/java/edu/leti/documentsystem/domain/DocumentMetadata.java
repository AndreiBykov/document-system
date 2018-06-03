package edu.leti.documentsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
@Data
@NoArgsConstructor
public class DocumentMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String filename;
    private LocalDateTime uploadDateTime;
    private Float plagiarism;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY,
        cascade = CascadeType.ALL)
    @JoinColumn(name = "document_id")
    private Document document;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY,
        cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
        })
    @JoinTable(name = "documents_shingles",
        joinColumns = {@JoinColumn(name = "document_metadata_id")},
        inverseJoinColumns = {@JoinColumn(name = "shingle_id")})
    private Set<Shingle> shingles = new HashSet<>();

    public DocumentMetadata(String filename, LocalDateTime uploadDateTime) {
        this.filename = filename;
        this.uploadDateTime = uploadDateTime;
    }
}
