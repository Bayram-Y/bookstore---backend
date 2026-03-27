package com.eazybook.marcus.repository;

import com.eazybook.marcus.entity.Role;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Cacheable("roles")
        // ROLE_USER -> CACHE MISS -> DB call -> Cache Store (ROLE_USER -> Role Record) -> Customer 1
        // ROLE_USER -> CACHE HIT  -> Customer 2
        // ROLE_ADMIN -> CACHE MISS -> DB call -> Cache Store (ROLE_ADMIN -> Role Record) -> Customer X

    Optional<Role> findByName(String name);

}