package com.boostcms.application.domain;

import java.io.Serializable;
import java.util.Date;
import com.boostcms.common.domain.BaseDomain;



/**
 * 
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-02-13 15:32:56
 */
public class DictionaryDO  extends BaseDomain implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//中文名
	private String name;
	//字典值类型
	private Integer type;
	//备注
	private String desc;

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：中文名
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：中文名
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：字典值类型
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：字典值类型
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：备注
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * 获取：备注
	 */
	public String getDesc() {
		return desc;
	}
}

/*
*toString
*return ReflectionToStringBuilder.toString(this);     
*
*
*/
