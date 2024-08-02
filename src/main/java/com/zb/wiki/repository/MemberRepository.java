package com.zb.wiki.repository;

import com.zb.wiki.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
  boolean existsByUsername(String username);
  Optional<Member> findByUsernameAndPassword(String username, String password);
}
