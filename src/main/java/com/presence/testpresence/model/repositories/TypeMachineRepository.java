package com.presence.testpresence.model.repositories;


import com.presence.testpresence.model.entities.TypeMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeMachineRepository extends JpaRepository<TypeMachine, Integer> {

    TypeMachine findOneById(Integer id);

}
