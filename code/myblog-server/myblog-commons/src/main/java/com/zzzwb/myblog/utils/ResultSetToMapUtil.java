package com.zzzwb.myblog.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JDBC结果集封装到map的工具类
 *
 * @author zeng wenbin
 * @date Created in 2019/7/17
 */
public final class ResultSetToMapUtil {

	/**
	 * 工具类不能实例化
	 */
	private ResultSetToMapUtil(){}

	/**
	 * 封装数据到ListList<Map<String,Object>>
	 *
	 * @param resultSet jdbc结果集
	 * @return ListList<Map<String,Object>>
	 * @throws SQLException
	 */
	public static List<Map<String,Object>> packageResultSet(ResultSet resultSet) throws SQLException {
		List<Map<String, Object>> datas = new ArrayList<>();
		ResultSetMetaData metaData = resultSet.getMetaData();
		int columnCount = metaData.getColumnCount();
		while (resultSet.next()) {
			Map<String, Object> map = new HashMap<>(15);
			for (int i = 1; i <= columnCount; i++) {
				map.put(metaData.getColumnName(i), resultSet.getObject(i));
			}
			datas.add(map);
		}
		return datas;
	}
}
