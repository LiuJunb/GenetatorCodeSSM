/**
 * Project Name:CreateProject
 * File Name:CreateBean.java
 * Package Name:com.createproject
 * Date:2016年4月11日上午11:26:08
 * Copyright (c) 2016, stnts.com All Rights Reserved.
 *
*/

package com.ulewo.easyjava.builder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import com.ulewo.easyjava.bean.ColumnInfo;
import com.ulewo.easyjava.bean.DataTableInfo;
import com.ulewo.easyjava.utils.Constants;

/**
 * ClassName:CreateBean <br/>
 * Date:     2016年4月11日 上午11:26:08 <br/>
 * @author   luohl
 * Copyright (c) 2016, stnts.com All Rights Reserved. 
 */
public class BuildMapperXml {

	private static final String BASE_RESULT_MAP = "base_result_map";

	private static final String BASE_COLUMN_LIST = "base_column_list";

	private static final String BASE_CONDITION = "base_condition";

	private static final String QUERY_CONDITION = "query_condition";

	private static final String BASE_CONDITION_Filed = "base_condition_filed";

	public static void buildMapperXml(DataTableInfo tableInfo) {

		File folder = new File(Constants.PATH_MAPPER_XML);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File beanFile = new File(Constants.PATH_MAPPER_XML,
				tableInfo.getBeanName() + Constants.SUFFIX_MAPPER_XML + ".xml");
		OutputStream out = null;
		OutputStreamWriter outw = null;
		BufferedWriter bw = null;

		String primaryKeyType = "";
		try {
			List<ColumnInfo> columnList = tableInfo.getColumnList();

			out = new FileOutputStream(beanFile);
			outw = new OutputStreamWriter(out, "utf-8");
			bw = new BufferedWriter(outw);
			bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			bw.newLine();
			bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" ");
			bw.newLine();
			bw.write("    \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
			bw.newLine();
			bw.write("<mapper namespace=\"" + Constants.PACKAGE_MAPPER + "." + tableInfo.getBeanName()
					+ Constants.SUFFIX_MAPPER_XML + "\">");
			bw.newLine();
			bw.newLine();
			//实体映射
			bw.write("\t<!--实体映射-->");
			bw.newLine();
			bw.write("\t<resultMap id=\"" + BASE_RESULT_MAP + "\" type=\"" + Constants.PACKAGE_BEAN + "."
					+ tableInfo.getBeanName() + "\">");
			bw.newLine();
			for (ColumnInfo columnInfo : columnList) {
				bw.write("\t\t<!--" + columnInfo.getComment() + "-->");
				bw.newLine();
				//主键
				if (columnInfo.getIsPrimaryKey() != null && columnInfo.getIsPrimaryKey()) {
					bw.write("\t\t<id column=\"" + columnInfo.getColumnName() + "\" property=\""
							+ columnInfo.getPropertyName() + "\"  />");
					primaryKeyType = columnInfo.getType();
				} else {
					bw.write("\t\t<result column=\"" + columnInfo.getColumnName() + "\" property=\""
							+ columnInfo.getPropertyName() + "\"  />");
				}
				bw.newLine();
			}
			bw.write("\t</resultMap>");
			bw.newLine();
			bw.newLine();
			bw.newLine();

			//通用结果集
			bw.write("\t<!-- 通用查询结果列-->");
			bw.newLine();
			bw.write("\t<sql id=\"" + BASE_COLUMN_LIST + "\">");
			bw.newLine();
			bw.write("\t\t ");
			for (int i = 0, _len = columnList.size(); i < _len; i++) {
				bw.write(columnList.get(i).getColumnName());
				if (i != _len - 1) {
					bw.write(",");
				}
			}
			bw.newLine();
			bw.write("\t</sql>");
			bw.newLine();
			bw.newLine();

			//基本查询列
			bw.write("\t<sql id=\"" + BASE_CONDITION_Filed + "\">");
			bw.newLine();
			for (ColumnInfo columnInfo : columnList) {
				bw.write("\t\t\t<if test=\"" + columnInfo.getPropertyName() + " != null");
				if (Constants.TYPE_STRING.equals(columnInfo.getType())) {
					bw.write(" and " + columnInfo.getPropertyName() + "!=''");
				}
				bw.write("\">");
				bw.newLine();
				bw.write("\t\t\t\t and  " + columnInfo.getColumnName() + " = #{" + columnInfo.getPropertyName() + "}");
				bw.newLine();
				bw.write("\t\t\t</if>");
				bw.newLine();
			}
			bw.write("\t</sql>");

			//生成where条件
			bw.newLine();
			bw.write("\t<!-- 通用条件列-->");
			bw.newLine();
			bw.write("\t<sql id=\"" + BASE_CONDITION + "\">");
			bw.newLine();
			bw.write("\t <where>");
			bw.write(" <include refid=\"" + BASE_CONDITION_Filed + "\" />");
			bw.write("\t </where>");
			bw.newLine();
			bw.write("\t</sql>");
			bw.newLine();
			bw.newLine();

			bw.write("\t<!-- 通用查询条件列-->");
			bw.newLine();
			bw.write("\t<sql id=\"" + QUERY_CONDITION + "\">");
			bw.newLine();
			bw.write("\t <where>");
			bw.newLine();
			bw.write("\t\t\t<include refid=\"" + BASE_CONDITION_Filed + "\" />");
			bw.newLine();
			for (ColumnInfo columnInfo : columnList) {
				if (Constants.TYPE_STRING.equals(columnInfo.getType())) {
					bw.write("\t\t\t<if test=\"" + columnInfo.getPropertyName() + Constants.SUFFIX_PROPERTY_FUZZY
							+ "!= null  and " + columnInfo.getPropertyName() + Constants.SUFFIX_PROPERTY_FUZZY
							+ "!=''\">");
					bw.newLine();
					bw.write("\t\t\t\t and  " + columnInfo.getColumnName() + " like concat('%', #{"
							+ columnInfo.getPropertyName() + Constants.SUFFIX_PROPERTY_FUZZY + "}, '%')");
					bw.newLine();
					bw.write("\t\t\t</if>");
					bw.newLine();
				}

				if (Constants.TYPE_DATE.equals(columnInfo.getType())) {
					String start = columnInfo.getPropertyName() + Constants.SUFFIX_BEAN_PARAM_TIME_START;
					String end = columnInfo.getPropertyName() + Constants.SUFFIX_BEAN_PARAM_TIME_END;
					//开始时间
					bw.write("\t\t\t<if test=\"" + start + "!= null and " + end + "!=''\">");
					bw.newLine();
					bw.write("\t\t\t\t <![CDATA[ and  " + columnInfo.getColumnName() + ">=str_to_date('${" + start
							+ "}', '%Y-%m-%d') ]]>");
					bw.newLine();
					bw.write("\t\t\t</if>");
					bw.newLine();

					//结束日期
					bw.write("\t\t\t<if test=\"" + end + "!= null and " + end + "!=''\">");
					bw.newLine();
					bw.write("\t\t\t\t <![CDATA[ and  " + columnInfo.getColumnName() + "< date_sub(str_to_date('${"
							+ end + "}','%Y-%m-%d'),interval -1 day) ]]>");
					bw.newLine();
					bw.write("\t\t\t</if>");
					bw.newLine();
				}
			}
			bw.write("\t </where>");
			bw.newLine();
			bw.write("\t</sql>");
			bw.newLine();
			bw.newLine();

			// 查询对象方法
			bw.write("\t<!-- 查询集合-->");
			bw.newLine();
			bw.write("\t<select id=\"selectList\" resultMap=\"" + BASE_RESULT_MAP + "\" >");
			bw.newLine();
			bw.write("\t\t SELECT");
			bw.write(" <include refid=\"" + BASE_COLUMN_LIST + "\" />");
			bw.write(" FROM " + tableInfo.getTableName());
			bw.write(" <include refid=\"" + QUERY_CONDITION + "\" />");
			bw.newLine();
			bw.write("\t\t <if test=\"orderBy!=null\">");
			bw.newLine();
			bw.write("\t\t\t order by ${orderBy}");
			bw.newLine();
			bw.write("\t\t </if>");
			bw.newLine();
			bw.write("\t\t <if test=\"page!=null\">");
			bw.newLine();
			bw.write("\t\t\t limit #{page.start},#{page.end}");
			bw.newLine();
			bw.write("\t\t </if>");
			bw.newLine();
			bw.write("\t</select>");
			bw.newLine();
			bw.newLine();

			// 查询方法完毕
			bw.write("\t<!-- 查询数量-->");
			bw.newLine();
			bw.write("\t<select id=\"selectCount\" resultType=\"java.lang.Integer\" >");
			bw.newLine();
			bw.write("\t\t SELECT");
			bw.write(" count(1)");
			bw.write(" FROM " + tableInfo.getTableName());
			bw.write(" <include refid=\"" + QUERY_CONDITION + "\" />");
			bw.newLine();
			bw.write("\t</select>");
			bw.newLine();
			bw.newLine();

			//insert方法（匹配有值的字段）

			//获取自增长的属性
			ColumnInfo autoIncrementColumn = null;
			for (ColumnInfo columnInfo : columnList) {
				if (null != columnInfo.getIsAutoIncrement() && columnInfo.getIsAutoIncrement()) {
					autoIncrementColumn = columnInfo;
					break;
				}
			}

			bw.write("\t<!-- 插入 （匹配有值的字段）-->");
			bw.newLine();
			bw.write("\t<insert id=\"insert\" parameterType=\"" + Constants.PACKAGE_BEAN + "." + tableInfo.getBeanName()
					+ "\">");

			bw.newLine();
			if (autoIncrementColumn != null) {
				bw.write("\t\t<selectKey keyProperty=\"" + autoIncrementColumn.getPropertyName() + "\" resultType=\""
						+ autoIncrementColumn.getType() + "\" order=\"AFTER\">");
				bw.newLine();
				bw.write("\t\t\tSELECT LAST_INSERT_ID()");
				bw.newLine();
				bw.write("\t\t</selectKey>");
				bw.newLine();
			}
			bw.write("\t\t INSERT INTO " + tableInfo.getTableName());
			bw.newLine();
			bw.write("\t\t <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >");
			bw.newLine();
			for (ColumnInfo columnInfo : columnList) {
				if (columnInfo.getIsAutoIncrement()) {
					continue;
				}
				bw.write("\t\t\t<if test=\"" + columnInfo.getPropertyName() + " != null\">");
				bw.newLine();
				bw.write("\t\t\t\t " + columnInfo.getColumnName() + ",");
				bw.newLine();
				bw.write("\t\t\t</if>");
				bw.newLine();
			}
			bw.write("\t\t </trim>");
			bw.newLine();
			bw.write("\t\t <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >");
			bw.newLine();
			for (ColumnInfo columnInfo : columnList) {
				if (columnInfo.getIsAutoIncrement()) {
					continue;
				}
				bw.write("\t\t\t<if test=\"" + columnInfo.getPropertyName() + "!=null\">");
				bw.newLine();
				bw.write("\t\t\t\t #{" + columnInfo.getPropertyName() + "},");
				bw.newLine();
				bw.write("\t\t\t</if>");
				bw.newLine();
			}
			bw.write("\t\t </trim>");
			bw.newLine();
			bw.write("\t</insert>");
			bw.newLine();
			bw.newLine();

			bw.write("\t<!-- 插入 （根据表名称插入）-->");
			bw.newLine();
			bw.write("\t<insert id=\"insertWidthTableName\" parameterType=\"" + Constants.PACKAGE_BEAN + "."
					+ tableInfo.getBeanName() + "\">");

			bw.newLine();
			if (autoIncrementColumn != null) {
				bw.write("\t\t<selectKey keyProperty=\"bean." + autoIncrementColumn.getPropertyName()
						+ "\" resultType=\"" + autoIncrementColumn.getType() + "\" order=\"AFTER\">");
				bw.newLine();
				bw.write("\t\t\tSELECT LAST_INSERT_ID()");
				bw.newLine();
				bw.write("\t\t</selectKey>");
				bw.newLine();
			}
			bw.write("\t\t INSERT INTO ${tableName}");
			bw.write("(");
			for (int i = 0, len = columnList.size(); i < len; i++) {
				if (null != columnList.get(i).getIsAutoIncrement() && columnList.get(i).getIsAutoIncrement()) {
					continue;
				}
				bw.write(columnList.get(i).getColumnName());
				if (i != len - 1) {
					bw.write(",");
				}
			}
			bw.write(")values");
			bw.write("(");
			bw.newLine();
			bw.write("\t\t\t ");
			for (int i = 0, len = columnList.size(); i < len; i++) {
				if (null != columnList.get(i).getIsAutoIncrement() && columnList.get(i).getIsAutoIncrement()) {
					continue;
				}
				bw.write("#{bean." + columnList.get(i).getPropertyName() + "}");
				if (i != len - 1) {
					bw.write(",");
				}
			}
			bw.write(")");
			bw.newLine();
			bw.write("\t\tON DUPLICATE KEY UPDATE ");
			bw.newLine();
			bw.write("\t\t");
			for (int i = 0, len = columnList.size(); i < len; i++) {
				ColumnInfo column = columnList.get(i);
				if ((null != column.getIsAutoIncrement() && column.getIsAutoIncrement()) || column.isUniqueKey()) {
					continue;
				}
				String columnName = column.getColumnName();
				bw.write(columnName + " = values(" + columnName + ")");
				if (i != len - 1) {
					bw.write(",");
				}
			}
			bw.newLine();
			bw.write("\t</insert>");
			bw.newLine();
			bw.newLine();

			//批量插入
			bw.write("\t<!-- 添加 （批量插入）-->");
			bw.newLine();
			bw.write("\t\t<insert id=\"insertBatch\" parameterType=\"" + Constants.PACKAGE_BEAN + "."
					+ tableInfo.getBeanName() + "\">");
			bw.newLine();
			bw.write("\t\t INSERT INTO " + tableInfo.getTableName());
			bw.write("(");
			for (int i = 0, len = columnList.size(); i < len; i++) {
				if (null != columnList.get(i).getIsAutoIncrement() && columnList.get(i).getIsAutoIncrement()) {
					continue;
				}
				bw.write(columnList.get(i).getColumnName());
				if (i != len - 1) {
					bw.write(",");
				}
			}
			bw.write(")values");
			bw.newLine();
			bw.write("\t\t\t <foreach collection=\"list\" item=\"item\" separator=\",\">");
			bw.newLine();
			bw.write("\t\t\t (");
			bw.newLine();
			bw.write("\t\t\t ");
			for (int i = 0, len = columnList.size(); i < len; i++) {
				if (null != columnList.get(i).getIsAutoIncrement() && columnList.get(i).getIsAutoIncrement()) {
					continue;
				}
				bw.write("#{item." + columnList.get(i).getPropertyName() + "}");
				if (i != len - 1) {
					bw.write(",");
				}
			}
			bw.newLine();
			bw.write("\t\t\t )");
			bw.newLine();
			bw.write("\t\t\t </foreach>");
			bw.newLine();
			bw.write("\t\tON DUPLICATE KEY UPDATE ");
			bw.newLine();
			bw.write("\t\t");
			for (int i = 0, len = columnList.size(); i < len; i++) {
				ColumnInfo column = columnList.get(i);
				if ((null != column.getIsAutoIncrement() && column.getIsAutoIncrement()) || column.isUniqueKey()) {
					continue;
				}
				String columnName = column.getColumnName();
				bw.write(columnName + " = values(" + columnName + ")");
				if (i != len - 1) {
					bw.write(",");
				}
			}
			bw.newLine();
			bw.write("\t\t</insert>");
			bw.newLine();
			bw.newLine();
			StringBuffer paramStr = new StringBuffer();
			int index = 0;
			for (ColumnInfo column : columnList) {
				if (column.getIsPrimaryKey() != null && column.getIsPrimaryKey()) {
					if (index > 0) {
						paramStr.append(" and ");
					}
					paramStr.append(column.getColumnName() + "=#{" + column.getPropertyName() + "}");
				}
				index++;
			}

			// 修改updateByPrimarykey方法
			if (paramStr.length() > 0) {
				bw.write("\t<!-- 根据PrimaryKey修 改-->");
				bw.newLine();
				bw.write("\t<update id=\"updateByPrimaryKey\" parameterType=\"" + Constants.PACKAGE_BEAN + "."
						+ tableInfo.getBeanName() + "\">");
				bw.newLine();
				bw.write("\t\t UPDATE " + tableInfo.getTableName());
				bw.newLine();
				bw.write(" \t\t <set> ");
				bw.newLine();
				for (ColumnInfo columnInfo : columnList) {
					if (null != columnInfo.getIsPrimaryKey() && columnInfo.getIsPrimaryKey()) {
						continue;
					}
					bw.write("\t\t\t<if test=\"bean." + columnInfo.getPropertyName() + " != null\">");
					bw.newLine();
					bw.write("\t\t\t\t " + columnInfo.getColumnName() + " = #{bean." + columnInfo.getPropertyName()
							+ "},");
					bw.newLine();
					bw.write("\t\t\t</if>");
					bw.newLine();
				}
				bw.write(" \t\t </set>");
				bw.newLine();
				bw.write(" \t\t where " + paramStr);
				bw.newLine();
				bw.write("\t</update>");
				bw.newLine();
				bw.newLine();
			}

			if (paramStr.length() > 0) {
				bw.write("\t<!-- 根据PrimaryKey删除-->");
				bw.newLine();
				bw.write("\t<delete id=\"deleteByPrimaryKey\">");
				bw.newLine();
				bw.write("\t\tdelete from " + tableInfo.getTableName() + " where " + paramStr);
				bw.newLine();
				bw.write("\t</delete>");
				bw.newLine();
				bw.newLine();

				bw.write("\t<!-- 根据PrimaryKey获取对象-->");
				bw.newLine();
				bw.write("\t<select id=\"selectByPrimaryKey\" resultMap=\"" + BASE_RESULT_MAP + "\" >");
				bw.newLine();
				bw.write("\t\tselect <include refid=\"" + BASE_COLUMN_LIST + "\" /> from " + tableInfo.getTableName()
						+ " where " + paramStr);
				bw.newLine();
				bw.write("\t</select>");
				bw.newLine();
				bw.newLine();
			}
			bw.write("</mapper>");
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outw != null) {
				try {
					outw.close();
				} catch (IOException e) {
					e.printStackTrace();

				}
			}
			if (null != bw) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
