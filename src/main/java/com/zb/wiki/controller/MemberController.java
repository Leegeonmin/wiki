package com.zb.wiki.controller;

import com.zb.wiki.dto.SignUp;
import com.zb.wiki.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

  private final MemberService memberService;


  /**
   * 회원가입 API
   * @param request 사용자 아이디, 비밀번호, 이메일
   * @return 회원가입 성공메시지
   */
  public ResponseEntity<String> signUp(@RequestBody @Valid SignUp.Request request) {
    log.info("signUp request : {}", request);
    memberService.signUp(request.getUsername(), request.getPassword(), request.getEmail());
    return ResponseEntity.ok().body("Sign up successful");
  }
}
