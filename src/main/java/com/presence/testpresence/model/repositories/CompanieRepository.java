package com.presence.testpresence.model.repositories;

import com.presence.testpresence.model.entities.Companie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanieRepository extends JpaRepository<Companie, Integer> {

    Companie findOneById(Integer id);

}
