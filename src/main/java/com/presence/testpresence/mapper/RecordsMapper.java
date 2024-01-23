package com.presence.testpresence.mapper;


import java.util.List;

import com.presence.testpresence.model.entities.Records;

public interface RecordsMapper {
   

    int deleteByPrimaryKey(Integer id);

    int insert(Records record);

    int insertSelective(Records record);

  

    Records selectByPrimaryKey(Integer id);

 

    int updateByPrimaryKeySelective(Records record);

    int updateByPrimaryKey(Records record);
    
    List<Records> selectAllRecords();
}