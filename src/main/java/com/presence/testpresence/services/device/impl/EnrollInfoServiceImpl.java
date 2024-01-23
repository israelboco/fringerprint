package com.presence.testpresence.services.device.impl;

import java.util.ArrayList;
import java.util.List;

import com.presence.testpresence.mapper.EnrollInfoMapper;
import com.presence.testpresence.mapper.PersonMapper;
import com.presence.testpresence.model.entities.EnrollInfo;
import com.presence.testpresence.model.entities.Person;
import com.presence.testpresence.model.entities.UserInfo;
import com.presence.testpresence.services.device.EnrollInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EnrollInfoServiceImpl implements EnrollInfoService {
	
	@Autowired
	EnrollInfoMapper enrollInfoMapper;
	
	@Autowired
	PersonMapper personMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return enrollInfoMapper.deleteByPrimaryKey(id);
	}

	

	@Override
	public int insertSelective(EnrollInfo record) {
		// TODO Auto-generated method stub
		return enrollInfoMapper.insertSelective(record);
	}

	@Override
	public EnrollInfo selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return enrollInfoMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(EnrollInfo record) {
		// TODO Auto-generated method stub
		return enrollInfoMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKeyWithBLOBs(EnrollInfo record) {
		// TODO Auto-generated method stub
		return enrollInfoMapper.updateByPrimaryKeyWithBLOBs(record);
	}



	@Override
	public int insert(int enrollid, int backupnum,String imagePath,String signature) {
		// TODO Auto-generated method stub
		return enrollInfoMapper.insert(enrollid, backupnum,imagePath, signature);
	}



	@Override
	public EnrollInfo selectByBackupnum(int enrollId, int backupnum) {
		// TODO Auto-generated method stub
		return enrollInfoMapper.selectByBackupnum(enrollId, backupnum);
	}



	@Override
	public List<UserInfo> usersToSendDevice() {
		List<Person>persons=personMapper.selectAll();
		List<EnrollInfo>enrollInfos=enrollInfoMapper.selectAll();
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



	@Override
	public List<EnrollInfo> selectAll() {
		// TODO Auto-generated method stub
		return enrollInfoMapper.selectAll();
	}



	@Override
	public List<EnrollInfo> selectByEnrollId(int enrollId) {
		// TODO Auto-generated method stub
		return enrollInfoMapper.selectByEnrollId(enrollId);
	}



	@Override
	public int updateByEnrollIdAndBackupNum(String signatures, int enrollId,
			int backupnum) {
		// TODO Auto-generated method stub
		return enrollInfoMapper.updateByEnrollIdAndBackupNum(signatures, enrollId, backupnum);
	}



	@Override
	public int deleteByEnrollId(Integer id) {
		// TODO Auto-generated method stub
		return enrollInfoMapper.deleteByEnrollId(id);
	}

	
	
}
