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
public class BuildServiceImpl {

	public static void buildServiceImpl(DataTableInfo tableInfo) {

		File folder = new File(Constants.PATH_SERVICE_IMPL);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File beanFile = new File(Constants.PATH_SERVICE_IMPL,
				tableInfo.getBeanName() + Constants.SUFFIX_SERVICE_IMPL + ".java");
		OutputStream out = null;
		OutputStreamWriter outw = null;
		BufferedWriter bw = null;

		try {
			out = new FileOutputStream(beanFile);
			outw = new OutputStreamWriter(out, "utf-8");
			bw = new BufferedWriter(outw);
			bw.write("package " + Constants.PACKAGE_SERVICE_IMPL + ";");
			bw.newLine();
			bw.newLine();
			bw.write("import java.util.List;");
			bw.newLine();
			bw.newLine();
			bw.write("import javax.annotation.Resource;");
			bw.newLine();
			bw.newLine();
			bw.write("import org.springframework.stereotype.Service;");
			bw.newLine();
			bw.newLine();
			bw.write("import " + Constants.PACKAGE_ENUMS + ".PageSize;");
			bw.newLine();
			bw.write("import " + Constants.PACKAGE_PARAM + "." + tableInfo.getBeanParamName() + ";");
			bw.newLine();
			bw.write("import " + Constants.PACKAGE_BEAN + "." + tableInfo.getBeanName() + ";");
			bw.newLine();
			bw.write("import " + Constants.PACKAGE_VO + ".PaginationResult;");
			bw.newLine();
			bw.write("import " + Constants.PACKAGE_VO + ".SimplePage;");
			bw.newLine();
			bw.write("import " + Constants.PACKAGE_MAPPER + "." + tableInfo.getBeanName() + Constants.SUFFIX_MAPPER
					+ ";");
			bw.newLine();
			bw.write("import " + Constants.PACKAGE_SERVICE + "." + tableInfo.getBeanName() + Constants.SUFFIX_SERVICE
					+ ";");
			bw.newLine();

			bw = BuildComment.buildClassComment(bw, tableInfo.getComment() + " 业务接口实现");
			bw.newLine();
			String anServiceBean = tableInfo.getBeanName() + Constants.SUFFIX_SERVICE;
			anServiceBean = anServiceBean.substring(0, 1).toLowerCase() + anServiceBean.substring(1);
			bw.write("@Service(\"" + anServiceBean + "\")");
			bw.newLine();
			bw.write("public class " + tableInfo.getBeanName() + Constants.SUFFIX_SERVICE_IMPL + " implements "
					+ tableInfo.getBeanName() + Constants.SUFFIX_SERVICE + " {");
			bw.newLine();
			bw.newLine();
			bw.write("\t@Resource");
			bw.newLine();

			String beanName = tableInfo.getBeanName();
			String paramMapper = beanName + Constants.SUFFIX_MAPPER;
			paramMapper = paramMapper.substring(0, 1).toLowerCase() + paramMapper.substring(1);
			bw.write("\tprivate " + tableInfo.getBeanName() + Constants.SUFFIX_MAPPER + "<" + tableInfo.getBeanName()
					+ "," + tableInfo.getBeanParamName() + "> " + paramMapper + ";");
			bw.newLine();

			//根据条件查询列表
			bw = BuildComment.buildMethodComment(bw, "根据条件查询列表");
			bw.newLine();
			bw.write("\tpublic List<" + beanName + "> findListByParam(" + tableInfo.getBeanParamName() + " param) {");
			bw.newLine();
			bw.write("\t\tList<" + beanName + "> list = this." + paramMapper + ".selectList(param);");
			bw.newLine();
			bw.write("\t\treturn list;");
			bw.newLine();
			bw.write("\t}");
			bw.newLine();

			//根据条件查询记录数
			bw = BuildComment.buildMethodComment(bw, "根据条件查询列表");
			bw.newLine();
			bw.write("\tpublic Integer findCountByParam(" + tableInfo.getBeanParamName() + " param) {");
			bw.newLine();
			bw.write("\t\tInteger count = this." + paramMapper + ".selectCount(param);");
			bw.newLine();
			bw.write("\t\treturn count;");
			bw.newLine();
			bw.write("\t}");
			bw.newLine();

			//分页查询的方法
			bw = BuildComment.buildMethodComment(bw, "分页查询方法");
			bw.newLine();
			bw.write("\tpublic PaginationResult<" + beanName + "> findListByPage(" + tableInfo.getBeanParamName()
					+ " param) {");
			bw.newLine();
			bw.write("\t\tint count = this." + paramMapper + ".selectCount(param);");
			bw.newLine();
			bw.write("\t\tint pageSize = param.getPageSize()==null?PageSize.SIZE15.getSize():param.getPageSize();");
			bw.newLine();
			bw.write("\t\tint pageNo = 0;");
			bw.newLine();

			bw.write("\t\tif (null != param.getPageNo()) {");
			bw.newLine();
			bw.write("\t\t\tpageNo=param.getPageNo();");
			bw.newLine();
			bw.write("\t\t}");
			bw.newLine();
			bw.write("\t\tSimplePage page = new SimplePage(pageNo, count, pageSize);");
			bw.newLine();
			bw.write("\t\tparam.setPage(page);");
			bw.newLine();
			bw.write("\t\tList<" + beanName + "> list = this." + paramMapper + ".selectList(param);");
			bw.newLine();
			bw.write("\t\tPaginationResult<" + beanName + "> result = new PaginationResult<" + beanName
					+ ">(page, list);");
			bw.newLine();
			bw.write("\t\treturn result;");
			bw.newLine();
			bw.write("\t}");
			bw.newLine();

			//新增
			bw = BuildComment.buildMethodComment(bw, "新增");
			bw.newLine();
			bw.write("\tpublic Integer add(" + tableInfo.getBeanName() + " bean){");
			bw.newLine();
			bw.write("\t\treturn this." + paramMapper + ".insert(bean);");
			bw.newLine();
			bw.write("\t}");
			bw.newLine();

			//批量新增
			bw = BuildComment.buildMethodComment(bw, "批量新增");
			bw.newLine();
			bw.write("\tpublic Integer addBatch(List<" + tableInfo.getBeanName() + "> listBean){");
			bw.newLine();
			bw.write("\t\tif (listBean == null || listBean.isEmpty()) {");
			bw.newLine();
			bw.write("\t\t\treturn 0;");
			bw.newLine();
			bw.write("\t\t}");
			bw.newLine();
			bw.write("\t\treturn this." + paramMapper + ".insertBatch(listBean);");
			bw.newLine();
			bw.write("\t}");
			bw.newLine();

			List<ColumnInfo> columnList = tableInfo.getColumnList();
			StringBuffer paramStr = new StringBuffer();
			StringBuffer paramStrValue = new StringBuffer();
			int index = 0;
			for (ColumnInfo column : columnList) {
				if (column.getIsPrimaryKey() != null && column.getIsPrimaryKey()) {
					if (index > 0) {
						paramStr.append(",");
						paramStrValue.append(",");
					}
					paramStr.append(column.getType() + " " + column.getPropertyName() + "");
					paramStrValue.append(column.getPropertyName());
					index++;
				}

			}
			if (paramStr.length() > 0) {

				//修改
				bw = BuildComment.buildMethodComment(bw, "修改");
				bw.newLine();
				bw.write("\tpublic Integer updateByPrimaryKey(" + tableInfo.getBeanName() + " bean,"
						+ paramStr.toString() + "){");
				bw.newLine();
				bw.write("\t\treturn this." + paramMapper + ".updateByPrimaryKey(bean," + paramStrValue + ");");
				bw.newLine();
				bw.write("\t}");
				bw.newLine();

				//删除
				bw = BuildComment.buildMethodComment(bw, "删除");
				bw.newLine();
				bw.write("\tpublic Integer deleteByPrimaryKey(" + paramStr.toString() + "){");
				bw.newLine();
				bw.write("\t\treturn this." + paramMapper + ".deleteByPrimaryKey(" + paramStrValue + ");");
				bw.newLine();
				bw.write("\t}");
				bw.newLine();

				//更具primaryKey获取
				BuildComment.buildMethodComment(bw, "根据primaryKey获取对象");
				bw.newLine();
				bw.write("\tpublic " + tableInfo.getBeanName() + " get" + tableInfo.getBeanName() + "ByPrimaryKey("
						+ paramStr.toString() + "){");
				bw.newLine();
				bw.write("\t\treturn this." + paramMapper + ".selectByPrimaryKey(" + paramStrValue + ");");
				bw.newLine();
				bw.write("\t}");
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
