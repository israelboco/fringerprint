package com.presence.testpresence.services.device;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.presence.testpresence.model.entities.AccessDay;
import com.presence.testpresence.model.entities.Device;
import com.presence.testpresence.model.entities.MachineCommand;
import com.presence.testpresence.model.repositories.AccessDayRepository;
import com.presence.testpresence.model.repositories.DeviceRepository;
import com.presence.testpresence.model.repositories.MachineCommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class AccessDayService {
	
	@Autowired
	AccessDayRepository accessDayRepository;
	
	@Autowired
	MachineCommandRepository machineCommandRepository;

	@Autowired
	DeviceRepository deviceRepository;

	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		this.accessDayRepository.deleteById(id);
		return id;
	}

	public int insert(AccessDay record) {
		// TODO Auto-generated method stub
		return accessDayRepository.save(record).getId();
	}

	public int insertSelective(AccessDay record) {
		// TODO Auto-generated method stub
		return accessDayRepository.save(record).getId();
	}

	public AccessDay selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return accessDayRepository.findOneById(id);
	}

	public int updateByPrimaryKeySelective(AccessDay record) {
		// TODO Auto-generated method stub
		return accessDayRepository.save(record).getId();
	}

	public int updateByPrimaryKey(AccessDay record) {
		// TODO Auto-generated method stub
		return accessDayRepository.save(record).getId();
	}
	public List<AccessDay> selectAll() {
		// TODO Auto-generated method stub
		return accessDayRepository.findAll();
	}

	public void setAccessDay() {
		// TODO Auto-generated method stub
		StringBuilder sb=new StringBuilder();
		sb.append("{\"cmd\":\"setdevlock\",\"dayzone\":[");
		List<AccessDay> accessDays = accessDayRepository.findAll();
		List<AccessDay> accessDaysTemp = new ArrayList<AccessDay>();
	
		
         if(accessDays.size()<8){
        	 accessDaysTemp.addAll(accessDays);
        	 for (int i = accessDays.size(); i < 8; i++) {
				 AccessDay accessDay1 = getAccessDay(i);
				 accessDaysTemp.add(i, accessDay1);
				
			}
        	 
         }
		 System.out.println(accessDaysTemp.size());
		 for (int i = 0; i < accessDaysTemp.size(); i++) {
			System.out.println(accessDaysTemp.get(i));
			
		}
		 
		for (int i = 0; i < accessDaysTemp.size(); i++) {
			  sb.append("{\"day\":[");
              sb.append("{\"section\":\"" + accessDaysTemp.get(i).getStartTime1() + "~" + accessDaysTemp.get(i).getEndTime1() + "\"},");
              sb.append("{\"section\":\"" + accessDaysTemp.get(i).getStartTime2() + "~" + accessDaysTemp.get(i).getEndTime2() + "\"},");
              sb.append("{\"section\":\"" + accessDaysTemp.get(i).getStartTime3() + "~" + accessDaysTemp.get(i).getEndTime3() + "\"},");
              sb.append("{\"section\":\"" + accessDaysTemp.get(i).getStartTime4() + "~" + accessDaysTemp.get(i).getEndTime4() + "\"},");
              sb.append("{\"section\":\"" + accessDaysTemp.get(i).getStartTime5() + "~" + accessDaysTemp.get(i).getEndTime5() + "\"}");
              if (i != 7) {
                  sb.append("]},");
              } else {
                  sb.append("]}");
              }              
		}
		   sb.append("]}");
		  
		 // System.out.println("jshshhs"+sb.toString());
		String message = sb.toString();
		//System.out.println(message);
		//WebSocketPool.sendMessageToAllDeviceFree(message);
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

	private AccessDay getAccessDay(int i) {
		AccessDay accessDay1 = new AccessDay();
		accessDay1.setId(i +1);

		accessDay1.setStartTime1("00:00");
		accessDay1.setEndTime1("00:00");
		accessDay1.setStartTime2("00:00");
		accessDay1.setEndTime2("00:00");
		accessDay1.setStartTime3("00:00");
		accessDay1.setEndTime3("00:00");
		accessDay1.setStartTime4("00:00");
		accessDay1.setEndTime4("00:00");
		accessDay1.setStartTime5("00:00");
		accessDay1.setEndTime5("00:00");
		return accessDay1;
	}

}
