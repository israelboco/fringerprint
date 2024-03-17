package com.presence.testpresence.job;

import java.util.*;
import java.util.Map.Entry;

import com.presence.testpresence.model.entities.Device;
import com.presence.testpresence.model.repositories.DeviceRepository;
import com.presence.testpresence.model.repositories.MachineCommandRepository;
import com.presence.testpresence.ws.DeviceStatus;
import com.presence.testpresence.model.entities.MachineCommand;
import com.presence.testpresence.websokets.WebSocketPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;


@Component
public class SendOrderJob extends Thread {

	private static Logger logger = LogManager.getLogger(SendOrderJob.class);

    @Autowired
	MachineCommandRepository machineCommandRepository;
    
    @Autowired
	DeviceRepository deviceRepository;
	
	//List<Device>deviceList=deviceService.
	Map<String, DeviceStatus> wdList = WebSocketPool.wsDevice;
	//List<WebSocketSession> wdList
	//		= Collections.synchronizedList(new ArrayList<>());

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
			    		
			    		entry.getValue().getWebSocket().sendMessage(new TextMessage(inSending.get(0).getContent()));
						MachineCommand machineCommand = new MachineCommand();
						machineCommand.setStatus(0);
						machineCommand.setSendStatus(1);
						machineCommand.setRunTime(new Date());
						machineCommand.setId(inSending.get(0).getId());
			    		machineCommandRepository.save(machineCommand);
					}else if(pendingCommand.size()==1){
						if(System.currentTimeMillis()-(pendingCommand.get(0).getRunTime()).getTime()>20*1000) {
							logger.debug("pending command : "+pendingCommand);
							if(pendingCommand.get(0).getErrCount()<3) {
							//	System.out.println("数据"+pendingCommand);
							//entry.getValue().getWebSocket().send(pendingCommand.get(0).getContent());
							MachineCommand machineCommand=pendingCommand.get(0);
							machineCommand.setErrCount(machineCommand.getErrCount()+1);
							machineCommand.setRunTime(new Date());
							machineCommandRepository.save(machineCommand);
					    	Device device=deviceRepository.findBySerialNum(pendingCommand.get(0).getSerial());
							if (device.getStatus()!=0) {
								entry.getValue().getWebSocket().sendMessage(new TextMessage(pendingCommand.get(0).getContent()));
								
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
