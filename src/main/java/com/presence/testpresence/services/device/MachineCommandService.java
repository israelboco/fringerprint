package com.presence.testpresence.services.device;

import com.presence.testpresence.model.entities.MachineCommand;


public interface MachineCommandService {
	
	public void addMachineCommand(MachineCommand machineCommand);
	
//	public void addGetUserCommand()
	
	public void addGetOneUserCommand(int enrollId,int backupNum,String serialNum);

}
