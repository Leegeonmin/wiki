package com.zb.wiki.service;

import com.zb.wiki.domain.Member;
import com.zb.wiki.exception.GlobalError;
import com.zb.wiki.exception.GlobalException;
import com.zb.wiki.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberService {

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
}