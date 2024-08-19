package com.zb.wiki.service;


import com.zb.wiki.domain.Document;
import com.zb.wiki.domain.Member;
import com.zb.wiki.dto.DocumentDto;
import com.zb.wiki.exception.GlobalError;
import com.zb.wiki.exception.GlobalException;
import com.zb.wiki.repository.DocumentRepository;
import com.zb.wiki.repository.MemberRepository;
import com.zb.wiki.type.DocumentStatus;
import org.springframework.data.domain.Pageable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DocumentService {

  private static final String TAG_DELIMITER = "|";
  private final DocumentRepository documentRepository;
  private final MemberRepository memberRepository;


  /**
   * 문서 추가 요청 로직 1. jwt로 통해 받은 userid로 사용자 인증 2. 문서 제목이 중복인지 확인 3. List로 입력받은 태그 String으로 변환 후 4. 문서
   * 상태를 Pending으로 저장
   *
   * @param id      jwt UserID
   * @param title   문서 제목
   * @param context 문서 내용
   * @param tags    문서 태그
   */
  @Transactional(readOnly = false)
  public void addDocumentPending(Long id, String title, String context, List<String> tags) {
    Member member = memberRepository.findById(id).orElseThrow(
        () -> new GlobalException(GlobalError.USER_NOT_FOUND)
    );

    if (documentRepository.existsByTitle(title)) {
      throw new GlobalException(GlobalError.DOCUMENT_TITLE_DUPLICATED);
    }

    String tag = setTagToString(tags);

    documentRepository.save(
        Document.builder()
            .documentStatus(DocumentStatus.PENDING)
            .title(title)
            .context(context)
            .tag(tag)
            .createdBy(member)
            .build()
    );
  }


  /**
   * 태그 List -> String 태그 사이는 | 로 구분
   *
   * @param tags 태그 List
   * @return String
   */
  private String setTagToString(List<String> tags) {
    if (tags != null && !tags.isEmpty()) {
      return String.join(TAG_DELIMITER, tags);
    } else {
      return null;
    }
  }

  private List<String> setTagToList(String tag) {
    if (StringUtils.hasText(tag)) {
      return List.of(tag.split("\\" + TAG_DELIMITER));
    } else {
      return List.of();
    }
  }

  /**
   * DB에서 미승인 문서 조회 로직 1. Pending상태인 문서 조회
   *
   * @param pageable Paging처리
   * @return DocumentDto
   */
  public List<DocumentDto> findPendingDocuments(Pageable pageable) {
    List<Document> documents = documentRepository.findByDocumentStatus(DocumentStatus.PENDING,
        pageable);

    return documents.stream().map(
        x -> DocumentDto.builder()
            .id(x.getId())
            .title(x.getTitle())
            .tags(this.setTagToList(x.getTag()))
            .author(x.getCreatedBy().getUsername())
            .build()
    ).toList();

  }

  /**
   * 문서의 ID로 미승인 문서 조회 1. id로 문서가 존재하는지 2. 해당 문서가 승인 대기 문서인지 확인 후 3. 문서 반환
   *
   * @param documentId 문서 Id
   * @return 문서
   */
  public DocumentDto findPendingDocument(Long documentId) {
    Document document = documentRepository.findById(documentId).orElseThrow(
        () -> new GlobalException(GlobalError.DOCUMENT_NOT_FOUND)
    );
    if (document.getDocumentStatus() != DocumentStatus.PENDING) {
      throw new GlobalException(GlobalError.DOCUMENT_TYPE_ERROR);
    }

    return DocumentDto.builder()
        .id(document.getId())
        .title(document.getTitle())
        .tags(this.setTagToList(document.getTag()))
        .author(document.getCreatedBy().getUsername())
        .context(document.getContext())
        .build();

  }
}
