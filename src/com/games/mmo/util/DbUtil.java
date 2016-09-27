package com.games.mmo.util;

import java.util.ArrayList;
import java.util.List;

import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.util.StringUtil;

public class DbUtil {
	/**
	 * 找出满足该数据库表名和该数据库名的数量
	 * @param tableName
	 * @param dataBaseName
	 * @return
	 */
	public static int getCountByTableAndDbName(String tableName, String... dataBaseName) {
		if (dataBaseName.length == 0)
			return 0;
		int result = 0;
		String sql = new String("select count(table_name) as result from information_schema.tables where table_name='"+tableName+"'" + " AND TABLE_SCHEMA in (");
		StringBuilder s = new StringBuilder();
		boolean frist = true;
		for (String string : dataBaseName) {
			if (frist)
				frist = false;
			else
				s.append(",");
			s.append("'");
			s.append(string);
			s.append("'");
		}
		sql += s + ")";
		result = BaseDAO.instance().queryIntForSql(sql,null);
		return result;
	}
	
	/**
	 * 找出所有数据库该表名的数量
	 * @param tableName
	 * @return
	 */
	public static int getCountByTableName(String tableName) {
		int result = 0;
		String sql = new String("select count(table_name) as result from information_schema.tables where table_name='"+tableName+"'");
		result = BaseDAO.instance().queryIntForSql(sql,null);
		return result;
	}
	
	/**
	 * 找出满足该数据库表名和该数据库名的表名
	 * @param tableNames
	 * @param dataBaseName
	 * @return
	 */
	public static List<String> getTableNameByTableAndDbName(String tableNames, String dataBaseName) {
		if(StringUtil.isEmpty(tableNames) || StringUtil.isEmpty(dataBaseName)) return new ArrayList<String>();
		String sql = "select table_name from information_schema.tables where table_name in("+tableNames+")" + " AND TABLE_SCHEMA = '" + dataBaseName + "'";
		List<String> result = BaseDAO.instance().jdbcTemplate.queryForList(sql, String.class);
		return result;
	}
}
