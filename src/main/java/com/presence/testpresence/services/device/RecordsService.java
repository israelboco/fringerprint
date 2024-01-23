package com.presence.testpresence.services.device;

import java.util.List;

import com.presence.testpresence.model.entities.Records;

public interface RecordsService {
	 int deleteByPrimaryKey(Integer id);

	    int insert(Records record);

	    int insertSelective(Records record);

	  

	    Records selectByPrimaryKey(Integer id);

	 

	    int updateByPrimaryKeySelective(Records record);

	    int updateByPrimaryKey(Records record);
	    
	    List<Records> selectAllRecords();
}
