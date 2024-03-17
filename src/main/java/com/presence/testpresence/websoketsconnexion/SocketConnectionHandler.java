package com.presence.testpresence.websoketsconnexion;
// Program to eastablish the socket connexion

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.presence.testpresence.model.entities.*;
import com.presence.testpresence.model.repositories.MachineCommandRepository;
import com.presence.testpresence.services.device.*;
import com.presence.testpresence.util.ImageProcess;
import com.presence.testpresence.websokets.WebSocketPool;
import com.presence.testpresence.ws.DeviceStatus;
import com.presence.testpresence.ws.UserTemp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

// Socket-connexion Configuration class
public class SocketConnectionHandler extends TextWebSocketHandler {

    public static Logger logger = LoggerFactory.getLogger(SocketConnectionHandler.class);

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

    Long timeStamp=0L;
    Long timeStamp2=0L;

    // In this list all the connexions will be stored
    // Then it will be used to broadcast the message
    List<WebSocketSession> webSocketSessions
            = Collections.synchronizedList(new ArrayList<>());

    // This method is executed when client tries to sessionect
    // to the sockets
    @Override
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception
    {

        super.afterConnectionEstablished(session);
        // Logging the connexion ID with sessionected Message
        //System.out.println(session.getId() + " sessionected");
        logger.debug(session.getId() + " connexion");

        // Adding the session into the list
        webSocketSessions.add(session);
    }

