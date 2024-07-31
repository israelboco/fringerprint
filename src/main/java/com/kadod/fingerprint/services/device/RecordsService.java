package com.kadod.fingerprint.services.device;

import java.util.List;

import com.kadod.database.model.entities.Records;
import com.kadod.database.model.repositories.RecordsRepository;
import com.kadod.fingerprint.services.PresenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RecordsService {
	
	@Autowired
	RecordsRepository recordsRepository;
	@Autowired
	PresenceService presenceService;

	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		this.recordsRepository.deleteById(id);
		return id;
	}

	public int insert(Records record) {
		// TODO Auto-generated method stub
		Records recordSave = recordsRepository.save(record);
		this.presenceService.create(recordSave);
		return recordSave.getId();
	}

	public int insertSelective(Records record) {
		// TODO Auto-generated method stub
		return recordsRepository.save(record).getId();
	}

	public Records selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return recordsRepository.findOneById(id);
	}

	public int updateByPrimaryKeySelective(Records record) {
		// TODO Auto-generated method stub
		return recordsRepository.save(record).getId();
	}

	public int updateByPrimaryKey(Records record) {
		// TODO Auto-generated method stub
		return recordsRepository.save(record).getId();
	}

	public List<Records> selectAllRecords() {
		// TODO Auto-generated method stub
		return recordsRepository.findAll();
	}

}
