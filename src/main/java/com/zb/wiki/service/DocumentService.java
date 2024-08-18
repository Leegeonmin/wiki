package com.zb.wiki.service;


import com.zb.wiki.domain.Document;
import com.zb.wiki.domain.Member;
import com.zb.wiki.exception.GlobalError;
import com.zb.wiki.exception.GlobalException;
import com.zb.wiki.repository.DocumentRepository;
import com.zb.wiki.repository.MemberRepository;
import com.zb.wiki.type.DocumentStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DocumentService {

  private static final String TAG_DELIMITER = "|";
  private final DocumentRepository documentRepository;
  private final MemberRepository memberRepository;


  /**
   * 문서 추가 요청 로직
   * 1. jwt로 통해 받은 userid로 사용자 인증
   * 2. 문서 제목이 중복인지 확인
   * 3. List로 입력받은 태그 String으로 변환 후
   * 4. 문서 상태를 Pending으로 저장
   * @param id jwt UserID
   * @param title 문서 제목
   * @param context 문서 내용
   * @param tags 문서 태그
   */
  @Transactional(readOnly = false)
  public void addDocumentPending(Long id, String title, String context, List<String> tags) {
    Member member = memberRepository.findById(id).orElseThrow(
        () -> new GlobalException(GlobalError.USER_NOT_FOUND)
    );

    if (documentRepository.existsByTitle(title)) {
      throw new GlobalException(GlobalError.DOCUMENT_TITLE_DUPLICATED);
    }

    String tag = null;
    if (tags != null && !tags.isEmpty()) {
      tag = setTag(tags);
    }
    documentRepository.save(
        Document.builder()
            .documentStatus(DocumentStatus.PENDING)
            .title(title)
            .context(context)
            .tag(tag)
            .modifiedBy(member)
            .build()
    );
  }


  /**
   * 태그 List -> String
   * 태그 사이는 | 로 구분
   * @param tags 태그 List
   * @return String
   */
  private String setTag(List<String> tags) {
    return String.join(TAG_DELIMITER, tags);
  }
}
