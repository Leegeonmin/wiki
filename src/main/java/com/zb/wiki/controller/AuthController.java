package com.zb.wiki.controller;

import com.zb.wiki.dto.GlobalResponse;
import com.zb.wiki.dto.KakaoOauth2;
import com.zb.wiki.dto.SignIn;
import com.zb.wiki.dto.SignIn.Response;
import com.zb.wiki.dto.SignUp;
import com.zb.wiki.security.JwtProvider;
import com.zb.wiki.service.MemberService;
import com.zb.wiki.service.Oauth2Service;
import com.zb.wiki.type.GlobalResponseStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final MemberService memberService;
  private final JwtProvider jwtProvider;
  private final Oauth2Service oauth2Service;

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
            .status(GlobalResponseStatus.SUCCESS)
            .message("회원가입 성공")
            .build()
    );
  }

  /**
   * 로그인 API
   * @param request 사용자 아이디, 비밀번호
   * @return JWT
   */
  @PostMapping("/signin")
  public ResponseEntity<GlobalResponse<Response>> signIn(
      @RequestBody @Valid SignIn.Request request) {
    log.info("signIn request : {}", request);
    memberService.signIn(request.getUsername(), request.getPassword());

    String jwt = jwtProvider.generateToken(request.getUsername());

    return ResponseEntity.ok().body(
        GlobalResponse.<SignIn.Response>builder()
            .status(GlobalResponseStatus.SUCCESS)
            .message("로그인 성공")
            .data(Response.builder().accessToken(jwt).build())
            .build()
    );
  }


  /**
   * 사용자 kakao 인증 로그인 후 카카오 리소스 서버에서 오는 callback api
   * @param code 카카오 리소스 서버에서 발급해 주는 코드
   * @return JWT
   */
  @GetMapping("/oauth2/kakao")
  public ResponseEntity<GlobalResponse<KakaoOauth2.Response>> kakaoOauth2(@RequestParam(name = "code") String code){
    String username = oauth2Service.loginOrRegisterKakaoUser(code);
    String jwt = jwtProvider.generateToken(username);
    return ResponseEntity.ok().body(
        GlobalResponse.<KakaoOauth2.Response>builder()
            .status(GlobalResponseStatus.SUCCESS)
            .message("로그인 성공")
            .data(KakaoOauth2.Response.builder().accessToken(jwt).build())
            .build()
    );
  }
}
