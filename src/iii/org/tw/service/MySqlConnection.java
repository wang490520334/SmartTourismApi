package iii.org.tw.service;

import java.sql.*;

public class MySqlConnection
{
	private final String userName = "ari";
	private final String passWord = "ari36993";
	private final String db = "jdbc:mysql://140.92.88.65/smart_tourism";
	
	private Connection connection;
	private Statement statement;
	private ResultSet result;
	//create the dabase connection.
	public MySqlConnection()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(db, userName, passWord);
			if(connection.isClosed())
				System.out.println("Failed Database Connection");
		}catch(Exception e){}
	}
	
	public void reviseQuery(String request) throws SQLException
	{
		statement = connection.createStatement();
		statement.executeUpdate(request);
		statement.close();
	}
	
	public ResultSet searchQuery(String request) throws SQLException
	{
		statement = connection.createStatement();
		result = statement.executeQuery(request); 
		return result;
	}
	//close the database connection.
	public void close() throws SQLException
	{
		connection.close();
	}
}