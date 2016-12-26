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
public class BuildMapper {

	public static void buildMapper(DataTableInfo tableInfo) {

		File folder = new File(Constants.PATH_MAPPER);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File beanFile = new File(Constants.PATH_MAPPER, tableInfo.getBeanName() + Constants.SUFFIX_MAPPER + ".java");
		OutputStream out = null;
		OutputStreamWriter outw = null;
		BufferedWriter bw = null;

		try {
			out = new FileOutputStream(beanFile);
			outw = new OutputStreamWriter(out, "utf-8");
			bw = new BufferedWriter(outw);
			bw.write("package " + Constants.PACKAGE_MAPPER + ";");
			bw.newLine();
			bw.newLine();
			bw.write("import org.apache.ibatis.annotations.Param;");
			bw = BuildComment.buildClassComment(bw, tableInfo.getComment() + " 数据库操作接口");
			bw.newLine();
			bw.write("public interface " + tableInfo.getBeanName() + Constants.SUFFIX_MAPPER + "<T,P> extends Base"
					+ Constants.SUFFIX_MAPPER + "<T,P> {");
			bw.newLine();

			List<ColumnInfo> columnList = tableInfo.getColumnList();
			StringBuffer paramStr = new StringBuffer();
			int index = 0;
			for (ColumnInfo column : columnList) {
				if (column.getIsPrimaryKey() != null && column.getIsPrimaryKey()) {
					if (index > 0) {
						paramStr.append(",");
					}
					paramStr.append("@Param(\"" + column.getPropertyName() + "\") " + column.getType() + " "
							+ column.getPropertyName() + "");
					index++;
				}

			}
			if (paramStr.length() > 0) {

				BuildComment.buildMethodComment(bw, "根据primaryKey更新");
				bw.newLine();
				bw.write("\t public Integer updateByPrimaryKey(@Param(\"bean\") T t," + paramStr.toString() + ");");
				bw.newLine();
				bw.newLine();

				BuildComment.buildMethodComment(bw, "根据primaryKey删除");
				bw.newLine();
				bw.write("\t public Integer deleteByPrimaryKey(" + paramStr.toString() + ");");
				bw.newLine();
				bw.newLine();

				BuildComment.buildMethodComment(bw, "根据primaryKey获取对象");
				bw.newLine();
				bw.write("\t public T selectByPrimaryKey(" + paramStr.toString() + ");");

				bw.newLine();
				bw.newLine();
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
