package com.presence.testpresence.model.repositories;

import com.presence.testpresence.model.entities.Option;
import com.presence.testpresence.model.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Integer> {

    Option findOneById(Integer id);
    List<Option> findBySubscription(Subscription subscription);

}
