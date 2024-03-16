package com.presence.testpresence.services.device;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.presence.testpresence.model.entities.*;
import com.presence.testpresence.model.repositories.MachineCommandRepository;
import com.presence.testpresence.model.repositories.PersonRepository;
import com.presence.testpresence.websokets.WebSocketPool;
import com.presence.testpresence.ws.DeviceStatus;
import com.presence.testpresence.ws.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

@Component
public class PersonService {
	
	@Autowired
	PersonRepository personRepository;

	@Autowired
	EnrollInfoService enrollInfoService;
	
	@Autowired
	MachineCommandRepository machineCommandRepository;
	
	public int updateByPrimaryKeySelective(Person record) {
		// TODO Auto-generated method stub
		return personRepository.save(record).getId();
	}

	public int updateByPrimaryKey(Person record) {
		// TODO Auto-generated method stub
		return personRepository.save(record).getId();
	}



	public int insertSelective(Person person) {
		// TODO Auto-generated method stub
		return personRepository.save(person).getId();
	}



	public int insert(Person person) {
		// TODO Auto-generated method stub
		return personRepository.save(person).getId();
	}

	public int deleteByPrimaryKey(int id) {
		// TODO Auto-generated method stub
		this.personRepository.deleteById(id);
		return id;
	}

	public Person selectByPrimaryKey(int id) {
		// TODO Auto-generated method stub
		return personRepository.findOneById(id);
	}

	public List<Person> selectAll() {
		// TODO Auto-generated method stub
		return personRepository.findAll();
	}

      public void setUserToDevice(int enrollId,String name,int backupnum,int admin,String records,String deviceSn) {
    	
    	  

			MachineCommand machineCommand=new MachineCommand();
			
			machineCommand.setName("setuserinfo");
			machineCommand.setStatus(0);
			machineCommand.setSendStatus(0);
			machineCommand.setErrCount(0);
			machineCommand.setSerial(deviceSn);
			machineCommand.setGmtCrate(new Date());
			machineCommand.setGmtModified(new Date());
	
    	  machineCommand.setContent("{\"cmd\":\"setuserinfo\",\"enrollid\":"+enrollId+ ",\"name\":\"" + name +"\",\"backupnum\":" + backupnum
					+ ",\"admin\":" + admin + ",\"record\":\"" + records + "\"}");; 
  		
  		if (backupnum==11||backupnum==10) {
  			machineCommand.setContent("{\"cmd\":\"setuserinfo\",\"enrollid\":"+enrollId+ ",\"name\":\"" + name +"\",\"backupnum\":" + backupnum
						+ ",\"admin\":" + admin + ",\"record\":" + records + "}"); 
			}
  		
    	 		
  		machineCommandRepository.save(machineCommand);
	  }
      
      public void setUserToDevice2(String deviceSn) {
    	  List<UserInfo>userInfos=enrollInfoService.usersToSendDevice();
		    
	    	System.out.println(userInfos.size());
	    	for (int i = 0; i < userInfos.size(); i++) {
	    		int enrollId=userInfos.get(i).getEnrollId();
				String name=userInfos.get(i).getName();
				int backupnum=userInfos.get(i).getBackupnum();
				int admin=userInfos.get(i).getAdmin();
				String record=userInfos.get(i).getRecord();						
				SendMessage message=new SendMessage();
	    		MachineCommand machineCommand=new MachineCommand();
	    		
	    		machineCommand.setName("setuserinfo");
	    		machineCommand.setStatus(0);
	    		machineCommand.setSendStatus(0);
	    		machineCommand.setErrCount(0);
	    		machineCommand.setSerial(deviceSn);
	    		machineCommand.setGmtCrate(new Date());
	    		machineCommand.setGmtModified(new Date());
	    		
		  
				machineCommand.setContent("{\"cmd\":\"setuserinfo\",\"enrollid\":"+enrollId+ ",\"name\":\"" + name +"\",\"backupnum\":" + backupnum
						+ ",\"admin\":" + admin + ",\"record\":\"" + record + "\"}"); 
	    	
	    		if (backupnum==11||backupnum==10) {
	    			machineCommand.setContent("{\"cmd\":\"setuserinfo\",\"enrollid\":"+enrollId+ ",\"name\":\"" + name +"\",\"backupnum\":" + backupnum
							+ ",\"admin\":" + admin + ",\"record\":" + record + "}"); 
				}
	    		
	    		machineCommandRepository.save(machineCommand);
	    		
	    		
	    	 }	
			
	}
      
      
       
