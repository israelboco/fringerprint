package com.presence.testpresence.services.device;

import java.util.List;

import com.presence.testpresence.model.entities.Device;
import com.presence.testpresence.model.repositories.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DeviceService {
  
	@Autowired
	DeviceRepository deviceRepository;
	 
	public List<Device> findAllDevice() {
		// TODO Auto-generated method stub
		List<Device>deviceLists = deviceRepository.findAll();
		return deviceLists;
	}

	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		this.deviceRepository.deleteById(id);
		
		return id;
	}

	public int insert(String serialNum, int status) {
		// TODO Auto-generated method stub
		Device device = new Device();
		device.setStatus(status);
		device.setSerialNum(serialNum);
		device = deviceRepository.save(device);
		return device.getId();
	}

	public int insertSelective(Device record) {
		// TODO Auto-generated method stub
		
		return 0;
	}

	public Device selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		Device device = deviceRepository.findOneById(id);
		return device;
	}

	public int updateByPrimaryKeySelective(Device record) {
		// TODO Auto-generated method stub
		
		return 0;
	}

	public int updateByPrimaryKey(Device record) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Device selectDeviceBySerialNum(String serialNum) {
		// TODO Auto-generated method stub
        return deviceRepository.findBySerialNum(serialNum);
	}

	public int updateStatusByPrimaryKey(int id, int status) {
		// TODO Auto-generated method stub
		Device device = deviceRepository.findOneById(id);
		if(device == null) return 0;
		device.setStatus(status);
		return deviceRepository.save(device).getId();
	}

	public List<Device> selectAllDevice() {
		// TODO Auto-generated method stub
		return deviceRepository.findAll();
	}
   
	
}
