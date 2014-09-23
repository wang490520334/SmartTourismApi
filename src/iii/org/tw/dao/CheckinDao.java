package iii.org.tw.dao;

import iii.org.tw.entity.Checkin;
import iii.org.tw.entity.UserToken;
import iii.org.tw.util.DbUtil;
import iii.org.tw.util.SmartTourismException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CheckinDao extends CommonDao {

	protected static CheckinDao singleCheckinDao;

	private CheckinDao() {

	}

	public static CheckinDao getInstance() {
		if (singleCheckinDao == null) {
			singleCheckinDao = new CheckinDao();
		}
		return singleCheckinDao;
	}

	public List<Map<String, Object>> getCheckins(String userKey) throws SmartTourismException {

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		List<Map<String, Object>> list = null;

		UserToken userToken = null;

		try {

			conn = DbUtil.getConnection();
			preparedStatement = conn.prepareStatement("select * from my_check_in where USER_ID = ?");
			preparedStatement.setString(1, userKey);

			ResultSet resultSet = preparedStatement.executeQuery();
			
			list = transferResultSetSetToList(resultSet);

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
		

		return list;
	}
	
	


	
	public List<Map<String, Object>> getCheckinByRelatedId(String userKey, int type, String relatedId) throws SmartTourismException {

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		List<Map<String, Object>> list = null;

		UserToken userToken = null;

		try {

			conn = DbUtil.getConnection();
			preparedStatement = conn.prepareStatement("select * from my_check_in where USER_ID = ? and POCKET_TYPE = ? and RELATED_ID = ?");
			preparedStatement.setString(1, userKey);
			preparedStatement.setInt(2, type);
			preparedStatement.setString(3, relatedId);

			ResultSet resultSet = preparedStatement.executeQuery();
			
			list = transferResultSetSetToList(resultSet);

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
		

		return list;
	}
	

	
	public void saveCheckin (Checkin checkin) throws SmartTourismException {
		
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		
		try {

			conn = DbUtil.getConnection();
			preparedStatement = conn.prepareStatement("insert into my_check_in (UUID,API_KEY,USER_ID,POCKET_TYPE,RELATED_ID) values (?,?,?,?,?)");
			preparedStatement.setString(1, UUID.randomUUID().toString());
			preparedStatement.setString(2, null);
			preparedStatement.setString(3, checkin.getUserId());
			preparedStatement.setInt(4, checkin.getPocketType());
			preparedStatement.setString(5, checkin.getRelateId());

			boolean success = preparedStatement.execute();

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
