package iii.org.tw.dao;

import iii.org.tw.entity.UserToken;
import iii.org.tw.util.DbUtil;
import iii.org.tw.util.SmartTourismException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserTokenDao {
	
	protected static UserTokenDao singleUserTokenDao;

	private UserTokenDao () {
		
	}
	
	public static UserTokenDao getInstance() {
		if (singleUserTokenDao == null) {
			singleUserTokenDao = new UserTokenDao();
		}
		return singleUserTokenDao;
	}
	
	
	public UserToken findOneByTokenId(String tokenId) throws SmartTourismException{
		//Connection conn = null;
	    //PreparedStatement preparedStatement = null;
	    
	    UserToken userToken = null;
	    
	    try {
    		Connection conn = DbUtil.getConnection();
    	    PreparedStatement preparedStatement = conn.prepareStatement("select UUID,USER_TOKEN,USER_ID,LOGIN_TIME from user_token where USER_TOKEN = ?");
	    	
			preparedStatement.setString(1, tokenId);

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				userToken = new UserToken();
				userToken.setUuid(resultSet.getString("UUID"));
				userToken.setUserToken(resultSet.getString("USER_TOKEN"));
				userToken.setUserId(resultSet.getString("USER_ID"));
				userToken.setLoginTime(resultSet.getString("LOGIN_TIME"));
	        }

			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			throw new SmartTourismException("資料庫錯誤，請洽系統管理員");
			
		}
	    
	    return userToken;
	    
	}
	
	
	public int deleteByTokenId(String tokenId) throws SmartTourismException{
		Connection conn = null;
	    PreparedStatement preparedStatement = null;
	    
	    UserToken userToken = null;
	    
	    try {
	
			conn = DbUtil.getConnection();
			preparedStatement = conn.prepareStatement("delete from user_token where USER_TOKEN = ?");
			preparedStatement.setString(1, tokenId);

			int count = preparedStatement.executeUpdate();

			return count;
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			throw new SmartTourismException("資料庫錯誤，請洽系統管理員");
			
		} finally {		
			try {
				preparedStatement.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	    
	    
	}
	
	
	public int deleteByUserId(String userId) throws SmartTourismException{
		Connection conn = null;
	    PreparedStatement preparedStatement = null;
	    
	    
	    try {
	
			conn = DbUtil.getConnection();
			preparedStatement = conn.prepareStatement("delete from user_token where USER_ID= ?");
			preparedStatement.setString(1, userId);

			int count = preparedStatement.executeUpdate();

			return count;
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			throw new SmartTourismException("資料庫錯誤，請洽系統管理員");
			
		} finally {		
			try {
				preparedStatement.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	    
	    
	}
	
	
}
