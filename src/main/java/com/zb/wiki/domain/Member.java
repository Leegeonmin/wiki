package com.zb.wiki.domain;


import com.zb.wiki.dto.KakaoUserInfo;
import com.zb.wiki.type.Oauth2ProviderType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Member{

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private Long id;
  @NotNull
  @Column(length = 20)
  private String username;

  @Column(length = 20, nullable = true)
  private String password;

  @Column(nullable = true)
  @Enumerated(EnumType.STRING)
  private Oauth2ProviderType oauthProvider;  // "KAKAO", "GOOGLE" 등

  @Column(nullable = true)
  private Long oauthId;  // OAuth 제공자의 고유 ID

  @NotNull
  @Column(length = 30)
  private String email;

  @CreatedDate
  private LocalDateTime createdDateTime;
  @LastModifiedDate
  private LocalDateTime modifiedDateTime;

  public void updateExistingUser(KakaoUserInfo kakaoUserInfo){
    this.username = kakaoUserInfo.getKakaoAccount().getProfile().getNickname();
  }

  public void linkKakaoUserInfo(KakaoUserInfo kakaoUserInfo){
    this.oauthProvider = Oauth2ProviderType.KAKAO;
    this.oauthId = kakaoUserInfo.getId();
  }
}
