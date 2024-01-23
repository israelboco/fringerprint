package com.presence.testpresence.services.device;

import java.util.List;

import com.presence.testpresence.model.entities.Device;
import org.springframework.data.repository.query.Param;


public interface DeviceService {


    List<Device> findAllDevice();
	
    int deleteByPrimaryKey(Integer id);

    int insert(@Param("serialNum")String serialNum, @Param("status")int status);

    int insertSelective(Device record);

    Device selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Device record);

    int updateByPrimaryKey(Device record);
    
    int updateStatusByPrimaryKey(int id,int status);
    
    Device selectDeviceBySerialNum(String serialNum);
    
    List<Device> selectAllDevice();
}
