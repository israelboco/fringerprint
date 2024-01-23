package com.presence.testpresence.services.device;

import java.util.List;

import com.presence.testpresence.model.entities.EnrollInfo;
import com.presence.testpresence.model.entities.UserInfo;


public interface EnrollInfoService {

	
	int deleteByPrimaryKey(Integer id);
	
	int deleteByEnrollId(Integer id);

	int insert(int enrollid,int backupnum,String imagPath,String signature);

    int insertSelective(EnrollInfo record);

   

    EnrollInfo selectByPrimaryKey(Integer id);
    
   
    int updateByPrimaryKeySelective(EnrollInfo record);

    int updateByPrimaryKeyWithBLOBs(EnrollInfo record);
    
    EnrollInfo selectByBackupnum(int enrollId,int backupnum);
    
    List<UserInfo> usersToSendDevice();
    
    List<EnrollInfo> selectAll();
    
    List<EnrollInfo> selectByEnrollId(int enrollId);
    int updateByEnrollIdAndBackupNum(String signatures,int enrollId,int backupnum);
}
