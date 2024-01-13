package com.presence.testpresence.model.repositories;

import com.presence.testpresence.model.entities.Appearance;
import com.presence.testpresence.model.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AppearanceRepository extends JpaRepository<Appearance, Integer> {

    Appearance findOneById(Integer id);
    List<Appearance> findByEmployee(Employee employee);
    List<Appearance> findByCreatedBetween(Date one, Date two);

}
