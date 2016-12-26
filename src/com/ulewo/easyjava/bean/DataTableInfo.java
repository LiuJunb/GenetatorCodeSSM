/**
 * Project Name:jettyTest
 * File Name:DataTableInfo.java
 * Package Name:com.createproject
 * Date:2016年4月8日下午4:40:25
 * Copyright (c) 2016, ulewo.com All Rights Reserved.
 */

package com.ulewo.easyjava.bean;

import java.util.List;
/**
 * 一张表，例如user表
 * tableName=user,
 * beanName=User, 
 * beanParamName=UserQuery, 
 * comment=, 
 * columnList=[
 * 	ColumnInfo [
 * 		columnName=id, 
 * 		propertyName=id, 
 * 		type=Integer, 
 * 		comment=, 
 * 		isAutoIncrement=false, 
 * 		isPrimaryKey=true, 
 * 		isUniqueKey=false], 
 * 	ColumnInfo [
 * 		columnName=name, 
 * 		propertyName=name, 
 * 		type=String, 
 * 		comment=, 
 * 		isAutoIncrement=false, 
 * 		isPrimaryKey=null, 
 * 		isUniqueKey=false], 
 * 	ColumnInfo [
 * 		columnName=price, 
 * 		propertyName=price, 	
 * 		type=java.math.BigDecimal, 
 * 		comment=, 
 * 		isAutoIncrement=false, 
 * 		isPrimaryKey=null, 
 * 		isUniqueKey=false], 
 * 	ColumnInfo [
 * 		columnName=age, 
 * 		propertyName=age, 
 * 		type=Integer, 
 * 		comment=, 
 * 		isAutoIncrement=false, 
 * 		isPrimaryKey=null, 
 * 		isUniqueKey=false]
 * 	]
 * ]
 * 
 * @author Administrator
 *
 */
public class DataTableInfo {
    /**
     * 表名
     */
    private String tableName;

    /**
     * bean名称
     */
    private String beanName;

    /**
     * 参数名称
     */
    private String beanParamName;

    /**
     * 表注释
     */
    private String comment;

    /**
     * 字段信息
     */
    List<ColumnInfo> columnList;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ColumnInfo> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<ColumnInfo> columnList) {
        this.columnList = columnList;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanParamName() {
        return beanParamName;
    }

    public void setBeanParamName(String beanParamName) {
        this.beanParamName = beanParamName;
    }

	@Override
	public String toString() {
		return "DataTableInfo [tableName=" + tableName + ", beanName=" + beanName + ", beanParamName=" + beanParamName
				+ ", comment=" + comment + ", columnList=" + columnList + "]";
	}
    
    

}
