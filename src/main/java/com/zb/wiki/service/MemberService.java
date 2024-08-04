package com.zb.wiki.service;

import com.zb.wiki.domain.Member;
import com.zb.wiki.exception.GlobalError;
import com.zb.wiki.exception.GlobalException;
import com.zb.wiki.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberService implements UserDetailsService {

  private final MemberRepository memberRepository;


  /**
   * 회원가입 로직
   * 사용자 아이디로 중복검사를 한 후 사용자 회원가입
   * @param username 사용자 아이디
   * @param password 사용자 비밀번호
   * @param email 이메일
   */
  @Transactional(readOnly = false)
  public void signUp(String username, String password, String email) {
    boolean isMemberExisted = memberRepository.existsByUsername(username);
    if(isMemberExisted) throw new GlobalException(GlobalError.USERNAME_DUPLICATED);

    memberRepository.save(Member.builder()
        .username(username)
        .password(password)
        .email(email)
        .build());
  }

  /**
   * 로그인 로직
   * 사용자의 아이디와 비밀번호로 중복검사
   * @param username 사용자 아이디
   * @param password 사용자 비밀번호
   */
  public void signIn(String username, String password) {
    memberRepository.findByUsernameAndPassword(username,password).orElseThrow(
        () ->  new GlobalException(GlobalError.USER_NOT_FOUND)
    );
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return memberRepository.findByUsername(username)
        .orElseThrow(()->new GlobalException(GlobalError.USER_NOT_FOUND));
  }
}
