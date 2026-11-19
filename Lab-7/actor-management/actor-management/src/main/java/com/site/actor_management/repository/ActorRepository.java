package com.site.actor_management.repository;

import com.site.actor_management.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    // Spring Data JPA provides all standard CRUD operations out of the box
}
