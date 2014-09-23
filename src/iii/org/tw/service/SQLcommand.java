package iii.org.tw.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLcommand 
{
	static Connection conn = null;
	static Statement stat;
	
	public void connect_DB(String db_name) throws ClassNotFoundException, SQLException
	{
//		final String DB_URL = "jdbc:mysql://localhost/"+db_name+"?useUnicode=true&characterEncoding=utf8";
//		// Database credentials
//		final String USER = "root";
//		final String PASS = "Simcoe0857";

		final String DB_URL = "jdbc:mysql://140.92.88.65/"+db_name+"?useUnicode=true&characterEncoding=utf8";
		// Database credentials
		final String USER = "ari";
		final String PASS = "ari36993";
		
		// Register JDBC driver
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		System.out.println("get");
	}
	public void modify_table(String query)
	{
		try 
		{
			
			stat = conn.createStatement();
			stat.executeUpdate(query);
			stat.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public ResultSet select_table(String query) throws SQLException
	{
		ResultSet rs;
		stat = conn.createStatement();
		rs = stat.executeQuery(query);
		return rs;
	}
	public int row_count(String table_name) throws SQLException
	{
		ResultSet rs;
		stat = conn.createStatement();
		rs = stat.executeQuery("SELECT count(*) AS A FROM "+table_name+"");
		rs.next();
		return rs.getInt("A");
	}
	public static int row_count_where(String table_name) throws SQLException
	{
		ResultSet rs;
		stat = conn.createStatement();
		rs = stat.executeQuery(table_name);
		rs.next();
		return rs.getInt("A");
	}
	public void close() throws SQLException
	{
		conn.close();
	}
}
