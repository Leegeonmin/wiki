package com.zb.wiki.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserInfo {

  private Long id;

  @JsonProperty("connected_at")
  private ZonedDateTime connectedAt;

  private Properties properties;

  @JsonProperty("kakao_account")

  private KakaoAccount kakaoAccount;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Properties {

    private String nickname;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class KakaoAccount {

    @JsonProperty("profile_nickname_needs_agreement")
    private boolean profileNicknameNeedsAgreement;
    @JsonProperty("profile_image_needs_agreement")
    private boolean profileImageNeedsAgreement;
    private Profile profile;
    @JsonProperty("has_email")
    private boolean hasEmail;
    @JsonProperty("email_needs_agreement")
    private boolean emailNeedsAgreement;
    @JsonProperty("is_email_valid")
    private boolean isEmailValid;
    @JsonProperty("is_email_verified")
    private boolean isEmailVerified;
    private String email;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Profile {
    private String nickname;
    private boolean isDefaultNickname;
  }
}
