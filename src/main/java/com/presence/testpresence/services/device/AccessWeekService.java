package com.presence.testpresence.services.device;

import com.presence.testpresence.model.entities.AccessWeek;

import java.util.List;


public interface AccessWeekService {


    int deleteByPrimaryKey(Integer id);

    int insert(AccessWeek record);

    int insertSelective(AccessWeek record);
    AccessWeek selectByPrimaryKey(Integer id);
   int updateByPrimaryKeySelective(AccessWeek record);

    int updateByPrimaryKey(AccessWeek record);
    
    List<AccessWeek> selectAllAccessWeek();
    
    public void setAccessWeek();
}
