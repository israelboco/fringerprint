package com.presence.testpresence.websokets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.presence.testpresence.model.entities.*;
import com.presence.testpresence.model.repositories.MachineCommandRepository;
import com.presence.testpresence.services.device.*;
import com.presence.testpresence.util.ImageProcess;
import com.presence.testpresence.ws.DeviceStatus;
import com.presence.testpresence.ws.UserTemp;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class WSServer extends WebSocketServer{


	@Autowired
	DeviceService deviceService;
	
	@Autowired
	RecordsService recordsService;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	EnrollInfoService enrollInfoService;
	
	@Autowired
	UserLockService userLockService;
	
	@Autowired
	MachineCommandRepository machineCommandRepository;
	
	int j=0;
    int h=0;
    int e=0;
   static int l;
   Long timeStamp=0L;
   Long timeStamp2=0L;

   //private static int TCP_PORT = 9000;

   public static boolean setUserResult;
   public static Logger logger = LoggerFactory.getLogger(WSServer.class);
  
	  
	  public WSServer(InetSocketAddress address) {
	        super(address);
	        logger.info("adresse ws" + address);
	    }

	    public WSServer(int TCP_PORT) throws UnknownHostException {
	        super(new InetSocketAddress(TCP_PORT));
	        logger.info("port ws" + TCP_PORT);
	    }
	  
	  
	@Override
	public void onOpen(WebSocket conn,
			ClientHandshake handshake) {
		// TODO Auto-generated method stub
	//	deviceService=(DeviceService)ContextLoader.getCurrentWebApplicationContext().getBean(DeviceService.class);
		  logger.info("Quelqu'un se connecte à la prise :" + conn);
	      //  l++;
		 logger.info("New connection from :" + conn.getRemoteSocketAddress());
		 //logger.info("New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
		 l++;
		
	}

	
	
	@Override
	public void onClose(WebSocket conn, int code,
			String reason, boolean remote) {
		// TODO Auto-generated method stub
		    String sn=WebSocketPool.removeDeviceByWebsocket(conn);
			//deviceService.updateStatusByPrimarykey(id, status)
			Device d1=deviceService.selectDeviceBySerialNum(sn);
			deviceService.updateStatusByPrimaryKey(d1.getId(), 0);
		  logger.info("onClose connection from :" + conn.getRemoteSocketAddress());
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		
	        ObjectMapper objectMapper = new ObjectMapper();
	        String ret;
	       
	            try {
	            	//Thread.sleep(7000);
	    	        String msg = message.replaceAll(",]", "]");
	    	        
					JsonNode jsonNode = (JsonNode)objectMapper.readValue(msg, JsonNode.class);
				//	System.out.println("数据"+jsonNode);
					if (jsonNode.has("cmd")) {
						ret=jsonNode.get("cmd").asText();
						if ("reg".equals(ret)) {
							logger.info("Informations sur l'équipement "+jsonNode);
							try {
								
							this.getDeviceInfo(jsonNode, conn);
								
								
							} catch (Exception e) {
								// TODO Auto-generated catch block
								conn.send("{\"ret\":\"reg\",\"result\":false,\"reason\":1}");
								e.printStackTrace();
							}
						}else if("sendlog".equals(ret)){
							SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
							try {
								this.getAttandence(jsonNode,conn);
								
							} catch (Exception e) {
								// TODO Auto-generated catch block
								conn.send("{\"ret\":\"sendlog\",\"result\":false,\"reason\":1}");
								e.printStackTrace();
							}
						}else if("senduser".endsWith(ret)){
							System.out.println(jsonNode);
						
							
							try {
								this.getEnrollInfo(jsonNode,conn);
							
							} catch (Exception e) {
								// TODO Auto-generated catch block
								conn.send("{\"ret\":\"senduser\",\"result\":false,\"reason\":1}");
								e.printStackTrace();
							}
						}
						
					}else if(jsonNode.has("ret")){
						ret=jsonNode.get("ret").asText();
						//boolean result;
						if("getuserlist".equals(ret)){
						//	System.out.println(jsonNode);
						//	System.out.println("数据"+message);
							this.getUserList(jsonNode,conn);
							
						}else if("getuserinfo".equals(ret)){
							//System.out.println("json数据"+jsonNode);
							this.getUserInfo(jsonNode,conn);
							String sn=jsonNode.get("sn").asText();
							DeviceStatus deviceStatus=new DeviceStatus();
							deviceStatus.setWebSocket(conn);
						    deviceStatus.setDeviceSn(sn);
						    deviceStatus.setStatus(1);
						    updateDevice(sn, deviceStatus);							
						}else if("setuserinfo".equals(ret)){
							Boolean result=jsonNode.get("result").asBoolean();
							//WebSocketPool.setUserResult(result);
							//setUserResult=result;
						//	System.out.println();
							String sn=jsonNode.get("sn").asText();
							DeviceStatus deviceStatus=new DeviceStatus();
							deviceStatus.setWebSocket(conn);
						    deviceStatus.setDeviceSn(sn);
						    deviceStatus.setStatus(1);
						    updateDevice(sn, deviceStatus);
						    updateCommandStatus(sn, "setuserinfo");
							logger.info("Diffusion des données: "+jsonNode);
						}else if("getalllog".equals(ret)){

							logger.info("Obtenir tous les enregistrements de poinçons: "+jsonNode);
							try {
								this.getAllLog(jsonNode,conn);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								
							}
							
						}else if("getnewlog".equals(ret)){

							logger.info("Obtenir tous les enregistrements de poinçons: "+jsonNode);

							try {
								this.getnewLog(jsonNode,conn);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								
							}
							
						}
						else if("deleteuser".equals(ret)){
							String sn=jsonNode.get("sn").asText();
							DeviceStatus deviceStatus=new DeviceStatus();
							deviceStatus.setWebSocket(conn);
						    deviceStatus.setDeviceSn(sn);
						    deviceStatus.setStatus(1);
						    updateDevice(sn, deviceStatus);
							logger.info("Suppression de personnel"+jsonNode);
							updateCommandStatus(sn, "deleteuser");
						}else if("initsys".equals(ret)){
							String sn=jsonNode.get("sn").asText();
							DeviceStatus deviceStatus=new DeviceStatus();
							deviceStatus.setWebSocket(conn);
						    deviceStatus.setDeviceSn(sn);
						    deviceStatus.setStatus(1);
						    updateDevice(sn, deviceStatus);
							logger.info("Initialisation du système: "+jsonNode);
							updateCommandStatus(sn, "initsys");
						}else if("setdevlock".equals(ret)){
							String sn=jsonNode.get("sn").asText();
							DeviceStatus deviceStatus=new DeviceStatus();
							deviceStatus.setWebSocket(conn);
						    deviceStatus.setDeviceSn(sn);
						    deviceStatus.setStatus(1);
						    updateDevice(sn, deviceStatus);
							logger.info("Réglage de la période de jour: "+jsonNode);
							updateCommandStatus(sn, "setdevlock");
						}else if("setuserlock".equals(ret)){
							String sn=jsonNode.get("sn").asText();
							DeviceStatus deviceStatus=new DeviceStatus();
							deviceStatus.setWebSocket(conn);
						    deviceStatus.setDeviceSn(sn);
						    deviceStatus.setStatus(1);
						    updateDevice(sn, deviceStatus);
							logger.info("Autorisation d'accès: "+jsonNode);
							updateCommandStatus(sn, "setuserlock");
						}else if("getdevinfo".equals(ret)){
							String sn=jsonNode.get("sn").asText();
							DeviceStatus deviceStatus=new DeviceStatus();
							deviceStatus.setWebSocket(conn);
						    deviceStatus.setDeviceSn(sn);
						    deviceStatus.setStatus(1);
						    updateDevice(sn, deviceStatus);
							logger.info(new Date()+ " Informations sur l'équipement: " +jsonNode);
							updateCommandStatus(sn, "getdevinfo");
						}else if("setusername".equals(ret)){
							String sn=jsonNode.get("sn").asText();
							DeviceStatus deviceStatus=new DeviceStatus();
							deviceStatus.setWebSocket(conn);
						    deviceStatus.setDeviceSn(sn);
						    deviceStatus.setStatus(1);
						    updateDevice(sn, deviceStatus);
							logger.info(new Date()+ " Nom à délivrer " +jsonNode);
							updateCommandStatus(sn, "setusername");
						}else if("reboot".equals(ret)){
							String sn=jsonNode.get("sn").asText();
							DeviceStatus deviceStatus=new DeviceStatus();
							deviceStatus.setWebSocket(conn);
						    deviceStatus.setDeviceSn(sn);
						    deviceStatus.setStatus(1);
						    updateDevice(sn, deviceStatus);
						    updateCommandStatus(sn, "reboot");
						}else {
							String sn=jsonNode.get("sn").asText();
							DeviceStatus deviceStatus=new DeviceStatus();
							deviceStatus.setWebSocket(conn);
						    deviceStatus.setDeviceSn(sn);
						    deviceStatus.setStatus(1);
						    updateDevice(sn, deviceStatus);
						    updateCommandStatus(sn, ret);
						}
						
					}
			        
					//Thread.sleep(40000);
					//conn.close();
					
			/* if(System.currentTimeMillis()) */
					
				}  catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
	}

	


	

	/*websocket链接发生错误的时候*/
	@Override
	public void onError(WebSocket conn, Exception ex) {
		// TODO Auto-generated method stub
		ex.printStackTrace();
		if(conn!=null){
			conn.close();
			WebSocketPool.removeDeviceByWebsocket(conn);
		}
	//	System.out.println("socket断开了");
		logger.error("La connexion de la prise est interrompue: "+conn);
	}

	public void onStart() {
		
		// TODO Auto-generated method stub
		
	}
	
	 
	
	public void updateCommandStatus(String serial,String commandType) {
		List<MachineCommand> machineCommand=machineCommandRepository.findBySendStatusAndSerial(1, serial);
		for (MachineCommand mc: machineCommand){
			if(mc.getName().equals(commandType)) {
				MachineCommand machineCmd = new MachineCommand();
				machineCmd.setStatus(0);
				machineCmd.setSendStatus(1);
				machineCmd.setRunTime(new Date());
				machineCmd.setId(mc.getId());
				machineCommandRepository.save(machineCmd);
			}
		}

	}
	
	
	
	   //获得连接设备信息
	public void getDeviceInfo(JsonNode jsonNode, WebSocket args1){
		String sn=jsonNode.get("sn").asText();
		logger.info("numero de serie :"+sn);
		DeviceStatus deviceStatus=new DeviceStatus();
		if(sn!=null){
		
			Device d1=deviceService.selectDeviceBySerialNum(sn);
			
			if(d1==null){
				int i=	deviceService.insert(sn, 1);
				System.out.println(i);
			}else{
				//deviceService.updateByPrimaryKey()
				deviceService.updateStatusByPrimaryKey(d1.getId(), 1);
			}
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 //long l=System.currentTimeMillis();
	      
		    args1.send("{\"ret\":\"reg\",\"result\":true,\"cloudtime\":\"" + sdf.format(new Date()) + "\"}");	
		    deviceStatus.setWebSocket(args1);
		    deviceStatus.setStatus(1);
		    deviceStatus.setDeviceSn(sn);
		    updateDevice(sn, deviceStatus);
		    logger.debug("getDeviceInfo : " + WebSocketPool.getDeviceStatus(sn).getDeviceSn());
	        
		}else{
			 args1.send("{\"ret\":\"reg\",\"result\":false,\"reason\":1}");
			    deviceStatus.setWebSocket(args1);
			    deviceStatus.setDeviceSn(sn);
			    deviceStatus.setStatus(1);
			    updateDevice(sn, deviceStatus);
		}
		
	  	timeStamp=System.currentTimeMillis();
	  	timeStamp2=timeStamp;
	}
    
	 public void updateDevice(String sn,DeviceStatus deviceStatus) {
		if(WebSocketPool.getDeviceStatus(sn)!=null){
			//WebSocketPool.removeDeviceStatus(sn);
			WebSocketPool.addDeviceAndStatus(sn, deviceStatus);
		}else{
			WebSocketPool.addDeviceAndStatus(sn, deviceStatus);
		}
	}


	//Accès aux registres de log, y compris les numéros de machine
	private void getAttandence(JsonNode jsonNode,
			WebSocket conn) {
		// TODO Auto-generated method stub
		logger.debug("Buche de log -----------"+jsonNode);
		String sn=jsonNode.get("sn").asText();
		int count=jsonNode.get("count").asInt();
		//int logindex=jsonNode.get("logindex").asInt();
		int logindex=-1;
		if(jsonNode.get("logindex")!=null) {
			logindex=jsonNode.get("logindex").asInt();
		}
	//	int logindex=jsonNode.get("logindex").asInt();
		List<Records>recordAll=new ArrayList<Records>();
		System.out.println(jsonNode);
		JsonNode records=jsonNode.get("record");
		DeviceStatus deviceStatus=new DeviceStatus();
		Boolean flag=false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if (count>0) {
			Iterator iterator=records.elements();
			while(iterator.hasNext()){
				JsonNode type=(JsonNode)iterator.next();
				int enrollId=type.get("enrollid").asInt();
				String timeStr=type.get("time").asText();
				int mode=type.get("mode").asInt();
				int inOut=type.get("inout").asInt();
				int event=type.get("event").asInt();
				Double temperature=(double) 0;
				if(type.get("temp")!=null) {
					 temperature=type.get("temp").asDouble();
					 temperature=temperature/100;
					 temperature=(double) Math.round(temperature * 10) / 10;
					logger.debug("valeur de la température: "+temperature);
				}
				Records record=new Records();
				record.setDeviceSerialNum(sn);
				record.setEnrollId(enrollId);
				record.setEvent(event);
				record.setIntout(inOut);
				record.setMode(mode);
				record.setRecordsTime(timeStr);
				record.setTemperature(temperature);
				if (type.get("image")!=null) {
					String picName=UUID.randomUUID().toString();
				  	flag= ImageProcess.base64toImage(type.get("image").asText(), picName);
				  	if(flag) {
				  	record.setImage(picName+".jpg");
				  	}
				}
				recordAll.add(record);	
	 
			}
			
			if (logindex>=0) {
				conn.send("{\"ret\":\"sendlog\",\"result\":true"+",\"count\":"+count+",\"logindex\":"+logindex+",\"cloudtime\":\""
						+ sdf.format(new Date()) + "\"}");
			}else if(logindex<0){
				conn.send("{\"ret\":\"sendlog\",\"result\":true"+",\"cloudtime\":\""
						+ sdf.format(new Date()) + "\"}");
			}
			/*
			 * conn.send("{\"ret\":\"sendlog\",\"result\":true"+",\"count\":"+count+
			 * ",\"logindex\":"+logindex+",\"cloudtime\":\"" + sdf.format(new Date()) +
			 * "\"}");
			 */
			deviceStatus.setWebSocket(conn);
		    deviceStatus.setStatus(1);
		    deviceStatus.setDeviceSn(sn);
		    updateDevice(sn, deviceStatus);
		}else if(count==0){
			conn.send("{\"ret\":\"\"sendlog\"\",\"result\":false,\"reason\":1}");
			deviceStatus.setWebSocket(conn);
		    deviceStatus.setStatus(1);
		    deviceStatus.setDeviceSn(sn);
		    updateDevice(sn, deviceStatus);
		}
	    
	    System.out.println(recordAll);
		for (int i = 0; i < recordAll.size(); i++) {
			Records recordsTemp=recordAll.get(i);	
			recordsService.insert(recordsTemp);
		}
		
		timeStamp2=System.currentTimeMillis();
		
	}

	// Obtenir des informations sur l'enregistrement de la poussée de la machine
	private void getEnrollInfo(JsonNode jsonNode,
			org.java_websocket.WebSocket conn) {
		// TODO Auto-generated method stub
		//System.out.println("连接数据类型"+(conn.getData()).getClass());
		//int enrollId=jsonNode.get("enrollid").asInt();
	//	System.out.println("json"+json);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sn=jsonNode.get("sn").asText();
		String signatures1=jsonNode.get("record").asText();
		Boolean flag=false;
		DeviceStatus deviceStatus=new DeviceStatus();
		 if (signatures1==null) {
			 conn.send("{\"ret\":\"senduser\",\"result\":false,\"reason\":1}");
			 deviceStatus.setWebSocket(conn);
			    deviceStatus.setStatus(1);
			    deviceStatus.setDeviceSn(sn);
			    updateDevice(sn, deviceStatus);
		}else{
			int backupnum=jsonNode.get("backupnum").asInt();
		//	if(backupnum!=10&&backupnum!=11){
				int enrollId=jsonNode.get("enrollid").asInt();
				String name=jsonNode.get("name").asText();
				int rollId=jsonNode.get("admin").asInt();
				String signatures=jsonNode.get("record").asText();
				Person person=new Person();
				person.setId(enrollId);
				person.setName(name);
				person.setRollId(rollId);
			//	System.out.println("员工号"+enrollId);
				personService.selectByPrimaryKey(enrollId);
				//System.out.println("人员信息"+personService.selectByPrimaryKey(enrollId));
				logger.debug("Accès aux données du personnel : " + personService.selectByPrimaryKey(enrollId).getName());
				if (personService.selectByPrimaryKey(enrollId)==null) {
					personService.insert(person);
				}
				EnrollInfo enrollInfo=new EnrollInfo();
				enrollInfo.setEnrollId(enrollId);
				enrollInfo.setBackupnum(backupnum);
				enrollInfo.setSignatures(signatures);
				if (backupnum==50) {
     				
     				String picName=UUID.randomUUID().toString();
				  	flag=ImageProcess.base64toImage(jsonNode.get("record").asText(), picName);
				  	enrollInfo.setImagePath(picName+".jpg");
					}
                if (enrollInfoService.selectByBackupnum(enrollId, backupnum)==null) {
					/*
					 * if (backupnum==50) {
					 * 
					 * String picName=UUID.randomUUID().toString();
					 * flag=ImageProcess.base64toImage(jsonNode.get("record").asText(), picName);
					 * enrollInfo.setImagePath(picName+".jpg"); }
					 */
                	enrollInfoService.insertSelective(enrollInfo);
				}
				
				conn.send("{\"ret\":\"senduser\",\"result\":true,\"cloudtime\":\"" + sdf.format(new Date()) + "\"}");
				deviceStatus.setWebSocket(conn);
			    deviceStatus.setStatus(1);
			    deviceStatus.setDeviceSn(sn);
			    updateDevice(sn, deviceStatus);
			/*}else{
				System.out.println("卡号或者密码的情况"+jsonNode);
				
				 conn.send("{\"ret\":\"senduser\",\"result\":true,\"cloudtime\":\"" + sdf.format(new Date()) + "\"}");
				 deviceStatus.setWebSocket(conn);
				    deviceStatus.setStatus(1);
				    deviceStatus.setDeviceSn(sn);
				    updateDevice(sn, deviceStatus);
				}*/
		}
		 timeStamp2=System.currentTimeMillis();
	}

	// Obtenir une liste d'utilisateurs, demande initiée par le serveur
     	private void getUserList(JsonNode jsonNode,
			WebSocket conn) {
		    List<UserTemp>userTemps=new ArrayList<UserTemp>();
     		boolean result=jsonNode.get("result").asBoolean();
     		
     		int count;
     		JsonNode records=jsonNode.get("record");
     	//	System.out.println("用户列表"+records);
     		String sn=jsonNode.get("sn").asText();
     		DeviceStatus deviceStatus=new DeviceStatus();
     		if(result){
     			count=jsonNode.get("count").asInt();
     			//System.out.println("用户数："+count);
     			if (count>0) {
     				 Iterator iterator=records.elements();
     	     		  while (iterator.hasNext()) {
     	     			JsonNode type=(JsonNode)iterator.next();
     					int enrollId=type.get("enrollid").asInt();
     					//int enrollId=Integer.valueOf(enrollId1);
     					int admin=type.get("admin").asInt();
     					int backupnum=type.get("backupnum").asInt();
     					UserTemp userTemp=new UserTemp();
     					userTemp.setEnrollId(enrollId);
     					userTemp.setBackupnum(backupnum);
     					userTemp.setAdmin(admin);
     					userTemps.add(userTemp);
     					
     					
     				}
     	     		conn.send("{\"cmd\":\"getuserlist\",\"stn\":false}");
     	     		//DeviceStatus deviceStatus=new DeviceStatus();
					deviceStatus.setWebSocket(conn);
				    deviceStatus.setStatus(1);
				    deviceStatus.setDeviceSn(sn);
				    updateDevice(sn, deviceStatus); 
				   
				}
     		 
     			 
     		}
     		for (int i = 0; i < userTemps.size(); i++) {
				UserTemp uTemp=new UserTemp();
				uTemp=userTemps.get(i);
				if(personService.selectByPrimaryKey(uTemp.getEnrollId())==null){
					Person personTemp=new Person();
					personTemp.setId(uTemp.getEnrollId());
					personTemp.setName("");
					personTemp.setRollId(uTemp.getAdmin());
					personService.insert(personTemp);
					
					
				}
			}
     		
     		for (int i = 0; i < userTemps.size(); i++) {
				UserTemp uTemp1=new UserTemp();
				uTemp1=userTemps.get(i);
				if(enrollInfoService.selectByBackupnum(uTemp1.getEnrollId(), uTemp1.getBackupnum())==null){
					EnrollInfo enrollInfo=new EnrollInfo();
					enrollInfo.setEnrollId(uTemp1.getEnrollId());
					enrollInfo.setBackupnum(uTemp1.getBackupnum());
				 	enrollInfoService.insertSelective(enrollInfo);
				//en
				}
			}
     		 updateCommandStatus(sn, "getuserlist");
     		
	     }


	// Obtenir les détails de l'utilisateur
     	private void getUserInfo(JsonNode jsonNode,
    			WebSocket conn) {
    		// TODO Auto-generated method stub
     		System.out.println(jsonNode);
     		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     	//	System.out.println("用户具体信息"+jsonNode);
     		Boolean result=jsonNode.get("result").asBoolean();
     		
     		String sn=jsonNode.get("sn").asText();
     		logger.debug("données sn"+sn);
     	
     		System.out.println(jsonNode);
     	//	DeviceStatus deviceStatus=new DeviceStatus();
     		Boolean flag=false;
     		if(result){
     			int backupnum=jsonNode.get("backupnum").asInt();
     			String signatures1=jsonNode.get("record").asText();
     		//	if(backupnum!=10&&backupnum!=11){
     				int enrollId=jsonNode.get("enrollid").asInt();
     				String name=jsonNode.get("name").asText();
     				int admin=jsonNode.get("admin").asInt();
     				String signatures=jsonNode.get("record").asText();
     				
     				Person person=new Person();
     				person.setId(enrollId);
     				person.setName(name);
     				person.setRollId(admin);
     				EnrollInfo enrollInfo=enrollInfoService.selectByBackupnum(enrollId, backupnum);
     				if (backupnum==50) {
         				
         				String picName=UUID.randomUUID().toString();
    				  	flag=ImageProcess.base64toImage(jsonNode.get("record").asText(), picName);
    				  	enrollInfo.setImagePath(picName+".jpg");
    					}
     				if (personService.selectByPrimaryKey(enrollId)==null) {
    					personService.insert(person);
    				}else if(personService.selectByPrimaryKey(enrollId)!=null){
    					personService.updateByPrimaryKey(person);
    				}
                    if (enrollInfo==null) {
                 //   	enrollInfoService.insert(enrollId, backupnum, signatures);
                    	enrollInfoService.insertSelective(enrollInfo);
    				}else if(enrollInfo!=null){
    				//	enrollInfoService.updateByEnrollIdAndBackupNum(signatures, enrollId, backupnum);
    					enrollInfo.setSignatures(signatures);
    					enrollInfoService.updateByPrimaryKeyWithBLOBs(enrollInfo);
    				}
            
                    
     			}
     	//	}
     		  updateCommandStatus(sn, "getuserinfo");
    	
    	}

	//Importe tous les enregistrements de log
     	private void getAllLog(JsonNode jsonNode, WebSocket conn) {
    	
     		Boolean result=jsonNode.get("result").asBoolean();
     		List<Records>recordAll=new ArrayList<Records>();
    		System.out.println("记录"+jsonNode);
     		String sn=jsonNode.get("sn").asText();
    		JsonNode records=jsonNode.get("record");
    		DeviceStatus deviceStatus=new DeviceStatus();
     		int count;
     		boolean flag=false;
     		if (result) {
				count=jsonNode.get("count").asInt();
				if(count>0){
					Iterator iterator=records.elements();
					while(iterator.hasNext()){
						JsonNode type=(JsonNode)iterator.next();
						int enrollId=type.get("enrollid").asInt();
						String timeStr=type.get("time").asText();
						int mode=type.get("mode").asInt();
						int inOut=type.get("inout").asInt();
						int event=type.get("event").asInt();
						Double temperature=(double) 0;
						if(type.get("temp")!=null) {
							 temperature=type.get("temp").asDouble();
							 temperature=temperature/100;
							 temperature=(double) Math.round(temperature * 10) / 10;
							logger.debug("valeur de la température: "+temperature);
						}
						Records record=new Records();
					//	record.setDeviceSerialNum(sn);
						record.setEnrollId(enrollId);
						record.setEvent(event);
						record.setIntout(inOut);
						record.setMode(mode);
						record.setRecordsTime(timeStr);
						record.setDeviceSerialNum(sn);
						record.setTemperature(temperature);
					
						recordAll.add(record);	      
					}
					  conn.send("{\"cmd\":\"getalllog\",\"stn\":false}");
					  deviceStatus.setWebSocket(conn);
	 				  deviceStatus.setStatus(1);
	 				    deviceStatus.setDeviceSn(sn);
	 			      updateDevice(sn, deviceStatus);
				}
			}
     	//	 System.out.println(recordAll);
     		for (int i = 0; i < recordAll.size(); i++) {
     			Records recordsTemp=recordAll.get(i);	
     			recordsService.insert(recordsTemp);
     		}
     		 updateCommandStatus(sn, "getalllog");
    		
    	}

	//Importe tous les enregistrements de log
     	private void getnewLog(JsonNode jsonNode, WebSocket conn) {
    	
     		Boolean result=jsonNode.get("result").asBoolean();
     		List<Records>recordAll=new ArrayList<Records>();
    		logger.debug("record (en sport, etc.): "+jsonNode);
     		String sn=jsonNode.get("sn").asText();
    		JsonNode records=jsonNode.get("record");
    		DeviceStatus deviceStatus=new DeviceStatus();
    		boolean flag=false;
     		int count;
     		if (result) {
				count=jsonNode.get("count").asInt();
				if(count>0){
					Iterator iterator=records.elements();
					while(iterator.hasNext()){
						JsonNode type=(JsonNode)iterator.next();
						int enrollId=type.get("enrollid").asInt();
						String timeStr=type.get("time").asText();
						int mode=type.get("mode").asInt();
						int inOut=type.get("inout").asInt();
						int event=type.get("event").asInt();
						Double temperature=(double) 0;
						if(type.get("temp")!=null) {
							 temperature=type.get("temp").asDouble();
							 temperature=temperature/100;
							 temperature=(double) Math.round(temperature * 10) / 10;
							logger.debug("valeur de la température: "+temperature);
						}
						Records record=new Records();
					//	record.setDeviceSerialNum(sn);
						record.setEnrollId(enrollId);
						record.setEvent(event);
						record.setIntout(inOut);
						record.setMode(mode);
						record.setRecordsTime(timeStr);
						record.setDeviceSerialNum(sn);
						record.setTemperature(temperature);
						recordAll.add(record);	      
					}
					  conn.send("{\"cmd\":\"getnewlog\",\"stn\":false}");
					  deviceStatus.setWebSocket(conn);
	 				  deviceStatus.setStatus(1);
	 				    deviceStatus.setDeviceSn(sn);
	 			      updateDevice(sn, deviceStatus);
				}
			}
     	//	 System.out.println(recordAll);
     		for (int i = 0; i < recordAll.size(); i++) {
     			Records recordsTemp=recordAll.get(i);	
     			recordsService.insert(recordsTemp);
     		}
     		updateCommandStatus(sn, "getnewlog");
    		
    	}


	public static void main(String[] args) throws InterruptedException, IOException {

	}
}

