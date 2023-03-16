package io.cg.repositories;

import io.cg.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

@RepositoryRestResource
public interface MemberRepository extends JpaRepository<Member, Long> {

    @RestResource(exported = false)
    Optional<Member> findByEmail(String email);


}
