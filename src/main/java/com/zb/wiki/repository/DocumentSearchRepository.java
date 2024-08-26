package com.zb.wiki.repository;

import com.zb.wiki.elasticsearch.DocumentDocument;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface DocumentSearchRepository extends ElasticsearchRepository<DocumentDocument, Long> {
  Optional<DocumentDocument> findById(Long id);
  Page<DocumentDocument> findByTitleContainsIgnoreCase(String title, Pageable pageable);

  Page<DocumentDocument> findByContextContainsIgnoreCase(String keyword, Pageable pageable);

  Page<DocumentDocument> findByTagsContainsIgnoreCase(String keyword, Pageable pageable);
}
