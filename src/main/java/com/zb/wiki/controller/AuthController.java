package com.zb.wiki.controller;

import com.zb.wiki.dto.GlobalResponse;
import com.zb.wiki.dto.SignIn;
import com.zb.wiki.dto.SignIn.Response;
import com.zb.wiki.dto.SignUp;
import com.zb.wiki.security.JwtProvider;
import com.zb.wiki.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final MemberService memberService;
  private final JwtProvider jwtProvider;

  /**
   * 회원가입 API
   *
   * @param request 사용자 아이디, 비밀번호, 이메일
   * @return 회원가입 성공메시지
   */
  @PostMapping("/signup")
  public ResponseEntity<GlobalResponse<String>> signUp(@RequestBody @Valid SignUp.Request request) {
    log.info("signUp request : {}", request);
    memberService.signUp(request.getUsername(), request.getPassword(), request.getEmail());
    return ResponseEntity.ok().body(
        GlobalResponse.<String>builder()
            .status("success")
            .message("회원가입 성공")
            .build()
    );
  }

  @PostMapping("/signin")
  public ResponseEntity<GlobalResponse<Response>> signIn(
      @RequestBody @Valid SignIn.Request request) {
    log.info("signIn request : {}", request);
    memberService.signIn(request.getUsername(), request.getPassword());

    String jwt = jwtProvider.generateToken(request.getUsername());

    return ResponseEntity.ok().body(
        GlobalResponse.<SignIn.Response>builder()
            .status("success")
            .message("로그인 성공")
            .data(Response.builder().accessToken(jwt).build())
            .build()
    );
  }
}
