package com.zb.wiki.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zb.wiki.custom.WithCustomMockUser;
import com.zb.wiki.dto.ApproveDocument;
import com.zb.wiki.dto.CreateDocument;
import com.zb.wiki.dto.DocumentDto;
import com.zb.wiki.security.JwtProvider;
import com.zb.wiki.service.ApprovalService;
import com.zb.wiki.service.DocumentSearchService;
import com.zb.wiki.service.DocumentService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = DocumentController.class)
@AutoConfigureMockMvc(addFilters = false)
class DocumentControllerTest {

  @MockBean
  private DocumentService documentService;
  @MockBean
  private ApprovalService approvalService;
  @MockBean
  private DocumentSearchService documentSearchService;
  @MockBean
  private JwtProvider jwtProvider;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;


  @Test
  @DisplayName("문서 추가 요청 성공")
  @WithCustomMockUser
  void createDocumentPending_success() throws Exception {
    //given
    Mockito.doNothing().when(documentService)
        .addDocumentPending(anyLong(), anyString(), anyString(),
            anyList());
    CreateDocument.Request request = CreateDocument.Request.builder()
        .title("title")
        .context("context")
        .tags(new ArrayList<>(Arrays.asList("tag1", "tag2")))
        .build();
    //when
    //then
    mockMvc.perform(post("/document")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("문서 추가 요청 완료"));
  }

  @Test
  @DisplayName("미승인 문서 조회 성공")
  void getPendingDocuments_success() throws Exception {
    //given
    Pageable pageable = Pageable.ofSize(10);
    List<DocumentDto> dtoList = new ArrayList<>();
    DocumentDto dto = DocumentDto.builder().id(10L).author("test")
        .title("title").tags(new ArrayList<>(List.of("tag1", "tag2"))).build();
    dtoList.add(dto);
    given(documentService.findPendingDocuments(pageable))
        .willReturn(dtoList);
    //when
    //then
    mockMvc.perform(get("/document/pending?page=0&size=10")
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("승인 대기 문서 조회 완료"))
        .andExpect(jsonPath("$.data[0].title").value("title"));
  }

  @Test
  @DisplayName("미승인 문서 단일 조회 성공")
  void getPendingDocument_success() throws Exception {
    //given
    DocumentDto dto = DocumentDto.builder().id(10L).author("test")
        .title("title").tags(new ArrayList<>(List.of("tag1", "tag2"))).build();
    given(documentService.findPendingDocument(anyLong()))
        .willReturn(dto);
    //when
    //then
    mockMvc.perform(get("/document/pending/10")
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("승인 대기 문서 단건 조회 완료"))
        .andExpect(jsonPath("$.data.title").value("title"));
  }

  @Test
  @DisplayName("미승인 문서 승인 요청 성공")
  @WithCustomMockUser
  void approveDocument_success() throws Exception {
    //given
    Mockito.doNothing().when(approvalService)
        .approveDocument(anyLong(), anyLong(), anyString());
    ApproveDocument.Request request = ApproveDocument.Request.builder()
        .status("APPROVED")
        .build();
    //when
    //then
    mockMvc.perform(post("/document/1/approval")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("문서 APPROVED 성공"));
  }
}