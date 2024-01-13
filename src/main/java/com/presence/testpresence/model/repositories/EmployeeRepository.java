package com.presence.testpresence.model.repositories;

import com.presence.testpresence.model.entities.Companie;
import com.presence.testpresence.model.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Employee findOneById(Employee employees);
    List<Employee> findByCompanie(Companie companie);

}
