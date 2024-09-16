package com.kadod.fingerprint.services.device;

import java.util.Date;
import java.util.List;

import com.kadod.commons.ws.UserLock;
import com.kadod.database.model.entities.Device;
import com.kadod.database.model.entities.MachineCommand;
import com.kadod.database.model.repositories.DeviceRepository;
import com.kadod.database.model.repositories.MachineCommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UserLockService {
	
	@Autowired
	DeviceRepository deviceRepository;
	
	@Autowired
	MachineCommandRepository machineCommandRepository;

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
		List<Device>deviceList = deviceRepository.findAll();
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
			machineCommandRepository.save(machineCommand);
		}
		
	}
	
}
