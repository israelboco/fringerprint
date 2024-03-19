package com.presence.testpresence.model.repositories;

import com.presence.testpresence.model.entities.EnrollInfo;
import com.presence.testpresence.model.entities.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollInfoRepository extends JpaRepository<EnrollInfo, Integer> {

    EnrollInfo findOneById(Integer id);
    EnrollInfo findOneByIdAndMachine(Integer id, Machine machine);

    EnrollInfo findByEnrollIdAndBackupnum(Integer enrollId, Integer backupnum);

    List<EnrollInfo> findByEnrollId(Integer enrollId);

    void deleteByEnrollId(Integer enrollId);

}
