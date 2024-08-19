package com.zb.wiki.repository;

import com.zb.wiki.domain.Document;
import com.zb.wiki.type.DocumentStatus;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
  boolean existsByTitle(String title);
  List<Document> findByDocumentStatus(DocumentStatus documentStatus, Pageable pageable);
}
