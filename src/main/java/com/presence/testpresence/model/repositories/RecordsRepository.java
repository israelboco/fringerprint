package com.presence.testpresence.model.repositories;

import com.presence.testpresence.model.entities.Records;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordsRepository extends JpaRepository<Records, Integer> {
    Records findOneById(Integer id);

}
