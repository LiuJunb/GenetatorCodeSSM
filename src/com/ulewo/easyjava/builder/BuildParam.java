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
public class BuildParam {

	public static void buildEntityParam(DataTableInfo tableInfo) {

		File folder = new File(Constants.PATH_PARAM);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File beanFile = new File(Constants.PATH_PARAM, tableInfo.getBeanName() + Constants.SUFFIX_BEAN_PARAM + ".java");
		OutputStream out = null;
		OutputStreamWriter outw = null;
		BufferedWriter bw = null;

		try {
			out = new FileOutputStream(beanFile);
			outw = new OutputStreamWriter(out, "utf-8");
			bw = new BufferedWriter(outw);
			bw.write("package " + Constants.PACKAGE_PARAM + ";");
			bw.newLine();
			bw.newLine();
			bw = BuildComment.buildClassComment(bw, tableInfo.getComment() + "参数");
			bw.newLine();
			bw.write("public class " + tableInfo.getBeanName() + Constants.SUFFIX_BEAN_PARAM + " extends BaseParam{");
			bw.newLine();
			bw.newLine();
			List<ColumnInfo> columnList = tableInfo.getColumnList();

			for (ColumnInfo columnInfo : columnList) {
				BuildComment.buildPropertyComment(bw, columnInfo.getComment());
				bw.newLine();
				bw.write("\tprivate " + columnInfo.getType() + " " + columnInfo.getPropertyName() + ";");
				bw.newLine();
				//添加模糊搜索条件
				if (Constants.TYPE_STRING.equals(columnInfo.getType())) {
					bw.newLine();
					bw.write("\tprivate " + columnInfo.getType() + " " + columnInfo.getPropertyName()
							+ Constants.SUFFIX_PROPERTY_FUZZY + ";");
					bw.newLine();
				}
				//添加时间日期搜索
				if (Constants.TYPE_DATE.equals(columnInfo.getType())) {
					String start = columnInfo.getPropertyName() + Constants.SUFFIX_BEAN_PARAM_TIME_START;
					String end = columnInfo.getPropertyName() + Constants.SUFFIX_BEAN_PARAM_TIME_END;
					bw.newLine();
					bw.write("\tprivate " + Constants.TYPE_STRING + " " + start + ";");
					bw.newLine();
					bw.newLine();
					bw.write("\tprivate " + Constants.TYPE_STRING + " " + end + ";");
					bw.newLine();
				}
			}
			bw.newLine();
			// 生成get 和 set方法
			String tempField = null;
			for (ColumnInfo columnInfo : columnList) {
				tempField = columnInfo.getPropertyName().substring(0, 1).toUpperCase()
						+ columnInfo.getPropertyName().substring(1);
				bw.newLine();
				bw.write("\tpublic void set" + tempField + "(" + columnInfo.getType() + " "
						+ columnInfo.getPropertyName() + "){");
				bw.newLine();
				bw.write("\t\tthis." + columnInfo.getPropertyName() + " = " + columnInfo.getPropertyName() + ";");
				bw.newLine();
				bw.write("\t}");
				bw.newLine();
				bw.newLine();
				bw.write("\tpublic " + columnInfo.getType() + " get" + tempField + "(){");
				bw.newLine();
				bw.write("\t\treturn this." + columnInfo.getPropertyName() + ";");
				bw.newLine();
				bw.write("\t}");
				bw.newLine();

				if (Constants.TYPE_STRING.equals(columnInfo.getType())) {
					bw.newLine();
					bw.write("\tpublic void set" + tempField + Constants.SUFFIX_PROPERTY_FUZZY + "("
							+ columnInfo.getType() + " " + columnInfo.getPropertyName()
							+ Constants.SUFFIX_PROPERTY_FUZZY + "){");
					bw.newLine();
					bw.write("\t\tthis." + columnInfo.getPropertyName() + Constants.SUFFIX_PROPERTY_FUZZY + " = "
							+ columnInfo.getPropertyName() + Constants.SUFFIX_PROPERTY_FUZZY + ";");
					bw.newLine();
					bw.write("\t}");
					bw.newLine();
					bw.newLine();
					bw.write("\tpublic " + columnInfo.getType() + " get" + tempField + Constants.SUFFIX_PROPERTY_FUZZY
							+ "(){");
					bw.newLine();
					bw.write("\t\treturn this." + columnInfo.getPropertyName() + Constants.SUFFIX_PROPERTY_FUZZY + ";");
					bw.newLine();
					bw.write("\t}");
					bw.newLine();
				}

				if (Constants.TYPE_DATE.equals(columnInfo.getType())) {
					String start = columnInfo.getPropertyName() + Constants.SUFFIX_BEAN_PARAM_TIME_START;
					String end = columnInfo.getPropertyName() + Constants.SUFFIX_BEAN_PARAM_TIME_END;
					tempField = start.substring(0, 1).toUpperCase() + start.substring(1);
					//开始日期
					bw.newLine();
					bw.write("\tpublic void set" + tempField + "(" + Constants.TYPE_STRING + " " + start + "){");
					bw.newLine();
					bw.write("\t\tthis." + start + " = " + start + ";");
					bw.newLine();
					bw.write("\t}");
					bw.newLine();
					bw.newLine();
					bw.write("\tpublic " + Constants.TYPE_STRING + " get" + tempField + "(){");
					bw.newLine();
					bw.write("\t\treturn this." + start + ";");
					bw.newLine();
					bw.write("\t}");

					//结束日期
					tempField = end.substring(0, 1).toUpperCase() + end.substring(1);
					bw.newLine();
					bw.write("\tpublic void set" + tempField + "(" + Constants.TYPE_STRING + " " + end + "){");
					bw.newLine();
					bw.write("\t\tthis." + end + " = " + end + ";");
					bw.newLine();
					bw.write("\t}");
					bw.newLine();
					bw.newLine();
					bw.write("\tpublic " + Constants.TYPE_STRING + " get" + tempField + "(){");
					bw.newLine();
					bw.write("\t\treturn this." + end + ";");
					bw.newLine();
					bw.write("\t}");
					bw.newLine();
				}
			}
			bw.newLine();
			bw.write("}");
			bw.newLine();
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
