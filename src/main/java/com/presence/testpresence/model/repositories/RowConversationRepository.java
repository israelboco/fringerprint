package com.presence.testpresence.model.repositories;

import com.presence.testpresence.model.entities.Employee;
import com.presence.testpresence.model.entities.RowConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RowConversationRepository extends JpaRepository<RowConversation, Integer> {

    RowConversation findOneById(Integer id);
    List<RowConversation> findByCreateByOrCreateToOrderByCreatedDesc(Employee createBy, Employee createTo);
    RowConversation findByCreateByBetweenAndCreateToBetween(Employee createBy, Employee createTo, Employee createTo2, Employee createBy2);
}
