package com.presence.testpresence.model.repositories;

import com.presence.testpresence.model.entities.Companie;
import com.presence.testpresence.model.entities.Employee;
import com.presence.testpresence.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Employee findOneById(Integer id);
    List<Employee> findByCompanie(Companie companie);
    Employee findByUser(User user);


}
