package com.zb.wiki.repository;

import com.zb.wiki.domain.Member;
import com.zb.wiki.type.Oauth2ProviderType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
  Optional<Member> findByUsername(String username);
  boolean existsByUsername(String username);
  Optional<Member> findByUsernameAndPassword(String username, String password);
  Optional<Member> findByOauthProviderAndOauthId(Oauth2ProviderType provider, Long oauthId);

  Optional<Member> findByEmail(String email);
}
