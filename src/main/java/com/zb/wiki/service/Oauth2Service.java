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


  @Transactional(readOnly = false)
  public String loginOrRegisterKakaoUser(String code) {
    String kakaoAccessToken = requestAccessToken(code);
    KakaoUserInfo userInfo = getUserInfo(kakaoAccessToken);

    return userInfo.getKakaoAccount().getProfile().getNickname();
  }


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

  private Member createMember(KakaoUserInfo userInfo) {
    return memberRepository.save(Member.builder().email(userInfo.getKakaoAccount().getEmail())
        .username(userInfo.getKakaoAccount().getProfile().getNickname()).oauthId(userInfo.getId())
        .oauthProvider(Oauth2ProviderType.KAKAO).build());
  }
}
