package com.presence.testpresence.services.device;

import java.util.ArrayList;
import java.util.List;


import com.presence.testpresence.model.entities.EnrollInfo;
import com.presence.testpresence.model.entities.Person;
import com.presence.testpresence.model.entities.UserInfo;
import com.presence.testpresence.model.repositories.EnrollInfoRepository;
import com.presence.testpresence.model.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class EnrollInfoService {
	
	@Autowired
	EnrollInfoRepository enrollInfoRepository;
	
	@Autowired
	PersonRepository personRepository;

	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		this.enrollInfoRepository.deleteById(id);
		return id;
	}

	public int insertSelective(EnrollInfo record) {
		// TODO Auto-generated method stub
		return enrollInfoRepository.save(record).getEnrollId();
	}

	public EnrollInfo selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return enrollInfoRepository.findOneById(id);
	}

	public int updateByPrimaryKeySelective(EnrollInfo record) {
		// TODO Auto-generated method stub
		return enrollInfoRepository.save(record).getEnrollId();
	}

	public int updateByPrimaryKeyWithBLOBs(EnrollInfo record) {
		// TODO Auto-generated method stub
		return enrollInfoRepository.save(record).getEnrollId();
	}


	public int insert(int enrollid, int backupnum,String imagePath,String signature) {
		// TODO Auto-generated method stub
		EnrollInfo enrollInfo = new EnrollInfo();
		enrollInfo.setBackupnum(backupnum);
		enrollInfo.setEnrollId(enrollid);
		enrollInfo.setSignatures(signature);
		enrollInfo.setImagePath(imagePath);
		return enrollInfoRepository.save(enrollInfo).getEnrollId();
	}



	public EnrollInfo selectByBackupnum(int enrollId, int backupnum) {
		// TODO Auto-generated method stub
		return enrollInfoRepository.findByEnrollIdAndBackupnum(enrollId, backupnum);
	}


	public List<UserInfo> usersToSendDevice() {
		List<Person> persons = personRepository.findAll();
		List<EnrollInfo> enrollInfos = enrollInfoRepository.findAll();
		List<UserInfo>userInfos=new ArrayList<UserInfo>();
		for (int i = 0; i < persons.size(); i++) {
			//Person person=new Person();
			for (int j = 0; j < enrollInfos.size(); j++) {
				if(persons.get(i).getId()==enrollInfos.get(j).getEnrollId()){
					UserInfo userInfo=new UserInfo();
					userInfo.setAdmin(persons.get(i).getRollId());
					userInfo.setBackupnum(enrollInfos.get(j).getBackupnum());
					userInfo.setEnrollId(persons.get(i).getId());
					userInfo.setName(persons.get(i).getName());
					userInfo.setRecord(enrollInfos.get(j).getSignatures());
					
					userInfos.add(userInfo);
				}
				
			}
		}

		return userInfos;
	}



	public List<EnrollInfo> selectAll() {
		// TODO Auto-generated method stub
		return enrollInfoRepository.findAll();
	}



	public List<EnrollInfo> selectByEnrollId(int enrollId) {
		// TODO Auto-generated method stub
		return enrollInfoRepository.findByEnrollId(enrollId);
	}


	public int updateByEnrollIdAndBackupNum(String signatures, int enrollId,
			int backupnum) {
		// TODO Auto-generated method stub
		EnrollInfo enrollInfo = enrollInfoRepository.findByEnrollIdAndBackupnum(enrollId, backupnum);
		enrollInfo.setSignatures(signatures);

		return enrollInfoRepository.save(enrollInfo).getEnrollId();
	}



	public int deleteByEnrollId(Integer id) {
		// TODO Auto-generated method stub
		this.enrollInfoRepository.deleteByEnrollId(id);
		return id;
	}

	
	
}
