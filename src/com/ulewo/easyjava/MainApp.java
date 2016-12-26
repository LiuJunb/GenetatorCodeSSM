/**
 * Project Name:easyJava
 * File Name:Test.java
 * Package Name:com.ulewo.easyjava
 * Date:2016年7月30日下午7:02:58
 * Copyright (c) 2016, ulewo.com All Rights Reserved.
 *
*/

package com.ulewo.easyjava;

import com.ulewo.easyjava.bean.DataTableInfo;
import com.ulewo.easyjava.builder.*;
import com.ulewo.easyjava.framwork.BuildFramework;

import java.util.List;

/**
 * ClassName:Test <br/>
 * Date:     2016年7月30日 下午7:02:58 <br/>
 * @author   多多洛
 * Copyright (c) 2016, ulewo.com All Rights Reserved. 
 */
public class MainApp {

	public static void main(String[] args) {
		/**
		 * 获取总的表数：DataTableInfo
		 */
		List<DataTableInfo> tableList = ReadTable.readTable();
		/**
		 * 处理每一张表，例如:User
		 */
		for (DataTableInfo table : tableList) {
//			System.out.println(table.toString());
			BuildFramework.initFramwork(table);
			//创建bean
			BuildBean.buildEntityBean(table);
			//创建参数bean
			BuildParam.buildEntityParam(table);
			//创建mapper
			BuildMapper.buildMapper(table);
			//创建XML
			BuildMapperXml.buildMapperXml(table);
			//创建service
			BuildService.buildService(table);
			//创建serviceimpl
			BuildServiceImpl.buildServiceImpl(table);
			//创建controller
			BuildController.buildController(table);
		}
	}

}
