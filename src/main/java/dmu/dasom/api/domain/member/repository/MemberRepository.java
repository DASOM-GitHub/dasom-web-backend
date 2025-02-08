package dmu.dasom.api.domain.member.repository;

import dmu.dasom.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(final String email);

    boolean existsByEmail(final String email);

}
