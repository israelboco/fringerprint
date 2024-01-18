package com.presence.testpresence.model.repositories;

import com.presence.testpresence.model.entities.Employee;
import com.presence.testpresence.model.entities.PresenceEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PresenceEmployeeRepository extends JpaRepository<PresenceEmployee, Integer> {

    PresenceEmployee findOneById(Integer id);
    List<PresenceEmployee> findByEmployee(Employee employee);
    List<PresenceEmployee> findByCreatedBetween(Date one, Date two);

}
