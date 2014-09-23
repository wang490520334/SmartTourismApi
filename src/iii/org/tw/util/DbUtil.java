package iii.org.tw.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.cpdsadapter.DriverAdapterCPDS;
import org.apache.commons.dbcp2.datasources.SharedPoolDataSource;

public class DbUtil {

	
	public static ThreadLocal<Connection> localCon = new ThreadLocal<Connection>();
	
	
	private static SharedPoolDataSource ds;
	public static Properties props = new Properties();
	
	
	static {
		
		try {
			props.load(DbUtil.class.getResourceAsStream("/db.properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		if (Boolean.valueOf(props.getProperty("db.connection.production"))) {
			DriverAdapterCPDS cpds = new DriverAdapterCPDS();
	        try {
				cpds.setDriver(props.getProperty("db.driver.name"));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        cpds.setUrl(props.getProperty("db.connection.url"));
	        cpds.setUser(props.getProperty("db.connection.username"));
	        cpds.setPassword(props.getProperty("db.connection.pass"));

	        SharedPoolDataSource poolDataSource = new SharedPoolDataSource();
	        poolDataSource.setConnectionPoolDataSource(cpds);
	        poolDataSource.setMaxTotal(50);
	        poolDataSource.setDefaultMaxWaitMillis(5000);
	        
	        ds = poolDataSource;
		} else {
			try {
				Class.forName(props.getProperty("db.driver.name"));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
        
        


    }

		
	public static Connection getConnection() throws Exception {

		try {

			
			Connection conn = localCon.get();
			

			if (conn == null || conn.isClosed()) {
				if (Boolean.valueOf(props.getProperty("db.connection.production"))) {
					conn = ds.getConnection();
					localCon.set(conn);
					return conn;
				} else {
					conn = DriverManager.getConnection(
							props.getProperty("db.connection.url"),
							props.getProperty("db.connection.username"),
							props.getProperty("db.connection.pass"));
					localCon.set(conn);
					return conn;
				}
			} else {
				return conn;
			}
			
	
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("未取得 Connenction，請確認所有環境與資料庫設定");

		}

	}
	
	
	public static void closeLocalConnection() {
		try {
    	
	    	Connection conn = DbUtil.localCon.get();
	    	if (conn != null && !conn.isClosed()) {
	    		conn.close();
	    	}
    	
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
	}


}
