package com.presence.testpresence.model.repositories;

import com.presence.testpresence.model.entities.CompanieType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanieTypeRepository extends JpaRepository<CompanieType, Integer> {

    CompanieType findOneById(Integer id);

}
