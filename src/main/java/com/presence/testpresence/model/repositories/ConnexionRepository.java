package com.presence.testpresence.model.repositories;

import com.presence.testpresence.model.entities.Connexion;
import com.presence.testpresence.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnexionRepository extends JpaRepository<Connexion, Integer> {

    Connexion findOneById(Integer id);
    Connexion findByUserAndActive(User user, Boolean active);
    Connexion findByUser(User user);
    Connexion findByTokenAndActive(String token, Boolean active);
    Page<Connexion> findByCompanyAndConfirmDemandeIsNull(String company, Pageable pageable);
    Page<Connexion> findByConfirmDemande(Boolean confirm, Pageable pageable);

}
