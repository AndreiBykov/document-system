package edu.leti.documentsystem.repositories;

import edu.leti.documentsystem.domain.DocumentMetadata;
import edu.leti.documentsystem.domain.Shingle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ShinglesRepository extends JpaRepository<Shingle, Long> {

    Optional<Shingle> findByShingle(String shingle);

    Set<Shingle> findByDocumentMetadatas_IdNot(Long id);
}
