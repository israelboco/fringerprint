package com.presence.testpresence.services.device;

import com.presence.testpresence.model.entities.AccessDay;

import java.util.List;


public interface AccessDayService {
	int deleteByPrimaryKey(Integer id);

    int insert(AccessDay record);

    int insertSelective(AccessDay record);

   

    AccessDay selectByPrimaryKey(Integer id);

   

    int updateByPrimaryKeySelective(AccessDay record);

    int updateByPrimaryKey(AccessDay record);
    
 
    List<AccessDay> selectAll();

	void setAccessDay();
}
