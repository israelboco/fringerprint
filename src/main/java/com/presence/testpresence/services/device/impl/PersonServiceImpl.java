package com.presence.testpresence.services.device.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.presence.testpresence.mapper.MachineCommandMapper;
import com.presence.testpresence.mapper.PersonMapper;
import com.presence.testpresence.model.entities.*;
import com.presence.testpresence.services.device.EnrollInfoService;
import com.presence.testpresence.services.device.PersonService;
import com.presence.testpresence.websokets.WebSocketPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {
	
	@Autowired
	PersonMapper personMapper;

	@Autowired
	EnrollInfoService enrollInfoService;
	
	@Autowired
	MachineCommandMapper machineCommandMapper;
	
	@Override
	public int updateByPrimaryKeySelective(Person record) {
		// TODO Auto-generated method stub
		return personMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(Person record) {
		// TODO Auto-generated method stub
		return personMapper.updateByPrimaryKey(record);
	}



	


	@Override
	public int insertSelective(Person person) {
		// TODO Auto-generated method stub
		return personMapper.insertSelective(person);
	}



	@Override
	public int insert(Person person) {
		// TODO Auto-generated method stub
		return personMapper.insert(person);
	}

	@Override
	public int deleteByPrimaryKey(int id) {
		// TODO Auto-generated method stub
		return personMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Person selectByPrimaryKey(int id) {
		// TODO Auto-generated method stub
		return personMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Person> selectAll() {
		// TODO Auto-generated method stub
		return personMapper.selectAll();
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
  		
    	 		
  		machineCommandMapper.insert(machineCommand);
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
	    		
	    		machineCommandMapper.insert(machineCommand);
	    		
	    		
	    	 }	
			
	}
      
      
       
      public void getSignature(int enrollId,String deviceSn,int backupNum) {
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
				deviceStatus.getWebSocket().send(message1);
	 			
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
    		
    		machineCommandMapper.insert(machineCommand);
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

	@Override
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

	@Override
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
			
			machineCommandMapper.insert(machineCommand);
	    	 personMapper.deleteByPrimaryKey(enrollId);
	    	 enrollInfoService.deleteByEnrollId(enrollId);
	    	 	 
	}

	@Override
	public void setUsernameToDevice(String deviceSn) {
		// TODO Auto-generated method stub
		
		 List<Person>persons=personMapper.selectAll();
		    
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
	    	machineCommandMapper.insert(machineCommand);
	}
}
