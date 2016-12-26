package com.ulewo.easyjava.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityUtil {

	private final String type_char = "char";

	private final String type_date = "date";

	private final String type_timestamp = "timestamp";

	private final String type_int = "int";

	private final String type_bigint = "bigint";

	private final String type_text = "text";

	private final String type_bit = "bit";

	private final String type_decimal = "decimal";

	private final String type_blob = "blob";

	private final String type_double = "double";

	private final String type_varchar = "varchar";

	private final String moduleName = "group";

	private final String base_dir = "C:/Users/1/Desktop/mappers/";
	private final String bean_path = base_dir + "entity_bean";

	private final String mapper_path = base_dir + "entity_mapper";

	private final String service_path = base_dir + "entity_service";

	private final String controller_path = base_dir + "entity_controller";

	private final String xml_path = base_dir + "entity_mapper/xml";

	private final String bean_package = "com.huajun.web.lsms.po";

	private final String mapper_package = "com.huajun.web.lsms.persistence";

	private final String service_package = "com.huajun.web.lsms.domain.service";

	private final String controller_package = "com.huajun.web.lsms.domain.controller";

	private final String driverName = "com.mysql.jdbc.Driver";

	private final String user = "root";

	private final String password = "root";

	private final String url = "jdbc:mysql://localhost:3306/wallet_data?useUnicode=true&amp;characterEncoding=utf8";

	private String tableName = null;

	private String beanName = null;

	private String mapperName = null;

	private String serviceName = null;

	private String controllerName = null;

	private Connection conn = null;

	/**
	 * 定义基本的 column_list 名称 resultMap名称
	 */
	private final String Base_Column_List = "base_column_list ";

	private final String BASE_RESULT_MAP = "base_result_map";

	private final String BASE_CONDITION = "base_condition";

	private void init() throws ClassNotFoundException, SQLException {
		Class.forName(driverName);
		conn = DriverManager.getConnection(url, user, password);
	}

	/**
	 *  获取所有的表
	 *
	 * @return
	 * @throws SQLException 
	 */
	private List<String> getTables() throws SQLException {
		List<String> tables = new ArrayList<String>();
		PreparedStatement pstate = conn.prepareStatement("show tables");
		ResultSet results = pstate.executeQuery();
		while (results.next()) {
			String tableName = results.getString(1);
			//          if ( tableName.toLowerCase().startsWith("yy_") ) {
			tables.add(tableName);
			//          }
		}
		return tables;
	}

	private void processTable(String table) {
		StringBuffer sb = new StringBuffer(table.length());
		String tableNew = table.toLowerCase();
		String[] tables = tableNew.split("_");
		String temp = null;
		for (int i = 0; i < tables.length; i++) {
			temp = tables[i].trim();
			sb.append(temp.substring(0, 1).toUpperCase()).append(temp.substring(1));
		}
		beanName = sb.toString();
		mapperName = beanName + "Mapper";
		serviceName = beanName + "Service";
		controllerName = beanName + "Controller";
	}

	private String processType(String type) {
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

	private String processField(String field) {
		StringBuffer sb = new StringBuffer(field.length());
		//field = field.toLowerCase();
		String[] fields = field.toLowerCase().split("_");
		String temp = null;
		sb.append(fields[0]);
		for (int i = 1; i < fields.length; i++) {
			temp = fields[i].trim();
			sb.append(temp.substring(0, 1).toUpperCase()).append(temp.substring(1));
		}
		return sb.toString();
	}

	/**
	 *  将实体类名首字母改为小写
	 *
	 * @param beanName
	 * @return 
	 */
	private String processResultMapId(String beanName) {
		return beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
	}

	/**
	 *  构建类上面的注释
	 *
	 * @param bw
	 * @param text
	 * @return
	 * @throws IOException 
	 */
	private BufferedWriter buildClassComment(BufferedWriter bw, String text) throws IOException {
		bw.newLine();
		bw.newLine();
		bw.write("/**");
		bw.newLine();
		bw.write(" * ");
		bw.newLine();
		bw.write(" * " + text);
		bw.newLine();
		bw.write(" * ");
		bw.newLine();
		bw.write(" **/");
		return bw;
	}

	/**
	 *  构建方法上面的注释
	 *
	 * @param bw
	 * @param text
	 * @return
	 * @throws IOException 
	 */
	private BufferedWriter buildMethodComment(BufferedWriter bw, String text) throws IOException {
		bw.newLine();
		bw.write("\t/**");
		bw.newLine();
		bw.write("\t * ");
		bw.newLine();
		bw.write("\t * " + text);
		bw.newLine();
		bw.write("\t * ");
		bw.newLine();
		bw.write("\t **/");
		return bw;
	}

	/**
	 *  生成实体类
	 *
	 * @param columns
	 * @param types
	 * @param comments
	 * @throws IOException 
	 */
	private void buildEntityBean(List<String> columns, List<String> types, List<String> comments, String tableComment)
			throws IOException {
		File folder = new File(bean_path);
		if (!folder.exists()) {
			folder.mkdir();
		}

		File beanFile = new File(bean_path, beanName + ".java");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(beanFile)));
		bw.write("package " + bean_package + ";");
		bw.newLine();
		bw.write("import java.io.Serializable;");
		bw.newLine();
		//bw.write("import lombok.Data;");
		//      bw.write("import javax.persistence.Entity;");
		bw = buildClassComment(bw, tableComment);
		bw.newLine();
		bw.write("@SuppressWarnings(\"serial\")");
		bw.newLine();
		//      bw.write("@Entity");
		//bw.write("@Data");
		//bw.newLine();
		bw.write("public class " + beanName + " implements Serializable {");
		bw.newLine();
		bw.newLine();
		int size = columns.size();
		for (int i = 0; i < size; i++) {
			bw.write("\t/**" + comments.get(i) + "**/");
			bw.newLine();
			bw.write("\tprivate " + processType(types.get(i)) + " " + processField(columns.get(i)) + ";");
			bw.newLine();
			bw.newLine();
		}
		bw.newLine();
		// 生成get 和 set方法
		String tempField = null;
		String _tempField = null;
		String tempType = null;
		for (int i = 0; i < size; i++) {
			tempType = processType(types.get(i));
			_tempField = processField(columns.get(i));
			tempField = _tempField.substring(0, 1).toUpperCase() + _tempField.substring(1);
			bw.newLine();
			//          bw.write("\tpublic void set" + tempField + "(" + tempType + " _" + _tempField + "){");
			bw.write("\tpublic void set" + tempField + "(" + tempType + " " + _tempField + "){");
			bw.newLine();
			//          bw.write("\t\tthis." + _tempField + "=_" + _tempField + ";");
			bw.write("\t\tthis." + _tempField + " = " + _tempField + ";");
			bw.newLine();
			bw.write("\t}");
			bw.newLine();
			bw.newLine();
			bw.write("\tpublic " + tempType + " get" + tempField + "(){");
			bw.newLine();
			bw.write("\t\treturn this." + _tempField + ";");
			bw.newLine();
			bw.write("\t}");
			bw.newLine();
		}
		bw.newLine();
		bw.write("}");
		bw.newLine();
		bw.flush();
		bw.close();
	}

	/**
	 *  构建Mapper文件
	 *
	 * @throws IOException 
	 */
	private void buildMapper() throws IOException {
		File folder = new File(mapper_path);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File mapperFile = new File(mapper_path, mapperName + ".java");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperFile), "utf-8"));
		bw.write("package " + mapper_package + ";");
		bw.newLine();
		bw.newLine();
		//  bw.write("import org.apache.ibatis.annotations.Param;");
		bw = buildClassComment(bw, mapperName + "数据库操作接口类");
		bw.newLine();
		bw.newLine();
		//      bw.write("public interface " + mapperName + " extends " + mapper_extends + "<" + beanName + "> {");
		bw.write("public interface " + mapperName + "<T> extends BaseMapper<T>{");
		bw.newLine();
		bw.newLine();
		// ----------定义Mapper中的方法End----------
		bw.newLine();
		bw.write("}");
		bw.flush();
		bw.close();
	}

	private void buildService() throws IOException {
		File folder = new File(service_path);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File mapperFile = new File(service_path, serviceName + ".java");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperFile), "utf-8"));
		bw.write("package " + service_package + ";");
		bw.newLine();
		bw.newLine();
		bw.write("import  java.util.List;");
		bw.newLine();
		bw.write("import java.util.Map;");
		bw.newLine();
		bw.newLine();
		bw.write("import javax.annotation.Resource;");
		bw.newLine();
		bw.newLine();
		bw.write("import org.springframework.stereotype.Service;");
		bw.newLine();
		bw.newLine();
		bw.write("import com.huajun.framework.core.enums.PageSize;");
		bw.newLine();
		bw.write("import com.huajun.tools.utils.StringTools;");
		bw.newLine();
		bw.write("import " + mapper_package + "." + mapperName + ";");
		bw.newLine();
		bw.write("import " + bean_package + "." + beanName + ";");
		bw.newLine();
		bw.write("import com.huajun.web.lsms.vo.PaginationResult;");
		bw.newLine();
		bw.write("import com.huajun.web.lsms.vo.SimplePage;");
		bw.newLine();
		//  bw.write("import org.apache.ibatis.annotations.Param;");
		bw = buildClassComment(bw, serviceName + "业务方法类");
		bw.newLine();
		bw.newLine();
		//      bw.write("public interface " + mapperName + " extends " + mapper_extends + "<" + beanName + "> {");
		bw.write("@Service");
		bw.newLine();
		bw.write("public class " + serviceName + "{");
		bw.newLine();
		bw.write("\t@Resource");
		bw.newLine();
		String chumMapperName = mapperName.substring(0, 1).toLowerCase() + mapperName.substring(1);
		bw.write("\tprivate " + mapperName + "<" + beanName + "> " + chumMapperName + ";");
		//分页查询的方法
		bw = buildClassComment(bw, "分页查询方法");
		bw.newLine();
		bw.write("\tpublic PaginationResult<" + beanName + "> findListByPage(Map<String,Object> param) {");
		bw.newLine();
		bw.write("\t\tint count = this." + chumMapperName + ".selectCount(param);");
		bw.newLine();
		bw.write("\t\tint pageSize = PageSize.SIZE15.getSize();");
		bw.newLine();
		bw.write("\t\tint pageNo = 0;");
		bw.newLine();
		bw.write("\t\tif (null != param.get(\"pageNo\")&& StringTools.isNumber(param.get(\"pageNo\").toString())) {");
		bw.newLine();
		bw.write("\t\t\tpageNo = Integer.parseInt(param.get(\"pageNo\").toString());");
		bw.newLine();
		bw.write("\t\t}");
		bw.newLine();
		bw.write("\t\tSimplePage page = new SimplePage(pageNo, count, pageSize);");
		bw.newLine();
		bw.write("\t\tparam.put(\"page\",page);");
		bw.newLine();
		bw.write("\t\tList<" + beanName + "> list = this." + chumMapperName + ".selectList(param);");
		bw.newLine();
		bw.write("\t\tPaginationResult<" + beanName + "> result = new PaginationResult<" + beanName + ">(page, list);");
		bw.newLine();
		bw.write("\t\treturn result;");
		bw.newLine();
		bw.write("\t}");
		bw.newLine();
		bw.write("}");
		bw.flush();
		bw.close();
	}

	private void buildController() throws IOException {
		File folder = new File(controller_path);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File mapperFile = new File(controller_path, controllerName + ".java");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperFile), "utf-8"));
		bw.write("package " + controller_package + ";");
		bw.newLine();
		bw.newLine();
		bw.write("import " + bean_package + "." + beanName + ";");
		bw.newLine();
		bw.write("import java.util.List;");
		bw.newLine();
		bw.write("import java.util.Map;");
		bw.newLine();
		bw.write("import javax.annotation.Resource;");
		bw.newLine();
		bw.write("import javax.servlet.http.HttpServletRequest;");
		bw.newLine();
		bw.write("import javax.servlet.http.HttpSession;");
		bw.newLine();
		bw.newLine();
		bw.write("import org.slf4j.Logger;");
		bw.newLine();
		bw.write("import org.slf4j.LoggerFactory;");
		bw.newLine();
		bw.write("import org.springframework.stereotype.Controller;");
		bw.newLine();
		bw.write("import org.springframework.web.bind.annotation.RequestMapping;");
		bw.newLine();
		bw.write("import org.springframework.web.bind.annotation.ResponseBody;");
		bw.newLine();
		bw.write("import org.springframework.web.servlet.ModelAndView;");
		bw.newLine();
		bw.write("import com.huajun.web.lsms.vo.AjaxResponse;");
		bw.newLine();
		bw.write("import com.huajun.framework.core.enums.ResponseCode;");
		bw.newLine();
		bw.write("import com.huajun.web.lsms.exception.BusinessException;");
		bw.newLine();
		bw.write("import com.huajun.web.lsms.vo.PaginationResult;");
		bw.newLine();
		bw.newLine();
		bw.write("import " + service_package + "." + serviceName + ";");
		bw.newLine();
		//  bw.write("import org.apache.ibatis.annotations.Param;");
		bw = buildClassComment(bw, controllerName + "contoller方法类");
		bw.newLine();
		bw.newLine();
		//      bw.write("public interface " + mapperName + " extends " + mapper_extends + "<" + beanName + "> {");
		bw.write("@Controller");
		bw.newLine();
		bw.write("@RequestMapping(value = \"/" + beanName.toLowerCase() + "\")");
		bw.newLine();
		bw.write("public class " + controllerName + " extends BaseController{");
		bw.newLine();
		bw.write("\tLogger logger = LoggerFactory.getLogger(" + controllerName + ".class);");
		bw.newLine();
		bw.write("\t@Resource");
		bw.newLine();
		String chumServiceName = serviceName.substring(0, 1).toLowerCase() + serviceName.substring(1);
		bw.write("\tprivate " + serviceName + " " + chumServiceName + ";");
		bw.newLine();
		bw.newLine();
		bw.write("\t\t/**分页查询**/");
		bw.newLine();
		bw.write("\t@RequestMapping(value = \"/" + beanName.toLowerCase() + "_list.do\")");
		bw.newLine();
		bw.write("\tpublic ModelAndView " + beanName.toLowerCase()
				+ "_list(HttpSession session,HttpServletRequest request) {");
		bw.newLine();
		bw.write("\t\tModelAndView view = new ModelAndView(\"/" + beanName.toLowerCase() + "/" + beanName.toLowerCase()
				+ "_list\");");
		bw.newLine();
		bw.write("\t\tMap<String, Object> param = this.builderParams(request, false);");
		bw.newLine();
		bw.write("\t\tPaginationResult<" + beanName + "> result = this." + chumServiceName + ".findListByPage(param);");
		bw.newLine();
		bw.write("\t\tview.addObject(\"result\", result);");
		bw.newLine();
		bw.write("\t\tview.addObject(\"requestParam\", param);");
		bw.newLine();
		bw.write("\t\treturn view;");
		bw.newLine();
		bw.write("\t}");
		bw.newLine();
		bw.newLine();

		bw.write("\t\t/**编辑**/");
		bw.newLine();
		bw.write("\t@RequestMapping(value = \"/" + beanName.toLowerCase() + "_edit.do\")");
		bw.newLine();
		bw.write("\tpublic ModelAndView " + beanName.toLowerCase()
				+ "_edit(HttpSession session,HttpServletRequest request) {");
		bw.newLine();
		bw.write("\t\tModelAndView view = new ModelAndView(\"/" + beanName.toLowerCase() + "/" + beanName.toLowerCase()
				+ "_edit\");");
		bw.newLine();
		bw.write("\t\treturn view;");
		bw.newLine();
		bw.write("\t}");
		bw.newLine();
		bw.newLine();

		bw.write("\t\t/**保存**/");
		bw.newLine();
		bw.write("\t@ResponseBody");
		bw.newLine();
		bw.write("\t@RequestMapping(value = \"/" + beanName.toLowerCase() + "_save.do\")");
		bw.newLine();
		bw.write("\tpublic AjaxResponse<?> " + beanName.toLowerCase() + "_save(HttpSession session, " + beanName + " "
				+ beanName.toLowerCase() + "){");
		bw.newLine();
		bw.write("\t\tAjaxResponse<Object> result = new AjaxResponse<Object>();");
		bw.newLine();
		bw.write("\t\tresult.setResponseCode(ResponseCode.SUCCESS);");
		bw.newLine();
		bw.write("\t\ttry {");
		bw.newLine();
		bw.write("\t\t\tthrow new BusinessException(\"xx\");");
		bw.newLine();
		bw.write("\t\t} catch (BusinessException e) {");
		bw.newLine();
		bw.write("\t\t\tresult.setResponseCode(ResponseCode.BUSINESSERROR);");
		bw.newLine();
		bw.write("\t\t\tresult.setErrorMsg(e.getMessage());");
		bw.newLine();
		bw.write("\t\t\tlogger.error(e.getMessage());");
		bw.newLine();
		bw.write("\t\t} catch (Exception e) {");
		bw.newLine();
		bw.write("\t\t\tresult.setResponseCode(ResponseCode.SERVERERROR);");
		bw.newLine();
		bw.write("\t\t\tlogger.error(e.getMessage());");
		bw.newLine();
		bw.write("\t\t}");
		bw.newLine();
		bw.write("\t\treturn result;");
		bw.newLine();
		bw.write("\t}");
		bw.newLine();
		bw.newLine();

		bw.write("\t\t/**详情**/");
		bw.newLine();
		bw.write("\t\t@RequestMapping(value = \"/" + beanName.toLowerCase() + "_detail.do\")");
		bw.newLine();
		bw.write("\t\tpublic ModelAndView " + beanName.toLowerCase()
				+ "_detail(HttpSession session, HttpServletRequest request) {");
		bw.newLine();
		bw.write("\t\t\tModelAndView view = new ModelAndView(\"/" + beanName.toLowerCase() + "/"
				+ beanName.toLowerCase() + "_detail\");");
		bw.newLine();
		bw.write("\t\t\tMap<String, Object> param = this.builderParams(request, false);");
		bw.newLine();
		bw.write("\t\t\treturn view;");
		bw.newLine();
		bw.write("\t\t}");

		//分页查询的方法
		bw.newLine();
		bw.write("}");
		bw.flush();
		bw.close();
	}

	/**
	 *  构建实体类映射XML文件
	 *
	 * @param columns
	 * @param types
	 * @param comments
	 * @throws IOException 
	 */
	private void buildMapperXml(List<String> columns, List<String> types, List<String> comments) throws IOException {
		File folder = new File(xml_path);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		File mapperXmlFile = new File(xml_path, mapperName + ".xml");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperXmlFile)));
		bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		bw.newLine();
		bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" ");
		bw.newLine();
		bw.write("    \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
		bw.newLine();
		bw.write("<mapper namespace=\"" + mapper_package + "." + mapperName + "\">");
		bw.newLine();
		bw.newLine();

		bw.write("\t<!--实体映射-->");
		bw.newLine();
		bw.write("\t<resultMap id=\"" + BASE_RESULT_MAP + "\" type=\"" + bean_package + "." + beanName + "\">");
		bw.newLine();
		bw.write("\t\t<!--" + comments.get(0) + "-->");
		bw.newLine();
		bw.write("\t\t<id property=\"" + this.processField(columns.get(0)) + "\" column=\"" + columns.get(0) + "\" />");
		bw.newLine();
		int size = columns.size();
		for (int i = 1; i < size; i++) {
			bw.write("\t\t<!--" + comments.get(i) + "-->");
			bw.newLine();
			bw.write("\t\t<result property=\"" + this.processField(columns.get(i)) + "\" column=\"" + columns.get(i)
					+ "\" />");
			bw.newLine();
		}
		bw.write("\t</resultMap>");

		bw.newLine();
		bw.newLine();
		bw.newLine();

		// 下面开始写SqlMapper中的方法
		// this.outputSqlMapperMethod(bw, columns, types);
		buildSQL(bw, columns, types);

		bw.write("</mapper>");
		bw.flush();
		bw.close();
	}

	private void buildSQL(BufferedWriter bw, List<String> columns, List<String> types) throws IOException {
		int size = columns.size();
		// 通用结果列
		bw.write("\t<!-- 通用查询结果列-->");
		bw.newLine();
		bw.write("\t<sql id=\"" + Base_Column_List + "\">");
		bw.newLine();
		bw.write("\t\t ");
		for (int i = 0; i < size; i++) {
			bw.write(columns.get(i));
			if (i != size - 1) {
				bw.write(",");
			}
		}
		bw.newLine();
		bw.write("\t</sql>");
		bw.newLine();
		bw.newLine();

		//生成where条件

		// 通用结果列
		bw.write("\t<!-- 通用查询条件列-->");
		bw.newLine();
		bw.write("\t<sql id=\"" + BASE_CONDITION + "\">");
		bw.newLine();
		bw.write("\t <where>");
		bw.newLine();
		String tempField = null;
		//   bw.write("\t\t id,");
		tempField = null;
		for (int i = 0; i < size; i++) {
			tempField = processField(columns.get(i));
			bw.write("\t\t\t<if test=\"" + tempField + " != null");
			if (types.get(i).contains(type_char)) {
				bw.write(" and " + tempField + "!=''");
			}
			bw.write("\">");
			bw.newLine();
			bw.write("\t\t\t\t and  " + columns.get(i) + " = #{" + tempField + "}");
			bw.newLine();
			bw.write("\t\t\t</if>");
			bw.newLine();

			if (types.get(i).contains(type_char)) {
				bw.write("\t\t\t<if test=\"" + tempField + "Fuzzy != null  and " + tempField + "Fuzzy!=''\">");
				bw.newLine();
				bw.write("\t\t\t\t and  " + columns.get(i) + " like '%${" + tempField + "Fuzzy}%'");
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

		//生成查询条件完毕
		// 查询（根据主键ID查询）
		/*bw.write("\t<!-- 查询（根据主键ID查询） -->");
		bw.newLine();
		bw.write("\t<select id=\"selectByPrimaryKey\" resultMap=\"" + BASE_RESULT_MAP + "\" parameterType=\"java.lang."
				+ processType(types.get(0)) + "\">");
		bw.newLine();
		bw.write("\t\t SELECT");
		bw.newLine();
		bw.write("\t\t <include refid=\"" + Base_Column_List + "\" />");
		bw.newLine();
		bw.write("\t\t FROM " + tableName);
		bw.newLine();
		bw.write("\t\t WHERE " + columns.get(0) + " = #{" + processField(columns.get(0)) + "}");
		bw.newLine();
		bw.write("\t</select>");
		bw.newLine();
		bw.newLine();*/
		// 查询完

		// 删除（根据主键ID删除）
		bw.write("\t<!--删除：根据条件删除-->");
		bw.newLine();
		bw.write("\t<delete id=\"delete\" parameterType=\"java.lang." + processType(types.get(0)) + "\">");
		bw.newLine();
		bw.write("\t\t DELETE FROM " + tableName);
		bw.write(" <include refid=\"" + BASE_CONDITION + "\" />");
		bw.newLine();
		bw.write("\t</delete>");
		bw.newLine();
		bw.newLine();
		// 删除完

		/*// 添加insert方法
		bw.write("\t<!-- 添加 -->");
		bw.newLine();
		bw.write("\t<insert id=\"insert\" parameterType=\"" + bean_package + "." + beanName + "\">");
		bw.newLine();
		bw.write("\t\t INSERT INTO " + tableName);
		bw.newLine();
		bw.write(" \t\t(");
		for (int i = 0; i < size; i++) {
			bw.write(columns.get(i));
			if (i != size - 1) {
				bw.write(",");
			}
		}
		bw.write(") ");
		bw.newLine();
		bw.write("\t\t VALUES ");
		bw.newLine();
		bw.write(" \t\t(");
		for (int i = 0; i < size; i++) {
			bw.write("#{" + processField(columns.get(i)) + "}");
			if (i != size - 1) {
				bw.write(",");
			}
		}
		bw.write(") ");
		bw.newLine();
		bw.write("\t</insert>");
		bw.newLine();
		bw.newLine();*/
		// 添加insert完

		//---------------  insert方法（匹配有值的字段）
		bw.write("\t<!-- 添加 （匹配有值的字段）-->");
		bw.newLine();
		bw.write("\t<insert id=\"insert\" parameterType=\"" + bean_package + "." + beanName + "\">");
		bw.newLine();
		bw.write("\t\t INSERT INTO " + tableName);
		bw.newLine();
		bw.write("\t\t <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >");
		bw.newLine();
		for (int i = 0; i < size; i++) {
			tempField = processField(columns.get(i));
			bw.write("\t\t\t<if test=\"" + tempField + " != null\">");
			bw.newLine();
			bw.write("\t\t\t\t " + columns.get(i) + ",");
			bw.newLine();
			bw.write("\t\t\t</if>");
			bw.newLine();
		}
		bw.write("\t\t </trim>");
		bw.newLine();
		bw.write("\t\t <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >");
		bw.newLine();
		tempField = null;
		for (int i = 0; i < size; i++) {
			tempField = processField(columns.get(i));
			bw.write("\t\t\t<if test=\"" + tempField + "!=null\">");
			bw.newLine();
			bw.write("\t\t\t\t #{" + tempField + "},");
			bw.newLine();
			bw.write("\t\t\t</if>");
			bw.newLine();
		}
		bw.write("\t\t </trim>");
		bw.newLine();
		bw.write("\t</insert>");
		bw.newLine();
		bw.newLine();
		//---------------  完毕

		//批量插入
		bw.write("\t<!-- 添加 （批量插入）-->");
		bw.newLine();
		bw.write("\t\t<insert id=\"insertBatch\" parameterType=\"" + bean_package + "." + beanName + "\">");
		bw.newLine();
		bw.write("\t\t INSERT INTO " + tableName);
		bw.write("(");
		for (int i = 0; i < size; i++) {
			tempField = processField(columns.get(i));
			if (i < size - 1) {
				bw.write(columns.get(i) + ",");
			} else {
				bw.write(columns.get(i));
			}
		}
		bw.write(")values");
		bw.newLine();
		bw.write("\t\t\t <foreach collection=\"list\" item=\"item\" separator=\",\">");
		bw.newLine();
		bw.write("\t\t\t (");
		bw.newLine();
		bw.write("\t\t\t ");
		for (int i = 0; i < size; i++) {
			tempField = processField(columns.get(i));
			if (i < size - 1) {
				bw.write("#{item." + tempField + "},");
			} else {
				bw.write("#{item." + tempField + "}");
			}
		}
		bw.newLine();
		bw.write("\t\t\t )");
		bw.newLine();
		bw.write("\t\t\t </foreach>");
		bw.newLine();
		bw.write("\t\t</insert>");
		bw.newLine();
		bw.newLine();

		// 修改update方法
		bw.write("\t<!-- 修 改-->");
		bw.newLine();
		bw.write("\t<update id=\"update\" parameterType=\"" + bean_package + "." + beanName + "\">");
		bw.newLine();
		bw.write("\t\t UPDATE " + tableName);
		bw.newLine();
		bw.write(" \t\t <set> ");
		bw.newLine();
		tempField = null;
		for (int i = 1; i < size; i++) {
			tempField = processField(columns.get(i));
			bw.write("\t\t\t<if test=\"" + tempField + " != null\">");
			bw.newLine();
			bw.write("\t\t\t\t " + columns.get(i) + " = #{" + tempField + "},");
			bw.newLine();
			bw.write("\t\t\t</if>");
			bw.newLine();
		}
		bw.write(" \t\t </set>");
		bw.newLine();

		bw.write("\t <where>");
		bw.newLine();
		tempField = null;
		//   bw.write("\t\t id,");
		tempField = null;
		tempField = processField(columns.get(0));
		bw.write("\t\t\t<if test=\"" + tempField + " != null");
		if (types.get(0).contains(type_char)) {
			bw.write(" and " + tempField + "!=''");
		}
		bw.write("\">");
		bw.newLine();
		bw.write("\t\t\t\t and  " + columns.get(0) + " = #{" + tempField + "}");
		bw.newLine();
		bw.write("\t\t\t</if>");
		bw.newLine();
		bw.write("\t </where>");
		bw.newLine();
		bw.write("\t</update>");
		bw.newLine();
		bw.newLine();
		// update方法完毕
		/*
				// ----- 修改（匹配有值的字段）
				bw.write("\t<!-- 修 改-->");
				bw.newLine();
				bw.write("\t<update id=\"updateByPrimaryKey\" parameterType=\"" + bean_package + "." + beanName + "\">");
				bw.newLine();
				bw.write("\t\t UPDATE " + tableName);
				bw.newLine();
				bw.write("\t\t SET ");
		
				bw.newLine();
				tempField = null;
				for (int i = 1; i < size; i++) {
					tempField = processField(columns.get(i));
					bw.write("\t\t\t " + columns.get(i) + " = #{" + tempField + "}");
					if (i != size - 1) {
						bw.write(",");
					}
					bw.newLine();
				}
		
				bw.write("\t\t WHERE " + columns.get(0) + " = #{" + processField(columns.get(0)) + "}");
				bw.newLine();
				bw.write("\t</update>");
				bw.newLine();
				bw.newLine();*/
		//完毕

		// 查询对象方法
		bw.write("\t<!-- 查询集合-->");
		bw.newLine();
		bw.write("\t<select id=\"selectList\" resultMap=\"" + BASE_RESULT_MAP + "\" >");
		bw.newLine();
		bw.write("\t\t SELECT");
		bw.write(" <include refid=\"" + Base_Column_List + "\" />");
		bw.write(" FROM " + tableName);
		bw.write(" <include refid=\"" + BASE_CONDITION + "\" />");
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
		bw.write(" FROM " + tableName);
		bw.write(" <include refid=\"" + BASE_CONDITION + "\" />");
		bw.newLine();
		bw.write("\t</select>");
		bw.newLine();
		bw.newLine();
	}

	/**
	 *  获取所有的数据库表注释
	 *
	 * @return
	 * @throws SQLException 
	 */
	private Map<String, String> getTableComment() throws SQLException {
		Map<String, String> maps = new HashMap<String, String>();
		PreparedStatement pstate = conn.prepareStatement("show table status");
		ResultSet results = pstate.executeQuery();
		while (results.next()) {
			String tableName = results.getString("NAME");
			String comment = results.getString("COMMENT");
			maps.put(tableName, comment);
		}
		return maps;
	}

	public void generate() throws ClassNotFoundException, SQLException, IOException {
		init();
		String prefix = "show full fields from ";
		List<String> columns = null;
		List<String> types = null;
		List<String> comments = null;
		PreparedStatement pstate = null;
		List<String> tables = getTables();
		Map<String, String> tableComments = getTableComment();
		for (String table : tables) {
			columns = new ArrayList<String>();
			types = new ArrayList<String>();
			comments = new ArrayList<String>();
			pstate = conn.prepareStatement(prefix + table);
			ResultSet results = pstate.executeQuery();
			while (results.next()) {
				columns.add(results.getString("FIELD"));
				types.add(results.getString("TYPE"));
				comments.add(results.getString("COMMENT"));
			}
			tableName = table;
			processTable(table);
			//          this.outputBaseBean();
			String tableComment = tableComments.get(tableName);
			buildEntityBean(columns, types, comments, tableComment);
			buildMapper();
			buildMapperXml(columns, types, comments);
			buildService();
			buildController();
		}
		conn.close();
	}

	public static void main(String[] args) {
		try {
			new EntityUtil().generate();
			// 自动打开生成文件的目录
			// Runtime.getRuntime().exec("cmd /c start explorer D:\\");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
