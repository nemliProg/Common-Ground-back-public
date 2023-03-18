package io.cg.repositories;

import io.cg.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

@RepositoryRestResource
public interface MemberRepository extends JpaRepository<Member, Long> {

    @RestResource(exported = false)
    @Query(value = "SELECT m.* FROM member m WHERE m.email LIKE :email AND m.role = 0", nativeQuery = true)
    Optional<Member> findByRegularMemberByEmail(String email);

    @RestResource(exported = false)
    @Query(value = "SELECT m.* FROM member m WHERE m.email LIKE :email AND m.role = 1", nativeQuery = true)
    Optional<Member> findAgentMemberByEmail(String email);

    @RestResource(exported = false)
    Optional<Member> findByEmail(String email);


}
