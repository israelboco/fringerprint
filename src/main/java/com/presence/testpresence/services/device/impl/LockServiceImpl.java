package com.presence.testpresence.services.device.impl;

import java.util.Date;
import java.util.List;

import com.presence.testpresence.mapper.DeviceMapper;
import com.presence.testpresence.mapper.MachineCommandMapper;
import com.presence.testpresence.model.entities.Device;
import com.presence.testpresence.model.entities.LockGroup;
import com.presence.testpresence.model.entities.MachineCommand;
import com.presence.testpresence.services.device.LockGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LockServiceImpl implements LockGroupService {
	
	@Autowired
	MachineCommandMapper machineCommandMapper;
	
	@Autowired
	DeviceMapper deviceMapper;

	@Override
	public void setLockGroup(LockGroup lockGroup) {
		// TODO Auto-generated method stub
		StringBuilder sb=new StringBuilder();
		 sb.append("{\"cmd\":\"setdevlock\",\"lockgroup\":[");
		
		 
         sb.append("{\"group\":" + lockGroup.getGroup1() + "},");
         sb.append("{\"group\":" + lockGroup.getGroup2() + "},");
         sb.append("{\"group\":" + lockGroup.getGroup3() + "},");
         sb.append("{\"group\":" + lockGroup.getGroup4()+ "},");
         sb.append("{\"group\":" + lockGroup.getGroup5()+ "}]}");
         
         String message=sb.toString();
         List<Device>deviceList=deviceMapper.findAllDevice();
 		for (int i = 0; i < deviceList.size(); i++) {
 			
 			MachineCommand machineCommand=new MachineCommand();
 			machineCommand.setContent(message);
 			machineCommand.setName("setdevlock");
 			machineCommand.setStatus(0);
 			machineCommand.setSendStatus(0);
 			machineCommand.setErrCount(0);
 			machineCommand.setSerial(deviceList.get(i).getSerialNum());
 			machineCommand.setGmtCrate(new Date());
 			machineCommand.setGmtModified(new Date());
 			
 		//	machineComandService.addMachineCommand(machineCommand);
 			machineCommandMapper.insert(machineCommand);
 		}
		
	}

}
