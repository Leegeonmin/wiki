package com.zb.wiki.service;

import com.zb.wiki.domain.Document;
import com.zb.wiki.dto.DocumentSearchDto;
import com.zb.wiki.dto.SearchType;
import com.zb.wiki.elasticsearch.DocumentDocument;
import com.zb.wiki.repository.DocumentSearchRepository;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentSearchService {

  private final ElasticsearchOperations elasticsearchOperations;
  private final DocumentSearchRepository documentSearchRepository;
  private final RedisTemplate<String, String> redisTemplate;
  private static final String SEARCH_KEY = "search:keywords";
  private static final String TOP_SEARCHES_KEY = "search:top";

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

    addSearchKeyword(keyword);

    return list.getContent()
        .stream()
        .map(DocumentSearchDto::of)
        .collect(Collectors.toList());
  }

  private void addSearchKeyword(String keyword) {
    redisTemplate.opsForZSet().incrementScore(SEARCH_KEY, keyword, 1);
    redisTemplate.expire(SEARCH_KEY, Duration.ofMinutes(5));
  }

  public List<String> getTopSearches() {
    return redisTemplate.opsForList().range(TOP_SEARCHES_KEY, 0, -1);
  }

  @Scheduled(fixedRate = 300000)
  public void updateTopSearches() {
    Set<String> topKeywords = redisTemplate.opsForZSet().reverseRange(SEARCH_KEY, 0, 9);
    redisTemplate.opsForList().rightPushAll(TOP_SEARCHES_KEY, topKeywords);
    redisTemplate.expire(TOP_SEARCHES_KEY, Duration.ofMinutes(5));
  }

}
