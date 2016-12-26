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
public class BuildService {

	public static void buildService(DataTableInfo tableInfo) {

		File folder = new File(Constants.PATH_SERVICE);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File beanFile = new File(Constants.PATH_SERVICE, tableInfo.getBeanName() + Constants.SUFFIX_SERVICE + ".java");
		OutputStream out = null;
		OutputStreamWriter outw = null;
		BufferedWriter bw = null;

		try {
			out = new FileOutputStream(beanFile);
			outw = new OutputStreamWriter(out, "utf-8");
			bw = new BufferedWriter(outw);
			bw.write("package " + Constants.PACKAGE_SERVICE + ";");
			bw.newLine();
			bw.newLine();
			bw.write("import java.util.List;");
			bw.newLine();
			bw.newLine();
			bw.write("import " + Constants.PACKAGE_PARAM + "." + tableInfo.getBeanParamName() + ";");
			bw.newLine();
			bw.write("import " + Constants.PACKAGE_BEAN + "." + tableInfo.getBeanName() + ";");
			bw.newLine();
			bw.write("import " + Constants.PACKAGE_VO + ".PaginationResult;");
			bw.newLine();

			//所有属性
			List<ColumnInfo> columnList = tableInfo.getColumnList();

			StringBuffer paramStr = new StringBuffer();
			int index = 0;
			for (ColumnInfo column : columnList) {
				if (column.getIsPrimaryKey() != null && column.getIsPrimaryKey()) {
					if (index > 0) {
						paramStr.append(",");
					}
					paramStr.append(column.getType() + " " + column.getPropertyName());
					index++;
				}

			}

			bw = BuildComment.buildClassComment(bw, tableInfo.getComment() + " 业务接口");
			bw.newLine();
			bw.write("public interface " + tableInfo.getBeanName() + Constants.SUFFIX_SERVICE + " {");
			bw.newLine();

			String beanName = tableInfo.getBeanName();

			//根据条件查询列表
			bw = BuildComment.buildMethodComment(bw, "根据条件查询列表");
			bw.newLine();
			bw.write("\tpublic List<" + beanName + "> findListByParam(" + tableInfo.getBeanParamName() + " param);");
			bw.newLine();
			//根据条件查询数量
			bw = BuildComment.buildMethodComment(bw, "根据条件查询列表");
			bw.newLine();
			bw.write("\tpublic Integer findCountByParam(" + tableInfo.getBeanParamName() + " param);");
			bw.newLine();
			//分页查询的方法
			bw = BuildComment.buildMethodComment(bw, "分页查询");
			bw.newLine();
			bw.write("\tpublic PaginationResult<" + beanName + "> findListByPage(" + tableInfo.getBeanParamName()
					+ " param);");
			bw.newLine();
			//新增的方法
			bw = BuildComment.buildMethodComment(bw, "新增");
			bw.newLine();
			bw.write("\tpublic Integer add(" + tableInfo.getBeanName() + " bean);");
			bw.newLine();
			//批量新增的方法
			bw = BuildComment.buildMethodComment(bw, "批量新增");
			bw.newLine();
			bw.write("\tpublic Integer addBatch(List<" + tableInfo.getBeanName() + "> listBean);");
			bw.newLine();

			if (paramStr.length() > 0) {
				//修改的方法
				bw = BuildComment.buildMethodComment(bw, "根据主键修改");
				bw.newLine();
				bw.write("\tpublic Integer updateByPrimaryKey(" + tableInfo.getBeanName() + " bean  ,"
						+ paramStr.toString() + ");");
				bw.newLine();
				bw.newLine();
				//根据删除
				bw = BuildComment.buildMethodComment(bw, "根据主键删除");
				bw.newLine();
				bw.write("\tpublic Integer deleteByPrimaryKey(" + paramStr.toString() + ");");
				bw.newLine();
				bw.newLine();

				BuildComment.buildMethodComment(bw, "根据primaryKey获取对象");
				bw.newLine();
				bw.write("\t public " + tableInfo.getBeanName() + " get" + tableInfo.getBeanName() + "ByPrimaryKey("
						+ paramStr.toString() + ");");

				bw.newLine();
				bw.newLine();
			}
			bw.write("}");
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
