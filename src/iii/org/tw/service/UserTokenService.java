package iii.org.tw.service;

import iii.org.tw.dao.UserTokenDao;
import iii.org.tw.entity.UserToken;
import iii.org.tw.model.UserModel;
import iii.org.tw.util.SmartTourismException;

public class UserTokenService {
	
	public static String getUserId (String userToken) throws SmartTourismException {
		
		UserTokenDao userTokenDao = UserTokenDao.getInstance();
	    UserToken userTokenEntity = userTokenDao.findOneByTokenId(userToken);
	    
	    if (userTokenEntity == null) {
	    	throw new SmartTourismException("userToken錯誤");
	    }
	    
	    return userTokenEntity.getUserId();
	}
	
	public static UserModel getUserModelByUserId (String userId) throws SmartTourismException {
	
	    UserModel user = LdapService.getUserDataByUserId(userId);
	    
	    return user;
	    
	}
	
	
	public static UserModel getUserModelByToken (String userToken) throws SmartTourismException {
		
		UserTokenDao userTokenDao = UserTokenDao.getInstance();
	    UserToken userTokenEntity = userTokenDao.findOneByTokenId(userToken);
	    
	    if (userTokenEntity == null) {
	    	throw new SmartTourismException("userToken錯誤");
	    }
	    
	    UserModel user = LdapService.getUserDataByUserId(userTokenEntity.getUserId());
	    
	    return user;
	    
	}

}
