package com.presence.testpresence.services.device;

import java.util.List;

import com.presence.testpresence.model.entities.Records;
import com.presence.testpresence.model.repositories.RecordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RecordsService {
	
	@Autowired
	RecordsRepository recordsRepository;

	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		this.recordsRepository.deleteById(id);
		return id;
	}

	public int insert(Records record) {
		// TODO Auto-generated method stub
		return recordsRepository.save(record).getId();
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
