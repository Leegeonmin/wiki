package com.zb.wiki.controller;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zb.wiki.custom.WithCustomMockUser;
import com.zb.wiki.dto.CreateDocument;
import com.zb.wiki.security.JwtProvider;
import com.zb.wiki.service.DocumentService;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = DocumentController.class)
@AutoConfigureMockMvc(addFilters = false)
class DocumentControllerTest {

  @MockBean
  private DocumentService documentService;
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
}