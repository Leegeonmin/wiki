package com.zb.wiki.repository;

import com.zb.wiki.domain.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {
  boolean existsByDocumentIdAndUserId(Long documentId, Long userId);
}
