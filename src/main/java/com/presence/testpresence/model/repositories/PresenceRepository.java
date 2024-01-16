package com.presence.testpresence.model.repositories;

import com.presence.testpresence.model.entities.Presence;
import com.presence.testpresence.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PresenceRepository extends JpaRepository<Presence, Integer> {

    Presence findOneById(Integer id);
    List<Presence> findByUser(User user);

}
