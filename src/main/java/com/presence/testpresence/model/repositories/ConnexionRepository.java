package com.presence.testpresence.model.repositories;

import com.presence.testpresence.model.entities.Connexion;
import com.presence.testpresence.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnexionRepository extends JpaRepository<Connexion, Integer> {

    Connexion findOneById(Integer id);
    Connexion findByUserAndActive(User user, Boolean active);
    Connexion findByTokenAndActive(String token, Boolean active);


}
