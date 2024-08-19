package com.zb.wiki.controller;


import com.zb.wiki.dto.CreateDocument;
import com.zb.wiki.dto.CustomUserDetailsDto;
import com.zb.wiki.dto.DocumentDto;
import com.zb.wiki.dto.GetPendingDocuments;
import com.zb.wiki.dto.GlobalResponse;
import com.zb.wiki.service.DocumentService;
import com.zb.wiki.type.GlobalResponseStatus;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/document")
@RequiredArgsConstructor
@Slf4j
public class DocumentController {

  private final DocumentService documentService;


  /**
   * 문서 추가 요청 API
   *
   * @param request 문서의 제목, 내용, 태그
   * @param member  jwt를 통해 받은 유저 정보
   * @return 문서 추가 요청 완료 메시지
   */
  @PostMapping
  public ResponseEntity<GlobalResponse<String>> createDocumentPending(
      @Valid @RequestBody CreateDocument.Request request,
      @AuthenticationPrincipal CustomUserDetailsDto member) {
    log.info("Create document pending");
    documentService.addDocumentPending(member.getId(), request.getTitle(), request.getContext(),
        request.getTags());
    return ResponseEntity.ok().body(
        GlobalResponse.<String>builder()
            .status(GlobalResponseStatus.SUCCESS)
            .message("문서 추가 요청 완료")
            .build()
    );
  }

  /**
   * 미승인 문서 조회 API
   * 문서 추가 요청을 통해 등록은 되었지만 승인되지 않은 문서들을 조회할 수 있는 API
   * @param pageable Paging처리
   * @return 승인 대기 문서
   */
  @GetMapping("/pending")
  public ResponseEntity<GlobalResponse<List<GetPendingDocuments.Response>>> getPendingDocuments(
      Pageable pageable) {
    log.info("Get pending documents");
    List<DocumentDto> pendingDocuments = documentService.findPendingDocuments(pageable);

    return ResponseEntity.ok().body(
        GlobalResponse.<List<GetPendingDocuments.Response>>builder()
            .status(GlobalResponseStatus.SUCCESS)
            .message("승인 대기 문서 조회 완료")
            .data(
                pendingDocuments.stream().map(
                    x -> GetPendingDocuments.Response.builder()
                        .id(x.getId())
                        .title(x.getTitle())
                        .tags(x.getTags())
                        .author(x.getAuthor())
                        .build()
                ).toList()
            )
            .build()
    );

  }
}
