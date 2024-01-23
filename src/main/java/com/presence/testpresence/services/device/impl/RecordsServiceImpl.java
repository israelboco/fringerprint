package com.presence.testpresence.services.device.impl;

import java.util.List;

import com.presence.testpresence.mapper.RecordsMapper;
import com.presence.testpresence.model.entities.Records;
import com.presence.testpresence.services.device.RecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RecordsServiceImpl implements RecordsService {
	
	@Autowired
	RecordsMapper recordsMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return recordsMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(Records record) {
		// TODO Auto-generated method stub
		return recordsMapper.insert(record);
	}

	@Override
	public int insertSelective(Records record) {
		// TODO Auto-generated method stub
		return recordsMapper.insert(record);
	}

	@Override
	public Records selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return recordsMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(Records record) {
		// TODO Auto-generated method stub
		return recordsMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(Records record) {
		// TODO Auto-generated method stub
		return recordsMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<Records> selectAllRecords() {
		// TODO Auto-generated method stub
		return recordsMapper.selectAllRecords();
	}

}
