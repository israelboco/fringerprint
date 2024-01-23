package com.presence.testpresence.services.device.impl;

import java.util.Date;
import java.util.List;

import com.presence.testpresence.mapper.DeviceMapper;
import com.presence.testpresence.mapper.MachineCommandMapper;
import com.presence.testpresence.model.entities.Device;
import com.presence.testpresence.model.entities.MachineCommand;
import com.presence.testpresence.model.entities.UserLock;
import com.presence.testpresence.services.device.UserLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserLockServiceImpl implements UserLockService {
	
	@Autowired
	DeviceMapper deviceMapper;
	
	@Autowired
	MachineCommandMapper commandMapper;

	@Override
	public void setUserLock(UserLock userLock,
							String starTime, String endTime) {
		StringBuilder sb=new StringBuilder();
		sb.append("{\"cmd\":\"setuserlock\",\"count\":1,\"record\":[{");
		  
		  int weekZone2=2;
		  sb.append("\"enrollid\":" +userLock.getEnrollId()+ ",");
          sb.append("\"weekzone\":" +userLock.getWeekZone()+ ",");
         
          sb.append("\"group\":" +userLock.getGroup()+ ",");
          sb.append("\"starttime\":\""+starTime+"\",");
          sb.append("\"endtime\":\""+endTime+"\"}]}");
		String message=sb.toString();
		List<Device>deviceList=deviceMapper.findAllDevice();
		for (int i = 0; i < deviceList.size(); i++) {
			
			MachineCommand machineCommand=new MachineCommand();
			machineCommand.setContent(message);
			machineCommand.setName("setuserlock");
			machineCommand.setStatus(0);
			machineCommand.setSendStatus(0);
			machineCommand.setErrCount(0);
			machineCommand.setSerial(deviceList.get(i).getSerialNum());
			machineCommand.setGmtCrate(new Date());
			machineCommand.setGmtModified(new Date());
			
		//	machineComandService.addMachineCommand(machineCommand);
			commandMapper.insert(machineCommand);
		}
		
	}
	
}
