package com.zb.wiki.service;

import com.zb.wiki.dto.KakaoTokenResponse;
import com.zb.wiki.exception.GlobalError;
import com.zb.wiki.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class Oauth2Service {

  private final RestClient restClient;

  @Value("${spring.oauth2.kakao.redirect-uri}")
  private String kakaoRedirect_uri;

  @Value("${spring.oauth2.kakao.client-id}")
  private String kakaoClientId;

  @Value("${spring.oauth2.kakao.grant-type}")
  private String kakaoGrantType;

  @Value("${spring.oauth2.kakao.token-uri}")
  private String kakaoTokenUri;

  public String processAuthorizationCode(String code) {
    String kakaoAccessToken = requestAccessToken(code);
    System.out.println(kakaoAccessToken);

    return null;
  }

  private String requestAccessToken(String code) {
    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
    requestBody.add("grant_type", kakaoGrantType);
    requestBody.add("client_id", kakaoClientId);
    requestBody.add("redirect_uri", kakaoRedirect_uri);
    requestBody.add("code", code);

    try{
      KakaoTokenResponse response = restClient.post()
          .uri(kakaoTokenUri)
          .headers(httpHeaders ->
              httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED)
          )
          .body(requestBody)
          .retrieve()
          .body(KakaoTokenResponse.class);

      if(response == null){
        throw new GlobalException(GlobalError.USER_NOT_FOUND);
      }
      return response.getAccess_token();

    }catch (HttpClientErrorException e){
      log.error(e.getResponseBodyAsString());
      throw new GlobalException(GlobalError.RESOURCE_SERVER_ERROR);

    }


  }
}
