package com.zb.wiki.repository;

import com.zb.wiki.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
  boolean existsByTitle(String title);
}
