package iii.org.tw.dao;

import iii.org.tw.util.SmartTourismException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonDao {
	
	
	public static List<Map<String, Object>> transferResultSetSetToList(ResultSet resultSet) throws SmartTourismException {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {

			ResultSetMetaData rsMeta = resultSet.getMetaData();
			int columnCount = rsMeta.getColumnCount();
			// Map rowData;
			while (resultSet.next()) {
				// rowData = new HashMap(columnCount);
				Map<String, Object> rowData = new HashMap<String, Object>();
				for (int i = 0; i < columnCount; i++) {
					rowData.put(rsMeta.getColumnName(i+1).toUpperCase(), resultSet.getObject(i + 1));
				}
				list.add(rowData);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new SmartTourismException("transferResultSetSetToList邏輯發生錯誤，請告知系統管理員");
		}

		return list;
	}
}
