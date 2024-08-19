package com.zb.wiki.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    final String authorizationHeader = request.getHeader("Authorization");

    String username = null;
    String jwt = null;

    if (authorizationHeader != null && authorizationHeader.startsWith("BEARER ")) {
      jwt = authorizationHeader.substring(7);
      username = jwtProvider.getUsernameFromToken(jwt);
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null
        && jwtProvider.isValidToken(jwt)) {
      Authentication authentication = jwtProvider.getAuthentication(jwt);
      SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    filterChain.doFilter(request, response);
  }
}
