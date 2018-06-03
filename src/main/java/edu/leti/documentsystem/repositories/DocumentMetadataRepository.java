package edu.leti.documentsystem.repositories;

import edu.leti.documentsystem.domain.DocumentMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentMetadataRepository extends JpaRepository<DocumentMetadata, Long> {

}
