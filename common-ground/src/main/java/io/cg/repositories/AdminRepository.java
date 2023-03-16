package io.cg.repositories;

import io.cg.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;


@RepositoryRestResource
public interface AdminRepository extends JpaRepository<Admin, Long> {

    @RestResource(exported = false)
    Optional<Admin> findByEmail(String email);


}
