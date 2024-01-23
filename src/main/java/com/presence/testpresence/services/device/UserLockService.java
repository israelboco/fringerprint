package com.presence.testpresence.services.device;


import com.presence.testpresence.model.entities.UserLock;

public interface UserLockService {

	
	void setUserLock(UserLock userLock, String starTime, String endTime);

}
