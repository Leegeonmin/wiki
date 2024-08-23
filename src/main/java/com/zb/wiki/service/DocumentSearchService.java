package com.zb.wiki.service;

import com.zb.wiki.domain.Document;
import com.zb.wiki.dto.DocumentSearchDto;
import com.zb.wiki.dto.SearchType;
import com.zb.wiki.elasticsearch.DocumentDocument;
import com.zb.wiki.repository.DocumentSearchRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentSearchService {

  private final ElasticsearchOperations elasticsearchOperations;
  private final DocumentSearchRepository documentSearchRepository;

  public void save(Document document) {
    elasticsearchOperations.save(DocumentDocument.from(document));
  }

  public List<DocumentSearchDto> search(String keyword, SearchType searchType, Pageable pageable) {
    Page<DocumentDocument> list;
    if (searchType == SearchType.TITLE) {
      list = documentSearchRepository.findByTitleContainsIgnoreCase(keyword, pageable);
    } else if (searchType == SearchType.TAG) {
      list = documentSearchRepository.findByTagsContainsIgnoreCase(keyword, pageable);
    } else {
      list = documentSearchRepository.findByContextContainsIgnoreCase(keyword, pageable);
    }

    return list.getContent()
        .stream()
        .map(DocumentSearchDto::of)
        .collect(Collectors.toList());
  }
}
