package edu.leti.documentsystem.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(indexes = {@Index(name = "IND_SHINGLE",
    columnList = "shingle")})
@Data
@NoArgsConstructor
public class Shingle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String shingle;

    @ManyToMany(fetch = FetchType.LAZY,
        cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
        },
        mappedBy = "shingles")
    private Set<DocumentMetadata> documentMetadatas = new HashSet<>();

    public Shingle(String shingle) {
        this.shingle = shingle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        Shingle shingle1 = (Shingle) o;

        return shingle != null ? shingle.equals(shingle1.shingle) : shingle1.shingle == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (shingle != null ? shingle.hashCode() : 0);
        return result;
    }
}
