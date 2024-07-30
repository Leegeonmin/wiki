package com.zb.wiki.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
public class Member {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private Long id;
  @NotNull
  @Column(length = 20)
  private String username;
  @NotNull
  @Column(length = 20)
//  @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
//      message = "비밀번호는 최소 8자 이상이어야 하며, 영문자, 숫자, 특수문자(@$!%*#?&)를 각각 1개 이상 포함해야 합니다.")
  private String password;

  @NotNull
  @Column(length = 30)
//  @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
//      message = "올바른 이메일 형식이 아닙니다.")
  private String email;

  @CreatedDate
  private LocalDateTime createdDate;
  @LastModifiedDate
  private LocalDateTime modifiedDate;
}
