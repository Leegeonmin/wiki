package com.zb.wiki.service;

import com.zb.wiki.domain.Member;
import com.zb.wiki.dto.CustomUserDetailsDto;
import com.zb.wiki.exception.GlobalError;
import com.zb.wiki.exception.GlobalException;
import com.zb.wiki.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Member member = memberRepository.findByUsername(username)
        .orElseThrow(() -> new GlobalException(GlobalError.USER_NOT_FOUND));
    return CustomUserDetailsDto.builder()
        .username(member.getUsername())
        .password(member.getPassword())
        .email(member.getEmail())
        .build();
  }
}
