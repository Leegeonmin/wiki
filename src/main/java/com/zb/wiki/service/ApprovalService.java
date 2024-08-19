package com.zb.wiki.service;

import com.zb.wiki.domain.Approval;
import com.zb.wiki.domain.Document;
import com.zb.wiki.domain.Member;
import com.zb.wiki.exception.GlobalError;
import com.zb.wiki.exception.GlobalException;
import com.zb.wiki.repository.ApprovalRepository;
import com.zb.wiki.repository.DocumentRepository;
import com.zb.wiki.repository.MemberRepository;
import com.zb.wiki.type.DocumentStatus;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApprovalService {
  private final ApprovalRepository approvalRepository;
  private final DocumentRepository documentRepository;
  private final MemberRepository memberRepository;

  private static final int APPROVAL_PERIOD = 7;


  /**
   * 문서 요청 로직
   * 1. 멤버Id가 유효한지
   * 2. 문서Id가 유효한지
   * 3. 문서 유효한지 검사 후
   * 4. Approval 저장
   * @param userId Jwt 유저
   * @param documentId 문서Id
   * @param status 승인 상태, APPROVED or REJECTED
   */
  @Transactional(readOnly = false)
  public void approveDocument(Long userId, Long documentId, String status) {
    Member member = memberRepository.findById(userId).orElseThrow(
        () -> new GlobalException(GlobalError.USER_NOT_FOUND)
    );

    Document document = documentRepository.findById(documentId).orElseThrow(
        () -> new GlobalException(GlobalError.DOCUMENT_NOT_FOUND)
    );
    approveDocumentValidation(document, member);

    approvalRepository.save(
        Approval.builder()
            .user(member)
            .document(document)
            .isApproved(status.equalsIgnoreCase("APPROVED"))
            .build()
    );

  }


  /**
   * 문서 요청 검증 로직
   * 1. 문서의 추가 요청 기간 후 일주일 이내인지
   * 2. 문서의 상태가 PENDING인지
   * 3. 문서에 이미 사용자가 승인 요청을 한 이력이 있는지 확인
   * @param document 문서
   * @param user 유저
   */
  private void approveDocumentValidation(Document document, Member user) {

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime oneWeekAfterCreation = document.getCreatedDateTime().plusDays(APPROVAL_PERIOD);

    if (now.isAfter(oneWeekAfterCreation)) {
      throw new GlobalException(GlobalError.APPROVAL_PERIOD_EXPIRED);
    }

    if(document.getDocumentStatus() != DocumentStatus.PENDING){
      throw new GlobalException(GlobalError.DOCUMENT_TYPE_ERROR);
    }
    if(approvalRepository.existsByDocumentIdAndUserId(document.getId(), user.getId())){
      throw new GlobalException(GlobalError.USER_ALREADY_SUBMIT);
    }
  }
}
