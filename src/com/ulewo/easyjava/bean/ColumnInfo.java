/**
 * Project Name:jettyTest
 * File Name:DataTableClumnInfo.java
 * Package Name:com.createproject
 * Date:2016年4月8日下午4:41:58
 * Copyright (c) 2016, ulewo.com All Rights Reserved.
 */

package com.ulewo.easyjava.bean;

/**
 * ClassName: ColumnInfo
 * date: 2016年4月11日 下午3:17:44
 * @author luohaili
 * @since JDK 1.7
 */
public class ColumnInfo implements Comparable<ColumnInfo> {
    /**
     * 字段名称
     */
    private String columnName;

    /**
     * bean属性名称
     */
    private String propertyName;

    /**
     * 字段类型
     */
    private String type;

    /**
     * 字段备注
     */
    private String comment;

    /**
     * 字段是否是自增长
     */
    private Boolean isAutoIncrement;

    /**
     * 是否是主要的主键
     */
    private Boolean isPrimaryKey;

    /**
     * 是否是唯一主键
     */
    private boolean isUniqueKey;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getIsAutoIncrement() {
        return isAutoIncrement;
    }

    public void setIsAutoIncrement(Boolean isAutoIncrement) {
        this.isAutoIncrement = isAutoIncrement;
    }

    public Boolean getIsPrimaryKey() {
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(Boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    public boolean isUniqueKey() {
        return isUniqueKey;
    }

    public void setUniqueKey(boolean isUniqueKey) {
        this.isUniqueKey = isUniqueKey;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public int compareTo(ColumnInfo o) {
        if (this.isPrimaryKey != null && this.isPrimaryKey && (o.getIsPrimaryKey() == null || !o.getIsPrimaryKey())) {
            return -1;
        } else {
            return 1;
        }
    }

	@Override
	public String toString() {
		return "ColumnInfo [columnName=" + columnName + ", propertyName=" + propertyName + ", type=" + type
				+ ", comment=" + comment + ", isAutoIncrement=" + isAutoIncrement + ", isPrimaryKey=" + isPrimaryKey
				+ ", isUniqueKey=" + isUniqueKey + "]";
	}
    
    

}
