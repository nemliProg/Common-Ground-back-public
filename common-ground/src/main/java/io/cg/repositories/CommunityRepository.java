package io.cg.repositories;

import io.cg.entities.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CommunityRepository extends JpaRepository<Community, Long> {
}
