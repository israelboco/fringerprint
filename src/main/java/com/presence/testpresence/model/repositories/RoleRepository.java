package com.presence.testpresence.model.repositories;

import com.presence.testpresence.model.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findOneById(Integer id);

}
