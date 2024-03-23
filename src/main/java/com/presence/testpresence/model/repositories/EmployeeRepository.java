package com.presence.testpresence.model.repositories;

import com.presence.testpresence.model.entities.Companie;
import com.presence.testpresence.model.entities.Employee;
import com.presence.testpresence.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Employee findOneById(Integer id);
    List<Employee> findByCompanie(Companie companie);
    Page<Employee> findByCompanie(Companie companie, Pageable pageable);
    Employee findByUser(User user);
    Employee findByUserAndCompanie(User user, Companie companie);


}
