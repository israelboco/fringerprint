package com.presence.testpresence.mapper;


import java.util.List;

import com.presence.testpresence.model.entities.AccessWeek;

public interface AccessWeekMapper {
   
    int deleteByPrimaryKey(Integer id);

    int insert(AccessWeek record);

    int insertSelective(AccessWeek record);

   
    AccessWeek selectByPrimaryKey(Integer id);


    int updateByPrimaryKeySelective(AccessWeek record);

    int updateByPrimaryKey(AccessWeek record);
    
    List<AccessWeek> selectAllAccessWeek();
}