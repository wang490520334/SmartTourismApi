package iii.org.tw.util;

import iii.org.tw.entity.Tour;

import java.sql.ResultSet;
import java.sql.SQLException;



public class tourexception {
	public static ResultSet check(ResultSet result) throws SQLException, NotFoundException
	{
		
		if(result.first()){
			result.previous();
			return result;
		}
			
		else
			throw new NotFoundException("No such data");
	}
}
