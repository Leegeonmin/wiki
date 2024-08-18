package com.zb.wiki.custom;

import com.zb.wiki.dto.CustomUserDetailsDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithCustomMockUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {

  @Override
  public SecurityContext createSecurityContext(WithCustomMockUser annotation) {
    Long id = annotation.id();
    String username = annotation.username();
    String password = annotation.password();
    String email = annotation.email();

    //여기서 바인딩되어 반환할 객체를 정의해주면 됩니다
    CustomUserDetailsDto user = CustomUserDetailsDto.builder()
        .id(id)
        .username(username)
        .password(password)
        .email(email)
        .build();

    UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(user, "password");
    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(token);
    return context;
  }
}