      public void getSignature(int enrollId,String deviceSn,int backupNum) throws IOException {
    	  try {
	   			Thread.sleep(400);
	   		} catch (InterruptedException e) {
	   			// TODO Auto-generated catch block
	   			e.printStackTrace();
	   		}
    	// List<Person>persons=personMapper.selectAll();
    	 String message="{\"cmd\":\"getuserinfo\",\"enrollid\":"+enrollId+",\"backupnum\":0}";
    	 String message1="{\"cmd\":\"getuserinfo\",\"enrollid\":"+enrollId+",\"backupnum\":"+backupNum+"}";	
    	 DeviceStatus deviceStatus=WebSocketPool.getDeviceStatus(deviceSn);
    	 System.out.println("socket连接"+WebSocketPool.getDeviceSocketBySn(deviceSn));
    //	 WebSocketPool.sendMessageToAll(message);
 		if(deviceStatus.getStatus()==1){
 			//WebSocketPool.sendMessageToAll(message);
 			deviceStatus.setStatus(0);
	 		updateDevice(deviceSn, deviceStatus);          
	 		if (null!=deviceStatus.getWebSocket()) {
				deviceStatus.getWebSocket().sendMessage(new TextMessage(message1));
	 			
			}
 		}else{
 			try {
	   			Thread.sleep(400);
	   		} catch (InterruptedException e) {
	   			// TODO Auto-generated catch block
	   			e.printStackTrace();
	   		}
 			deviceStatus.setStatus(0);
	 		updateDevice(deviceSn, deviceStatus);
	 		if (null!=deviceStatus.getWebSocket()) {
	 			WebSocketPool.sendMessageToDeviceStatus(deviceSn, message);
				//WebSocketPool.sendMessageToAll(message);
			}
 		}
	}
    
    public void getSignature2(List<EnrollInfo>enrolls,String deviceSn) {
		
    	for (int i = 0; i < enrolls.size(); i++) {
			
    		String message1="{\"cmd\":\"getuserinfo\",\"enrollid\":"+enrolls.get(i).getEnrollId()+",\"backupnum\":"+enrolls.get(i).getBackupnum()+"}";	
    	//	String message="{\"cmd\":\"getuserinfo\",\"enrollid\":"+enrollId+",\"backupnum\":"+ backupNum+"}";
    		MachineCommand machineCommand=new MachineCommand();
    		machineCommand.setContent(message1);
    		machineCommand.setName("getuserinfo");
    		machineCommand.setStatus(0);
    		machineCommand.setSendStatus(0);
    		machineCommand.setErrCount(0);
    		machineCommand.setSerial(deviceSn);
    		machineCommand.setGmtCrate(new Date());
    		machineCommand.setGmtModified(new Date());
    		
    		machineCommandRepository.save(machineCommand);
		}
		
		
	    	
	}  
      
      
      
      public void updateDevice(String sn,DeviceStatus deviceStatus) {
    	  if(WebSocketPool.getDeviceStatus(sn)!=null){
  			WebSocketPool.removeDeviceStatus(sn);
  			WebSocketPool.addDeviceAndStatus(sn, deviceStatus);
  		}else{
  			WebSocketPool.addDeviceAndStatus(sn, deviceStatus);
  		}
	}

	public void getSignatureByAll(int enrollId) {
		// TODO Auto-generated method stub
		//List<Person>persons=personMapper.selectAll();
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public void deleteUserInfoFromDevice(Integer enrollId,
			String deviceSn) {
		
		
			int backupnum=13;
			String message="{\"cmd\":\"deleteuser\",\"enrollid\":"+enrollId+",\"backupnum\":"+backupnum+"}";
		
			MachineCommand machineCommand=new MachineCommand();
			machineCommand.setContent(message);
			machineCommand.setName("deleteuser");
			machineCommand.setStatus(0);
			machineCommand.setSendStatus(0);
			machineCommand.setErrCount(0);
			machineCommand.setSerial(deviceSn);
			machineCommand.setGmtCrate(new Date());
			machineCommand.setGmtModified(new Date());
			
			machineCommandRepository.save(machineCommand);
	    	 this.deleteByPrimaryKey(enrollId);
	    	 enrollInfoService.deleteByEnrollId(enrollId);
	    	 	 
	}

	public void setUsernameToDevice(String deviceSn) {
		// TODO Auto-generated method stub
		
		 List<Person>persons = this.selectAll();
		    
	    	System.out.println(persons.size());
	    	int i=0;		    
	    	StringBuilder sb=new StringBuilder();
	    	sb.append("{\"cmd\":\"setusername\",\"count\":"+persons.size()+",\"record\":[");
	    	for (int j = 0; j < persons.size(); j++) {
	    		if(j==persons.size()-1||persons.size()==1){
				sb.append("{\"enrollid\":"+persons.get(j).getId()+",\"name\":\"" + persons.get(j).getName()+"\"}");	
	    		}else{
	    		sb.append("{\"enrollid\":"+persons.get(j).getId()+",\"name\":\"" + persons.get(j).getName()+"\"},");
	    		}
			}
	    	sb.append("]}");
	    	System.out.println("下发用户姓名"+sb);
	    	MachineCommand machineCommand=new MachineCommand();
    		
    		machineCommand.setName("setusername");
    		machineCommand.setStatus(0);
    		machineCommand.setSendStatus(0);
    		machineCommand.setSerial(deviceSn);
    		machineCommand.setGmtCrate(new Date());
    		machineCommand.setGmtModified(new Date());
    		machineCommand.setContent(sb.toString());
	    	machineCommandRepository.save(machineCommand);
	}

}
