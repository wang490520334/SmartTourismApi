package iii.org.tw.dao;

import iii.org.tw.entity.Comment;
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

public class CommentDao extends CommonDao {

	protected static CommentDao singleCommentDao;

	private CommentDao() {

	}

	public static CommentDao getInstance() {
		if (singleCommentDao == null) {
			singleCommentDao = new CommentDao();
		}
		return singleCommentDao;
	}

	public List<Map<String, Object>> getCommentsByPlaceId(int type, String placeId) throws SmartTourismException {
		
		
		
		
		List<Map<String, Object>> list = null;
		
		try (
	    		Connection conn = DbUtil.getConnection();
	    	    PreparedStatement preparedStatement = conn.prepareStatement("select * from comments where TYPE = ? and PLACE_ID = ? order by TIME DESC");				
	    ){
	    	
			preparedStatement.setInt(1, type);
			preparedStatement.setString(2, placeId);

			ResultSet resultSet = preparedStatement.executeQuery();
			
			list = transferResultSetSetToList(resultSet);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new SmartTourismException("資料庫錯誤，請洽系統管理員");
			
		}

		return list;

	}

	
	public List<Map<String, Object>> getCommentByCommentId(String commentId) throws SmartTourismException {
		
		List<Map<String, Object>> list = null;
		
		try (
	    		Connection conn = DbUtil.getConnection();
	    	    PreparedStatement preparedStatement = conn.prepareStatement("select * from comments where COMMENT_ID = ?");				
	    ){
	    	
			preparedStatement.setString(1, commentId);

			ResultSet resultSet = preparedStatement.executeQuery();
			
			list = transferResultSetSetToList(resultSet);

			
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new SmartTourismException("資料庫錯誤，請洽系統管理員");
			
		}

		return list;
	}
	
	
	public String saveComment (Comment comment) throws SmartTourismException {
		
		String uuid = null;
		
		try (
	    		Connection conn = DbUtil.getConnection();
	    	    PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO comments(Comment_Id, Place_Id, Api_Key, User_Id, Comment, Picture, Time, Type) VALUES (?,?,?,?,?,?,?,?)");				
	    ){
	    	
			uuid = UUID.randomUUID().toString();
			
			preparedStatement.setString(1, uuid);
			preparedStatement.setString(2, comment.getPlaceId());
			preparedStatement.setString(3, null);
			preparedStatement.setString(4, comment.getUserId());
			preparedStatement.setString(5, comment.getComment());
			preparedStatement.setString(6, comment.getPicture());
			preparedStatement.setTimestamp(7, comment.getTimestamp());
			preparedStatement.setInt(8, comment.getType());

			boolean success = preparedStatement.execute();

			return uuid;
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			throw new SmartTourismException("資料庫錯誤，請洽系統管理員");
			
		}
		

		
		
		
	}
	
	
	public int deleteCommentByCommentId(String CommentId) throws SmartTourismException {
		
		int count = 0;
		
		try (
	    		Connection conn = DbUtil.getConnection();
	    	    PreparedStatement preparedStatement = conn.prepareStatement("delete from comments where COMMENT_ID = ?");				
	    ){	    				
			preparedStatement.setString(1, CommentId);
			count = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			throw new SmartTourismException("資料庫錯誤，請洽系統管理員");
			
		}
		
		return count;
	}



}
