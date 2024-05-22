package com.presence.testpresence.model.repositories;

import com.presence.testpresence.model.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

    Subscription findOneById(Integer id);

}
