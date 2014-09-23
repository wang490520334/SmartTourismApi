package iii.org.tw.dao;

import iii.org.tw.entity.Pocket;
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

public class PocketDao extends CommonDao {

	protected static PocketDao singlePocketDao;

	private PocketDao() {

	}

	public static PocketDao getInstance() {
		if (singlePocketDao == null) {
			singlePocketDao = new PocketDao();
		}
		return singlePocketDao;
	}

	public List<Map<String, Object>> getPockets(String userKey) throws SmartTourismException {

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		List<Map<String, Object>> list = null;

		UserToken userToken = null;

		try {

			conn = DbUtil.getConnection();
			preparedStatement = conn.prepareStatement("select * from my_pocket where USER_ID = ?");
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
	
	
	public List<Map<String, Object>> getPocketsByType(String userKey, int type) throws SmartTourismException {

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		List<Map<String, Object>> list = null;

		UserToken userToken = null;

		try {

			conn = DbUtil.getConnection();
			preparedStatement = conn.prepareStatement("select * from my_pocket where USER_ID = ? and POCKET_TYPE = ?");
			preparedStatement.setString(1, userKey);
			preparedStatement.setInt(2, type);

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

	
	public List<Map<String, Object>> getPocketByRelatedId(String userKey, int type, String relatedId) throws SmartTourismException {

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		List<Map<String, Object>> list = null;

		UserToken userToken = null;

		try {

			conn = DbUtil.getConnection();
			preparedStatement = conn.prepareStatement("select * from my_pocket where USER_ID = ? and POCKET_TYPE = ? and RELATED_ID = ?");
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
	
	public List<Map<String, Object>> getPocketById(String id) throws SmartTourismException {

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		List<Map<String, Object>> list = null;

		try {

			conn = DbUtil.getConnection();
			preparedStatement = conn.prepareStatement("select * from my_pocket where Uuid = ? ");
			preparedStatement.setString(1, id);


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
	
	
	public String savePocket (Pocket pocket) throws SmartTourismException {
		
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		
		String uuid = null;
		
		try {

			uuid = UUID.randomUUID().toString();
			
			conn = DbUtil.getConnection();
			preparedStatement = conn.prepareStatement("insert into my_pocket (UUID,API_KEY,USER_ID,POCKET_TYPE,RELATED_ID) values (?,?,?,?,?)");
			preparedStatement.setString(1, uuid);
			preparedStatement.setString(2, null);
			preparedStatement.setString(3, pocket.getUserId());
			preparedStatement.setInt(4, pocket.getPocketType());
			preparedStatement.setString(5, pocket.getRelateId());

			boolean success = preparedStatement.execute();
			
			return uuid;

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
	
	
	public int deletePocketById(String id) throws SmartTourismException {

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		int count = 0;

		try {

			conn = DbUtil.getConnection();
			preparedStatement = conn.prepareStatement("delete from my_pocket where UUID = ?");
			preparedStatement.setString(1, id);
			
			count = preparedStatement.executeUpdate();

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
		
		return count;
	}



}
