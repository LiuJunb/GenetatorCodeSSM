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
public class BuildBean {

	public static void buildEntityBean(DataTableInfo tableInfo) {

		File folder = new File(Constants.PATH_BEAN);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File beanFile = new File(Constants.PATH_BEAN, tableInfo.getBeanName() + ".java");
		OutputStream out = null;
		OutputStreamWriter outw = null;
		BufferedWriter bw = null;

		try {
			out = new FileOutputStream(beanFile);
			outw = new OutputStreamWriter(out, "utf-8");
			bw = new BufferedWriter(outw);
			bw.write("package " + Constants.PACKAGE_BEAN + ";");
			bw.newLine();
			bw.newLine();
			bw.write("import java.io.Serializable;");
			bw.newLine();
			bw = BuildComment.buildClassComment(bw, tableInfo.getComment());
			bw.newLine();
			bw.write("@SuppressWarnings(\"serial\")");
			bw.newLine();
			bw.write("public class " + tableInfo.getBeanName() + " implements Serializable {");
			bw.newLine();
			bw.newLine();
			List<ColumnInfo> columnList = tableInfo.getColumnList();

			for (ColumnInfo columnInfo : columnList) {
				BuildComment.buildPropertyComment(bw, columnInfo.getComment());
				bw.newLine();
				bw.write("\tprivate " + columnInfo.getType() + " " + columnInfo.getPropertyName() + ";");
				bw.newLine();
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
			}

			bw.newLine();
			//重写toString方法
			bw.write("\tpublic String toString (){");
			StringBuilder tostringStr = new StringBuilder();
			for (ColumnInfo columnInfo : columnList) {
				String properName = columnInfo.getComment();
				properName = (properName == null || "".equals(properName)) ? columnInfo.getPropertyName() : properName;
				String clumn = columnInfo.getPropertyName();
				clumn = "(" + clumn + " == null ? \"空\" : " + clumn + ")";
				tostringStr.append("\"，" + properName + ":\"+" + clumn + "+");
			}

			String strResult = "\"" + tostringStr.substring(2, tostringStr.length() - 1) + ";";

			bw.newLine();
			bw.write("\t\treturn " + strResult);
			bw.newLine();
			bw.write("\t}");

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

	@Override
	public String toString() {

		// TODO Auto-generated method stub
		return super.toString();
	}

}
