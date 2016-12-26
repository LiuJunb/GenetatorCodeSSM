/**
 * Project Name:CreateProject
 * File Name:PropertyUtil.java
 * Package Name:com.createproject
 * Date:2016年4月11日上午11:28:56
 * Copyright (c) 2016, stnts.com All Rights Reserved.
 *
*/

package com.ulewo.easyjava.utils;

/**
 * ClassName:PropertyUtil <br/>
 * Date:     2016年4月11日 上午11:28:56 <br/>
 * @author   luohl
 * Copyright (c) 2016, stnts.com All Rights Reserved. 
 */
public class Constants {

	/**
	 * 包路径
	 */
	public static String PACKAGE_BASE = null;
	public static String PACKAGE_BEAN = null;
	public static String PACKAGE_PARAM = null;
	public static String PACKAGE_ENUMS = null;
	public static String PACKAGE_VO = null;
	public static String PACKAGE_MAPPER = null;
	public static String PACKAGE_SERVICE = null;
	public static String PACKAGE_SERVICE_IMPL = null;
	public static String PACKAGE_CONTROLLER = null;

	/**
	 * 文件存储路径
	 */
	public static String PATH_BASE = null;

	public static String PATH_BEAN = null;
	public static String PATH_PARAM = null;
	public static String PATH_ENUMS = null;
	public static String PATH_VO = null;
	public static String PATH_MAPPER = null;
	public static String PATH_MAPPER_XML = null;
	public static String PATH_SERVICE = null;
	public static String PATH_SERVICE_IMPL = null;
	public static String PATH_CONTROLLER = null;

	/**
	 * 后缀
	 */
	public static String SUFFIX_MAPPER = null;
	public static String SUFFIX_MAPPER_XML = null;
	public static String SUFFIX_SERVICE = null;
	public static String SUFFIX_SERVICE_IMPL = null;
	public static String SUFFIX_CONTROLLER = null;
	public static String SUFFIX_PROPERTY_FUZZY = null;
	public static String SUFFIX_BEAN_PARAM = null;
	public static String SUFFIX_BEAN_PARAM_TIME_START = null;
	public static String SUFFIX_BEAN_PARAM_TIME_END = null;

	/**
	 * 常量
	 */
	public static String TYPE_STRING = "String";

	public static String TYPE_DATE = "java.util.Date";

	static {
		PACKAGE_BASE = PropertiesUtils.getString("package.base");
		PACKAGE_BEAN = PACKAGE_BASE + "." + PropertiesUtils.getString("package.bean");
		PACKAGE_PARAM = PACKAGE_BASE + "." + PropertiesUtils.getString("package.param");
		PACKAGE_ENUMS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.enums");
		PACKAGE_VO = PACKAGE_BASE + "." + PropertiesUtils.getString("package.vo");
		PACKAGE_MAPPER = PACKAGE_BASE + "." + PropertiesUtils.getString("package.mapper");
		PACKAGE_SERVICE = PACKAGE_BASE + "." + PropertiesUtils.getString("package.service");
		PACKAGE_SERVICE_IMPL = PACKAGE_BASE + "." + PropertiesUtils.getString("package.service.impl");
		PACKAGE_CONTROLLER = PACKAGE_BASE + "." + PropertiesUtils.getString("package.controller");

		PATH_BASE = PropertiesUtils.getString("path.base");
		PATH_BEAN = PATH_BASE + PACKAGE_BEAN.replace(".", "/");
		PATH_PARAM = PATH_BASE + PACKAGE_PARAM.replace(".", "/");
		PATH_ENUMS = PATH_BASE + PACKAGE_ENUMS.replace(".", "/");
		PATH_VO = PATH_BASE + PACKAGE_VO.replace(".", "/");
		PATH_MAPPER = PATH_BASE + PACKAGE_MAPPER.replace(".", "/");
		PATH_MAPPER_XML = PropertiesUtils.getString("path.mapper.xml");
		PATH_SERVICE = PATH_BASE + PACKAGE_SERVICE.replace(".", "/");
		PATH_SERVICE_IMPL = PATH_BASE + PACKAGE_SERVICE_IMPL.replace(".", "/");
		PATH_CONTROLLER = PATH_BASE + PACKAGE_CONTROLLER.replace(".", "/");

		SUFFIX_BEAN_PARAM = PropertiesUtils.getString("suffix.bean.param");
		SUFFIX_PROPERTY_FUZZY = PropertiesUtils.getString("suffix.property.fuzzy");
		SUFFIX_MAPPER = PropertiesUtils.getString("suffix.mapper");
		SUFFIX_MAPPER_XML = PropertiesUtils.getString("suffix.mapper.xml");
		SUFFIX_SERVICE = PropertiesUtils.getString("suffix.service");
		SUFFIX_SERVICE_IMPL = PropertiesUtils.getString("suffix.service.impl");
		SUFFIX_CONTROLLER = PropertiesUtils.getString("suffix.controller");
		SUFFIX_BEAN_PARAM_TIME_START = PropertiesUtils.getString("suffix.bean.param.time.start");
		SUFFIX_BEAN_PARAM_TIME_END = PropertiesUtils.getString("suffix.bean.param.time.end");

	}
}
