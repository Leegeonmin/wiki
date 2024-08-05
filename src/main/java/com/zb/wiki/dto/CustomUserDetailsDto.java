package com.zb.wiki.dto;

import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Builder
@AllArgsConstructor
public class CustomUserDetailsDto implements UserDetails {

  private String username;
  private String password;
  private String email;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

}
