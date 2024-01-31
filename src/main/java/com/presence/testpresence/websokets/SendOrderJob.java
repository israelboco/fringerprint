package com.presence.testpresence.websokets;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.presence.testpresence.model.entities.Device;
import com.presence.testpresence.model.repositories.DeviceRepository;
import com.presence.testpresence.model.repositories.MachineCommandRepository;
import com.presence.testpresence.ws.DeviceStatus;
import com.presence.testpresence.model.entities.MachineCommand;
import org.springframework.beans.factory.annotation.Autowired;


public class SendOrderJob extends Thread{
	
    @Autowired
	MachineCommandRepository machineCommandRepository;
    
    @Autowired
	DeviceRepository deviceRepository;
	
	//List<Device>deviceList=deviceService.
	Map<String, DeviceStatus>wdList=WebSocketPool.wsDevice;
	
	private boolean stop=true;
	
	public void startThread() {
		this.stop=false;
		this.start();
	}
	
	public void stopThread() {
		this.stop=true;
		this.stop();
	}
	public void run() {
		
		while (!stop) {
			Iterator<Entry<String, DeviceStatus>>entries=wdList.entrySet().iterator();		
			try {
				
			
			while(entries.hasNext()){		
				Entry<String,DeviceStatus>entry=entries.next();
			//	System.out.println("数据"+entry);
				List<MachineCommand>inSending=machineCommandRepository.findBySendStatusAndSerial(0, entry.getKey());
			//	System.out.println("数据"+inSending);
			    if(inSending.size()>0) {
			    	List<MachineCommand>pendingCommand=machineCommandRepository.findBySendStatusAndSerial(1, entry.getKey());
			    	if (pendingCommand.size()<=0) {
			    		
			    		entry.getValue().getWebSocket().send(inSending.get(0).getContent());
						MachineCommand machineCommand = new MachineCommand();
						machineCommand.setStatus(0);
						machineCommand.setSendStatus(1);
						machineCommand.setRunTime(new Date());
						machineCommand.setId(inSending.get(0).getId());
						machineCommandRepository.save(machineCommand);
					}else if(pendingCommand.size()==1){
						if(System.currentTimeMillis()-(pendingCommand.get(0).getRunTime()).getTime()>20*1000) {
							System.out.println("numeros"+pendingCommand);
							if(pendingCommand.get(0).getErrCount()<3) {
							//	System.out.println("数据"+pendingCommand);
							//entry.getValue().getWebSocket().send(pendingCommand.get(0).getContent());
							MachineCommand machineCommand=pendingCommand.get(0);
							machineCommand.setErrCount(machineCommand.getErrCount()+1);
							machineCommand.setRunTime(new Date());
							machineCommandRepository.save(machineCommand);
					    	Device device=deviceRepository.findBySerialNum(pendingCommand.get(0).getSerial());
							if (device.getStatus()!=0) {
								entry.getValue().getWebSocket().send(pendingCommand.get(0).getContent());
								
							}
							}else {
								MachineCommand machineCommand=pendingCommand.get(0);
								machineCommand.setErrCount(machineCommand.getErrCount()+1);
								machineCommandRepository.save(machineCommand);
							}
						}
					}
			    				    	
			    }
				
			}
			} catch (Exception e) {
				
			}
		}
		
	}

}
