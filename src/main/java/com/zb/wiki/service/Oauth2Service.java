package com.zb.wiki.service;

import com.zb.wiki.domain.Member;
import com.zb.wiki.dto.KakaoTokenResponse;
import com.zb.wiki.dto.KakaoUserInfo;
import com.zb.wiki.exception.GlobalError;
import com.zb.wiki.exception.GlobalException;
import com.zb.wiki.repository.MemberRepository;
import com.zb.wiki.type.Oauth2ProviderType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class Oauth2Service {

  private final RestClient restClient;
  private final MemberRepository memberRepository;
  @Value("${spring.oauth2.kakao.redirect-uri}")
  private String kakaoRedirect_uri;

  @Value("${spring.oauth2.kakao.client-id}")
  private String kakaoClientId;

  @Value("${spring.oauth2.kakao.grant-type}")
  private String kakaoGrantType;

  @Value("${spring.oauth2.kakao.token-uri}")
  private String kakaoTokenUri;

  @Value("${spring.oauth2.kakao.userinfo-uri}")
  private String kakaoUserInfoUri;


  /**
   * 카카오 Oauth2 인증 후 서비스 연동 메서드 1. 카카오 로그인을 통해 받은 code로 AccessToken을 받아옴 2. AccessToken을 이용해 사용자 정보
   * 조회 3. 사용자 정보 조회 후 일치하는 사용자 정보가 DB에 존재하면 연동, 없으면 회원가입 이미 연동 되어있으면 정보 업데이트 4. Jwt 발급을 위해 사용자 이름
   * 리턴
   *
   * @param code 카카오 로그인을 통해 받은 code
   * @return 사용자 이름
   */
  @Transactional(readOnly = false)
  public String loginOrRegisterKakaoUser(String code) {
    String kakaoAccessToken = requestAccessToken(code);
    KakaoUserInfo userInfo = getUserInfo(kakaoAccessToken);
    Member member = findOrCreateMember(userInfo);
    return member.getUsername();
  }


  /**
   * AccessToken 발급 메서드
   *
   * @param code 카카오 로그인을 통해 받은 code
   * @return AccessToken
   */
  private String requestAccessToken(String code) {
    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
    requestBody.add("grant_type", kakaoGrantType);
    requestBody.add("client_id", kakaoClientId);
    requestBody.add("redirect_uri", kakaoRedirect_uri);
    requestBody.add("code", code);

    try {
      KakaoTokenResponse response = restClient.post().uri(kakaoTokenUri)
          .headers(httpHeaders -> httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED))
          .body(requestBody).retrieve().body(KakaoTokenResponse.class);

      if (response == null) {
        throw new GlobalException(GlobalError.USER_NOT_FOUND);
      }
      return response.getAccess_token();

    } catch (HttpClientErrorException e) {
      log.error(e.getResponseBodyAsString());
      throw new GlobalException(GlobalError.RESOURCE_SERVER_ERROR);

    }
  }

  /**
   * 사용자 정보 조회 메서드
   *
   * @param accessToken 사용자 정보 조회를 위한 AccessToken
   * @return 카카오 사용자 정보 조회 결과
   */
  private KakaoUserInfo getUserInfo(String accessToken) {
    KakaoUserInfo userInfo = restClient.get().uri(kakaoUserInfoUri)
        .headers(headers -> headers.setBearerAuth(accessToken)).retrieve()
        .body(KakaoUserInfo.class);

    if (userInfo != null) {
      return userInfo;
    } else {
      throw new RuntimeException("Failed to get user info from Kakao");
    }
  }

  /**
   * 카카오에서 받은 사용자 정보 연동, 비교, 업데이트 로직 1. 사용자 정보를 OauthProvider, OauthId를 비교해 DB에 존재하는지 확인 2. 존재한다면
   * 가져온 사용자 정보로 업데이트 3. 이메일 비교를 통해 Oauth2계정이 아닌 연동할 계정이 있는지 확인 4. 계정이 있다면 OauthProvider, OauthId 삽입
   * 5. 계정이 없다면 카카오에서 받은 정보로 회원가입 진행
   *
   * @param userInfo 카카오에서 받은 사용자 정보
   * @return Member 엔티티
   */
  private Member findOrCreateMember(KakaoUserInfo userInfo) {
    Optional<Member> byOauthProviderAndOauthId = memberRepository.findByOauthProviderAndOauthId(
        Oauth2ProviderType.KAKAO, userInfo.getId());
    // 유저 정보 업데이트
    if (byOauthProviderAndOauthId.isPresent()) {
      byOauthProviderAndOauthId.get().updateExistingUser(userInfo);
      return byOauthProviderAndOauthId.get();
    }

    Optional<Member> byEmail = memberRepository.findByEmail(userInfo.getKakaoAccount().getEmail());

    if (byEmail.isPresent()) {
      byEmail.get().linkKakaoUserInfo(userInfo);
      return byEmail.get();
    }

    return createMember(userInfo);
  }

  /**
   * 회원가입 메서드 MemberService의 회원가입 메서드와 달리 OauthId, OauthProvider로 입력
   * @param userInfo 카카오 사용자 정보
   * @return 멤버 엔티티
   */
  private Member createMember(KakaoUserInfo userInfo) {
    return memberRepository.save(Member.builder().email(userInfo.getKakaoAccount().getEmail())
        .username(userInfo.getKakaoAccount().getProfile().getNickname())
        .oauthId(userInfo.getId())
        .oauthProvider(Oauth2ProviderType.KAKAO).build());
  }
}
