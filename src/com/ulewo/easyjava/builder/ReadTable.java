/**
 * Project Name:jettyTest
 * File Name:ReadTable.java
 * Package Name:com.createproject
 * Date:2016年4月8日下午4:34:35
 * Copyright (c) 2016, ulewo.com All Rights Reserved.
 *
*/

package com.ulewo.easyjava.builder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ulewo.easyjava.bean.ColumnInfo;
import com.ulewo.easyjava.bean.DataTableInfo;
import com.ulewo.easyjava.utils.Constants;
import com.ulewo.easyjava.utils.PropertiesUtils;

/**
 * 
 * ClassName: ReadTable
 * date: 2016年4月11日 上午10:07:42 
 * @author luohaili
 * @version 
 * @since JDK 1.7
 */
public class ReadTable {

	private static Connection conn = null;

	private static final String AUTO_INCREMENT = "auto_increment";

	private static final String KEY_PRIMARY = "PRI";

	private static final String KEY_UNIQUE = "UNI";

	private static final String type_char = "char";

	private static final String type_date = "date";

	private static final String type_timestamp = "timestamp";

	private static final String type_int = "int";

	private static final String type_bigint = "bigint";

	private static final String type_text = "text";

	private static final String type_bit = "bit";

	private static final String type_decimal = "decimal";

	private static final String type_blob = "blob";

	private static final String type_double = "double";

	/**
	 * 显示所有表信息
	 */
	private static final String SQL_SHOW_TABLE_STATUS = "show table status";

	/**
	 * 查询表字段
	 */
	private static final String SQL_PREFIX_SHOW_FIELDS = "show full fields from ";

	static {
		String driverName = PropertiesUtils.getString("db.driver.name");
		String url = PropertiesUtils.getString("db.url");
		String user = PropertiesUtils.getString("db.username");
		String password = PropertiesUtils.getString("db.password");
		try {
			Class.forName(driverName);
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<DataTableInfo> readTable() {
		List<DataTableInfo> tableList = new ArrayList<DataTableInfo>();
		PreparedStatement pstate = null;
		ResultSet results = null;
		ResultSet clumnResult = null;
		try {
			pstate = conn.prepareStatement(SQL_SHOW_TABLE_STATUS);
			results = pstate.executeQuery();
			//获取表信息
			while (results.next()) {
				String tableName = results.getString("NAME");
				String comment = results.getString("COMMENT");
				DataTableInfo info = new DataTableInfo();
				info.setTableName(tableName);
				String beanName = processField(tableName);
				beanName = beanName.substring(0, 1).toUpperCase() + beanName.substring(1);
				info.setBeanName(beanName);
				info.setBeanParamName(beanName + Constants.SUFFIX_BEAN_PARAM);
				info.setComment(comment);
				List<ColumnInfo> columnList = new ArrayList<ColumnInfo>();
				info.setColumnList(columnList);
				tableList.add(info);
				//获取字段信息
				pstate = conn.prepareStatement(SQL_PREFIX_SHOW_FIELDS + tableName);
				clumnResult = pstate.executeQuery();
				while (clumnResult.next()) {
					ColumnInfo columnInfo = new ColumnInfo();
					columnInfo.setColumnName(clumnResult.getString("FIELD"));
					columnInfo.setPropertyName(processField(clumnResult.getString("FIELD")));
					columnInfo.setType(processType(clumnResult.getString("TYPE")));
					columnInfo.setComment(clumnResult.getString("COMMENT"));
					//判断是否是自增长
					if (AUTO_INCREMENT.equals(clumnResult.getString("EXTRA"))) {
						columnInfo.setIsAutoIncrement(true);
					} else {
						columnInfo.setIsAutoIncrement(false);
					}
					//判断是否是PRIMARY Key
					if (KEY_PRIMARY.equals(clumnResult.getString("Key"))) {
						columnInfo.setIsPrimaryKey(true);
					} else if (KEY_UNIQUE.equals(clumnResult.getString("Key"))) {
						columnInfo.setUniqueKey(true);
					}
					columnList.add(columnInfo);
				}
				//这里对字段排序下，PRIMARY KEY 的字段排在前面，不然生成的mapper.xml 会有问题。resultMap中 <id column=""> 必须在前面，否则报错
				Collections.sort(columnList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != clumnResult) {
				try {
					clumnResult.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (null != results) {
				try {
					results.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return tableList;
	}

	private static String processField(String field) {
		StringBuffer sb = new StringBuffer(field.length());
		String[] fields = field.toLowerCase().split("_");
		String temp = null;
		sb.append(fields[0]);
		for (int i = 1, len = fields.length; i < len; i++) {
			temp = fields[i].trim();
			sb.append(temp.substring(0, 1).toUpperCase()).append(temp.substring(1));
		}
		return sb.toString();
	}

	/**
	 * 转换类型
	 * processType:(这里用一句话描述这个方法的作用). <br/>
	 *
	 * @author luohaili
	 * @param type
	 * @return
	 * @since JDK 1.7
	 */
	private static String processType(String type) {
		if (type.indexOf(type_char) > -1) {
			return "String";
		} else if (type.indexOf(type_bigint) > -1) {
			return "Long";
		} else if (type.indexOf(type_int) > -1) {
			return "Integer";
		} else if (type.indexOf(type_double) > -1) {
			return "Double";
		} else if (type.indexOf(type_date) > -1) {
			return "java.util.Date";
		} else if (type.indexOf(type_text) > -1) {
			return "String";
		} else if (type.indexOf(type_timestamp) > -1) {
			return "java.util.Date";
		} else if (type.indexOf(type_bit) > -1) {
			return "Boolean";
		} else if (type.indexOf(type_decimal) > -1) {
			return "java.math.BigDecimal";
		} else if (type.indexOf(type_blob) > -1) {
			return "byte[]";
		}
		return null;
	}
}
