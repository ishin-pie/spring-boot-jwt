package com.ishinvin.backend.springboot.jwt.repository;


import com.ishinvin.backend.springboot.jwt.domain.Role;
import com.ishinvin.backend.springboot.jwt.domain.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(Roles name);

}
