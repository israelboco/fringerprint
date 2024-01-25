package com.presence.testpresence.model.repositories;

import com.presence.testpresence.model.entities.AccessDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessDayRepository extends JpaRepository<AccessDay, Integer> {

    AccessDay findOneById(Integer id);

}
