package com.presence.testpresence.services.device.impl;

import java.util.Date;

import com.presence.testpresence.mapper.MachineCommandMapper;
import com.presence.testpresence.model.entities.MachineCommand;
import com.presence.testpresence.services.device.MachineCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class MachineCommandServiceImpl implements MachineCommandService {

	@Autowired
	MachineCommandMapper machineCommandMapper;
	
	
	@Override
	public void addMachineCommand(MachineCommand machineCommand) {
		// TODO Auto-generated method stub
		machineCommandMapper.insert(machineCommand);
	}


	@Override
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
		
		machineCommandMapper.insert(machineCommand);
	}

}
