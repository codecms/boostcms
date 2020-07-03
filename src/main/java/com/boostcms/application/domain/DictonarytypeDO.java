package com.boostcms.application.domain;

import java.io.Serializable;
import java.util.Date;
import com.boostcms.common.domain.BaseDomain;



/**
 * 
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-02-13 15:32:53
 */
public class DictonarytypeDO  extends BaseDomain implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//中文名称
	private String name;

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
	 * 设置：中文名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：中文名称
	 */
	public String getName() {
		return name;
	}
}

/*
*toString
*return ReflectionToStringBuilder.toString(this);     
*
*
*/