    // When client dissessionect from WebSocket then this
    // method is called
    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus status)throws Exception
    {
        super.afterConnectionClosed(session, status);
        System.out.println(session.getId() + " Dissessionected");

        // Removing the connexion info from the list
        webSocketSessions.remove(session);
    }

    // It will handle exchanging of message in the network
    // It will have a session info who is sending the
    // message Also the Message object passes as parameter
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception
    {

        System.out.println(message + " message ");
        super.handleMessage(session, message);

        // Iterate through the list and pass the message to
        // all the sessions Ignore the session in the list
        // which wants to send the message.
        for (WebSocketSession webSocketSession :
                webSocketSessions) {
            if (session == webSocketSession)
                continue;

            ObjectMapper objectMapper = new ObjectMapper();
            String ret;

            try {
                //Thread.sleep(7000);
                String msg = message.toString().replaceAll(",]", "]");

                JsonNode jsonNode = (JsonNode)objectMapper.readValue(msg, JsonNode.class);
                //	System.out.println("数据"+jsonNode);
                if (jsonNode.has("cmd")) {
                    ret=jsonNode.get("cmd").asText();
                    if ("reg".equals(ret)) {
                        System.out.println("设备信息"+jsonNode);
                        try {

                            this.getDeviceInfo(jsonNode, session);


                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            session.sendMessage(new TextMessage("{\"ret\":\"reg\",\"result\":false,\"reason\":1}"));
                            e.printStackTrace();
                        }
                    }else if("sendlog".equals(ret)){
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                        try {
                            this.getAttandence(jsonNode,session);

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            session.sendMessage(new TextMessage("{\"ret\":\"sendlog\",\"result\":false,\"reason\":1}"));
                            e.printStackTrace();
                        }
                    }else if("senduser".endsWith(ret)){
                        System.out.println(jsonNode);


                        try {
                            this.getEnrollInfo(jsonNode,session);

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            session.sendMessage(new TextMessage("{\"ret\":\"senduser\",\"result\":false,\"reason\":1}"));
                            e.printStackTrace();
                        }
                    }

                }else if(jsonNode.has("ret")){
                    ret=jsonNode.get("ret").asText();
                    //boolean result;
                    if("getuserlist".equals(ret)){
                        //	System.out.println(jsonNode);
                        //	System.out.println("数据"+message);
                        this.getUserList(jsonNode,session);

                    }else if("getuserinfo".equals(ret)){
                        //System.out.println("json数据"+jsonNode);
                        this.getUserInfo(jsonNode,session);
                        String sn=jsonNode.get("sn").asText();
                        DeviceStatus deviceStatus=new DeviceStatus();
                        deviceStatus.setWebSocket(session);
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
                        deviceStatus.setWebSocket(session);
                        deviceStatus.setDeviceSn(sn);
                        deviceStatus.setStatus(1);
                        updateDevice(sn, deviceStatus);
                        updateCommandStatus(sn, "setuserinfo");
                        System.out.println("下发数据"+jsonNode);
                    }else if("getalllog".equals(ret)){

                        System.out.println("获取所有打卡记录"+jsonNode);
                        try {
                            this.getAllLog(jsonNode,session);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();

                        }

                    }else if("getnewlog".equals(ret)){

                        System.out.println("获取所有打卡记录"+jsonNode);
                        try {
                            this.getnewLog(jsonNode,session);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();

                        }

                    }
                    else if("deleteuser".equals(ret)){
                        String sn=jsonNode.get("sn").asText();
                        DeviceStatus deviceStatus=new DeviceStatus();
                        deviceStatus.setWebSocket(session);
                        deviceStatus.setDeviceSn(sn);
                        deviceStatus.setStatus(1);
                        updateDevice(sn, deviceStatus);
                        System.out.println("删除人员"+jsonNode);
                        updateCommandStatus(sn, "deleteuser");
                    }else if("initsys".equals(ret)){
                        String sn=jsonNode.get("sn").asText();
                        DeviceStatus deviceStatus=new DeviceStatus();
                        deviceStatus.setWebSocket(session);
                        deviceStatus.setDeviceSn(sn);
                        deviceStatus.setStatus(1);
                        updateDevice(sn, deviceStatus);
                        System.out.println("初始化系统"+jsonNode);
                        updateCommandStatus(sn, "initsys");
                    }else if("setdevlock".equals(ret)){
                        String sn=jsonNode.get("sn").asText();
                        DeviceStatus deviceStatus=new DeviceStatus();
                        deviceStatus.setWebSocket(session);
                        deviceStatus.setDeviceSn(sn);
                        deviceStatus.setStatus(1);
                        updateDevice(sn, deviceStatus);
                        System.out.println("设置天时间段"+jsonNode);
                        updateCommandStatus(sn, "setdevlock");
                    }else if("setuserlock".equals(ret)){
                        String sn=jsonNode.get("sn").asText();
                        DeviceStatus deviceStatus=new DeviceStatus();
                        deviceStatus.setWebSocket(session);
                        deviceStatus.setDeviceSn(sn);
                        deviceStatus.setStatus(1);
                        updateDevice(sn, deviceStatus);
                        System.out.println("门禁授权"+jsonNode);
                        updateCommandStatus(sn, "setuserlock");
                    }else if("getdevinfo".equals(ret)){
                        String sn=jsonNode.get("sn").asText();
                        DeviceStatus deviceStatus=new DeviceStatus();
                        deviceStatus.setWebSocket(session);
                        deviceStatus.setDeviceSn(sn);
                        deviceStatus.setStatus(1);
                        updateDevice(sn, deviceStatus);
                        System.out.println(new Date()+"设备信息"+jsonNode);
                        updateCommandStatus(sn, "getdevinfo");
                    }else if("setusername".equals(ret)){
                        String sn=jsonNode.get("sn").asText();
                        DeviceStatus deviceStatus=new DeviceStatus();
                        deviceStatus.setWebSocket(session);
                        deviceStatus.setDeviceSn(sn);
                        deviceStatus.setStatus(1);
                        updateDevice(sn, deviceStatus);
                        System.out.println(new Date()+"下发姓名"+jsonNode);
                        updateCommandStatus(sn, "setusername");
                    }else if("reboot".equals(ret)){
                        String sn=jsonNode.get("sn").asText();
                        DeviceStatus deviceStatus=new DeviceStatus();
                        deviceStatus.setWebSocket(session);
                        deviceStatus.setDeviceSn(sn);
                        deviceStatus.setStatus(1);
                        updateDevice(sn, deviceStatus);
                        updateCommandStatus(sn, "reboot");
                    }else {
                        String sn=jsonNode.get("sn").asText();
                        DeviceStatus deviceStatus=new DeviceStatus();
                        deviceStatus.setWebSocket(session);
                        deviceStatus.setDeviceSn(sn);
                        deviceStatus.setStatus(1);
                        updateDevice(sn, deviceStatus);
                        updateCommandStatus(sn, ret);
                    }

                }

                //Thread.sleep(40000);
                //session.close();

                /* if(System.currentTimeMillis()) */

            }  catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            webSocketSession.sendMessage(message);
        }
    }


    public void updateCommandStatus(String serial,String commandType) {
        List<MachineCommand> machineCommand=machineCommandRepository.findBySendStatusAndSerial(1, serial);
        if(!machineCommand.isEmpty() && machineCommand.get(0).getName().equals(commandType)) {
            MachineCommand machineCmd = new MachineCommand();
            machineCmd.setStatus(0);
            machineCmd.setSendStatus(1);
            machineCmd.setRunTime(new Date());
            machineCmd.setId(machineCommand.get(0).getId());
            machineCommandRepository.save(machineCmd);

        }
    }



    //获得连接设备信息
    public void getDeviceInfo(JsonNode jsonNode,WebSocketSession args1) throws IOException {
        String sn=jsonNode.get("sn").asText();
        System.out.println("序列号"+sn);
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

            args1.sendMessage(new TextMessage("{\"ret\":\"reg\",\"result\":true,\"cloudtime\":\"" + sdf.format(new Date()) + "\"}"));
            deviceStatus.setWebSocket(args1);
            deviceStatus.setStatus(1);
            deviceStatus.setDeviceSn(sn);
            updateDevice(sn, deviceStatus);
            System.out.println(WebSocketPool.getDeviceStatus(sn));

        }else{
            args1.sendMessage(new TextMessage("{\"ret\":\"reg\",\"result\":false,\"reason\":1}"));
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


    //获得打卡记录，包括机器号
    private void getAttandence(JsonNode jsonNode,
                               WebSocketSession session) throws IOException {
        // TODO Auto-generated method stub
        System.out.println("打卡记录-----------"+jsonNode);
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
                    System.out.println("温度值"+temperature);
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
                session.sendMessage(new TextMessage("{\"ret\":\"sendlog\",\"result\":true"+",\"count\":"+count+",\"logindex\":"+logindex+",\"cloudtime\":\""
                        + sdf.format(new Date()) + "\"}"));
            }else if(logindex<0){
                session.sendMessage(new TextMessage("{\"ret\":\"sendlog\",\"result\":true"+",\"cloudtime\":\""
                        + sdf.format(new Date()) + "\"}"));
            }
            /*
             * session.send("{\"ret\":\"sendlog\",\"result\":true"+",\"count\":"+count+
             * ",\"logindex\":"+logindex+",\"cloudtime\":\"" + sdf.format(new Date()) +
             * "\"}");
             */
            deviceStatus.setWebSocket(session);
            deviceStatus.setStatus(1);
            deviceStatus.setDeviceSn(sn);
            updateDevice(sn, deviceStatus);
        }else if(count==0){
            session.sendMessage(new TextMessage("{\"ret\":\"\"sendlog\"\",\"result\":false,\"reason\":1}"));
            deviceStatus.setWebSocket(session);
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

    //获取机器推送注册信息
    private void getEnrollInfo(JsonNode jsonNode,
                               WebSocketSession session) throws IOException {
        // TODO Auto-generated method stub
        //System.out.println("连接数据类型"+(session.getData()).getClass());
        //int enrollId=jsonNode.get("enrollid").asInt();
        //	System.out.println("json"+json);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sn=jsonNode.get("sn").asText();
        String signatures1=jsonNode.get("record").asText();
        Boolean flag=false;
        DeviceStatus deviceStatus=new DeviceStatus();
        if (signatures1==null) {
            session.sendMessage(new TextMessage("{\"ret\":\"senduser\",\"result\":false,\"reason\":1}"));
            deviceStatus.setWebSocket(session);
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
            System.out.println("获取人员数据"+personService.selectByPrimaryKey(enrollId));
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

            session.sendMessage(new TextMessage("{\"ret\":\"senduser\",\"result\":true,\"cloudtime\":\"" + sdf.format(new Date()) + "\"}"));
            deviceStatus.setWebSocket(session);
            deviceStatus.setStatus(1);
            deviceStatus.setDeviceSn(sn);
            updateDevice(sn, deviceStatus);
			/*}else{
				System.out.println("卡号或者密码的情况"+jsonNode);

				 session.send("{\"ret\":\"senduser\",\"result\":true,\"cloudtime\":\"" + sdf.format(new Date()) + "\"}");
				 deviceStatus.setWebSocket(session);
				    deviceStatus.setStatus(1);
				    deviceStatus.setDeviceSn(sn);
				    updateDevice(sn, deviceStatus);
				}*/
        }
        timeStamp2=System.currentTimeMillis();
    }

    //获取用户列表，服务器主动发出请求
    private void getUserList(JsonNode jsonNode,
                             WebSocketSession session) throws IOException {
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
                session.sendMessage(new TextMessage("{\"cmd\":\"getuserlist\",\"stn\":false}"));
                //DeviceStatus deviceStatus=new DeviceStatus();
                deviceStatus.setWebSocket(session);
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


    //     	获得用户详细信息
    private void getUserInfo(JsonNode jsonNode,
                             WebSocketSession session) {
        // TODO Auto-generated method stub
        System.out.println(jsonNode);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //	System.out.println("用户具体信息"+jsonNode);
        Boolean result=jsonNode.get("result").asBoolean();

        String sn=jsonNode.get("sn").asText();
        System.out.println("sn数据"+sn);

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

    //获取全部打卡记录
    private void getAllLog(JsonNode jsonNode, WebSocketSession session) throws IOException {

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
                        System.out.println("温度值"+temperature);
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
                session.sendMessage(new TextMessage("{\"cmd\":\"getalllog\",\"stn\":false}"));
                deviceStatus.setWebSocket(session);
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

    //获取全部打卡记录
    private void getnewLog(JsonNode jsonNode, WebSocketSession session) throws IOException {

        Boolean result=jsonNode.get("result").asBoolean();
        List<Records>recordAll=new ArrayList<Records>();
        System.out.println("记录"+jsonNode);
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
                        System.out.println("温度值"+temperature);
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
                session.sendMessage(new TextMessage("{\"cmd\":\"getnewlog\",\"stn\":false}"));
                deviceStatus.setWebSocket(session);
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
}

