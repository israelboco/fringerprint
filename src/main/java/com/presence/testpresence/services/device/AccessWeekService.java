package com.presence.testpresence.services.device;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.presence.testpresence.model.entities.AccessWeek;
import com.presence.testpresence.model.entities.Device;
import com.presence.testpresence.model.entities.MachineCommand;
import com.presence.testpresence.model.repositories.AccessDayRepository;
import com.presence.testpresence.model.repositories.AccessWeekRepository;
import com.presence.testpresence.model.repositories.DeviceRepository;
import com.presence.testpresence.model.repositories.MachineCommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class AccessWeekService {

	 
	@Autowired
	AccessDayRepository accessDayRepository;
	@Autowired
	AccessWeekRepository accessWeekRepository;
	
	@Autowired
	DeviceRepository deviceRepository;
	
	@Autowired
	MachineCommandRepository machineCommandRepository;
	
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		this.accessDayRepository.deleteById(id);
		return id;
	}

	public int insert(AccessWeek record) {
		// TODO Auto-generated method stub
		record = this.accessWeekRepository.save(record);
		return record.getId();
	}

	public int insertSelective(AccessWeek record) {
		// TODO Auto-generated method stub
		return accessWeekRepository.save(record).getId();
	}

	public AccessWeek selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return accessWeekRepository.findOneById(id);
	}

	public int updateByPrimaryKeySelective(AccessWeek record) {
		// TODO Auto-generated method stub
		return accessWeekRepository.save(record).getId();
	}

	public int updateByPrimaryKey(AccessWeek record) {
		// TODO Auto-generated method stub
		return accessWeekRepository.save(record).getId();
	}

	public List<AccessWeek> selectAllAccessWeek() {
		// TODO Auto-generated method stub
		return accessWeekRepository.findAll();
	}

	 //下发周时段
	public void setAccessWeek() {
		StringBuilder sb=new StringBuilder();
		 sb.append("{\"cmd\":\"setdevlock\",\"weekzone\":[");
		 try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 List<AccessWeek> accessWeeks = accessWeekRepository.findAll();
		 List<AccessWeek>accessWeeksTemp=new ArrayList<AccessWeek>();
		 if(accessWeeks.size()==8){
			 for (int i = 0; i < accessWeeks.size(); i++) {
				  sb.append("{\"week\":[");
	                sb.append("{\"day\":" + accessWeeks.get(i).getSunday() + "},");
	                sb.append("{\"day\":" + accessWeeks.get(i).getMonday() + "},");
	                sb.append("{\"day\":" + accessWeeks.get(i).getTuesday() + "},");
	                sb.append("{\"day\":" + accessWeeks.get(i).getWednesday() + "},");
	                sb.append("{\"day\":" + accessWeeks.get(i).getThursday() + "},");
	                sb.append("{\"day\":" + accessWeeks.get(i).getFriday() + "},");
	                sb.append("{\"day\":" + accessWeeks.get(i).getSaturday() + "}");
	                if (i != 7) {
		                  sb.append("]},");
		              } else {
		                  sb.append("]}");
		              }
			}
		 }else if(accessWeeks.size()<8){
			 accessWeeksTemp.addAll(accessWeeks);
			 for (int i = accessWeeks.size(); i < 8; i++) {
				 AccessWeek accessWeek=new AccessWeek();
				 accessWeek.setId(i+1);
				 accessWeek.setMonday(0);
				 accessWeek.setTuesday(0);
				 accessWeek.setWednesday(0);
				 accessWeek.setThursday(0);
				 accessWeek.setFriday(0);
				 accessWeek.setSaturday(0);
				 accessWeek.setSunday(0);
				 accessWeeksTemp.add(i, accessWeek);
				
			}
			 
			 for (int i = 0; i < accessWeeksTemp.size(); i++) {
				 sb.append("{\"week\":[");
	                sb.append("{\"day\":" + accessWeeksTemp.get(i).getSunday() + "},");
	                sb.append("{\"day\":" + accessWeeksTemp.get(i).getMonday() + "},");
	                sb.append("{\"day\":" + accessWeeksTemp.get(i).getTuesday() + "},");
	                sb.append("{\"day\":" + accessWeeksTemp.get(i).getWednesday() + "},");
	                sb.append("{\"day\":" + accessWeeksTemp.get(i).getThursday() + "},");
	                sb.append("{\"day\":" + accessWeeksTemp.get(i).getFriday() + "},");
	                sb.append("{\"day\":" + accessWeeksTemp.get(i).getSaturday() + "}");
	                if (i != 7) {
		                  sb.append("]},");
		              } else {
		                  sb.append("]}");
		              }
			}
		 }
		 
		 sb.append("]}");
		 String message=sb.toString();
		 List<Device>deviceList = deviceRepository.findAll();
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
	
}
