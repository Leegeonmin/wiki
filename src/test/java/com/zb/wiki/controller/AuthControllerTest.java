package com.zb.wiki.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;

import com.zb.wiki.security.JwtProvider;
import com.zb.wiki.service.MemberService;
import com.zb.wiki.service.Oauth2Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false )
class AuthControllerTest {

  @MockBean
  private MemberService memberService;
  @MockBean
  private JwtProvider jwtProvider;
  @MockBean
  private Oauth2Service oauth2Service;
  @Autowired
  private MockMvc mockMvc;


  @Test
  @DisplayName("회원가입 성공")
  void signUp() throws Exception {
    //given
    Mockito.doNothing().when(memberService).signUp(anyString(), anyString(), anyString());

    //when
    //then
    mockMvc.perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\" : \"testuser\", "
                + "\"password\":\"q1w2e3r4!\", "
                + "\"email\" : \"lkm5611@naver.com\"}")
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("회원가입 성공"));
  }

  @Test
  @DisplayName("로그인 성공")
  void signIn() throws Exception {
    //given
    Mockito.doNothing().when(memberService).signIn(anyString(), anyString());
    given(jwtProvider.generateToken(anyString()))
        .willReturn("testaccesstoken");

    //when
    mockMvc.perform(post("/auth/signin")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ "
                + "\"username\" : \"lee\", "
                + "\"password\" :  \"q1w2e3r4!\" "
                + "}")
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.data.accessToken").value("testaccesstoken"))
        .andDo(print());
  }
}