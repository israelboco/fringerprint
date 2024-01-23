package com.presence.testpresence.mapper;


import java.util.List;

import com.presence.testpresence.model.entities.Person;

public interface PersonMapper {
   
    int deleteByPrimaryKey(int id);

    int insert(Person record);

    int insertSelective(Person record);


    Person selectByPrimaryKey(int id);

   
    int updateByPrimaryKeySelective(Person record);

    int updateByPrimaryKey(Person record);
    
    List<Person> selectAll();
} 