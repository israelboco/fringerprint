package com.kadod.fingerprint.services.device;

import java.util.Date;

import com.google.gson.Gson;
import com.kadod.commons.ws.MachineCommandWs;
import com.kadod.database.model.entities.MachineCommand;
import com.kadod.database.model.repositories.MachineCommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MachineCommandService {

	@Autowired
	MachineCommandRepository machineCommandRepository;
	
	
	public void addMachineCommand(MachineCommandWs ws) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		MachineCommand machineCommand = gson.fromJson(gson.toJson(ws), MachineCommand.class);
		machineCommandRepository.save(machineCommand);
	}


	public void addGetOneUserCommand(int enrollId, int backupNum, String serialNum) {
		// TODO Auto-generated method stub
		String message="{\"cmd\":\"getuserinfo\",\"enrollid\":"+enrollId+",\"backupnum\":"+ backupNum+"}";
		MachineCommand machineCommand=new MachineCommand();
		machineCommand.setContent(message);
		machineCommand.setName("getuserinfo");
		machineCommand.setStatus(0);
		machineCommand.setSendStatus(0);
		machineCommand.setSerial(serialNum);
		machineCommand.setGmtCrate(new Date());
		machineCommand.setGmtModified(new Date());
		
		machineCommandRepository.save(machineCommand);
	}

}
