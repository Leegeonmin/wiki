package com.zb.wiki.repository;

import com.zb.wiki.domain.Document;
import com.zb.wiki.type.DocumentStatus;
import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
  boolean existsByTitle(String title);
  List<Document> findByDocumentStatus(DocumentStatus documentStatus, Pageable pageable);
  @Query("SELECT d FROM Document d WHERE d.documentStatus = :status AND d.createdDateTime <= :date")
  List<Document> findByDocumentStatusAndCreatedDateBefore(
      @Param("status") DocumentStatus status,
      @Param("date") LocalDateTime date);
  @Modifying
  @Query("UPDATE Document d SET d.documentStatus = ?2 WHERE d.id = ?1")
  void updateDocumentStatusById(Long id, DocumentStatus documentStatus);
}
