package com.presence.testpresence.model.repositories;

import com.presence.testpresence.model.entities.Role;
import com.presence.testpresence.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findOneById(Integer id);
    User findOneByEmailAndPassword(String email, String password);
    List<User> findByRolesIn(List<Role> grants);

}
