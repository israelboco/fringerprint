package com.presence.testpresence.mapper;

import com.presence.testpresence.model.entities.EnrollInfo;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface EnrollInfoMapper {
 

    int deleteByPrimaryKey(Integer id);

    int deleteByEnrollId(Integer id);

    int insertSelective(EnrollInfo record);

    int insertEnrollInfo(EnrollInfo record);

    EnrollInfo selectByPrimaryKey(Integer id);
    
    int updateByPrimaryKeySelective(EnrollInfo record);

    int updateByPrimaryKeyWithBLOBs(EnrollInfo record);

    
    int insert(@Param("enrollId")int enrollid, @Param("backupnum")int backupnum, @Param("imagePath")String imagePath, @Param("signatures")String signature);
   
    EnrollInfo selectByBackupnum(@Param("enrollId")int enrollId,@Param("backupnum")int backupnum);
       
    List<EnrollInfo> selectAll();
    
    List<EnrollInfo> selectByEnrollId(int enrollId);
    
    int updateByEnrollIdAndBackupNum(@Param("signatures")String signatures,@Param("enrollId")int enrollId,@Param("backupnum")int backupnum);
}