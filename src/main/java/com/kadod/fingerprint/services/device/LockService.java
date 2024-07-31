package com.kadod.fingerprint.services.device;

import java.util.Date;
import java.util.List;

import com.kadod.commons.ws.LockGroup;
import com.kadod.database.model.entities.Device;
import com.kadod.database.model.entities.MachineCommand;
import com.kadod.database.model.repositories.DeviceRepository;
import com.kadod.database.model.repositories.MachineCommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LockService {
	
	@Autowired
	MachineCommandRepository machineCommandRepository;
	
	@Autowired
	DeviceRepository deviceRepository;

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
         List<Device> deviceList = deviceRepository.findAll();
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
 			machineCommandRepository.save(machineCommand);
 		}
		
	}

}
