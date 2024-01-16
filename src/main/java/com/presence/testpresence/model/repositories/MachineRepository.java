package com.presence.testpresence.model.repositories;

import com.presence.testpresence.model.entities.Companie;
import com.presence.testpresence.model.entities.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Integer> {

    Machine findOneById(Integer id);

    List<Machine> findByActive(Boolean active);
    List<Machine> findByCompanieAndActive(Companie companie, Boolean active);
    List<Machine> findByCompanie(Companie companie);

}
