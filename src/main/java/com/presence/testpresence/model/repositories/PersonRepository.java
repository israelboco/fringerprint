package com.presence.testpresence.model.repositories;

import com.presence.testpresence.model.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    Person findOneById(Integer id);

}
