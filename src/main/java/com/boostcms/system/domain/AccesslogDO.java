package com.boostcms.system.domain;

import java.io.Serializable;
import java.util.Date;

import com.boostcms.common.domain.BaseDomain;




/**
 * 
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-09-22 11:11:29
 */
public class AccesslogDO  extends BaseDomain implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//用户id
	private Long userId;
	//用户名
	private String username;
	//用户操作
	private String operation;
	//响应时间
	private Integer time;
	//请求方法
	private String method;
	//请求参数
	private String params;
	//IP地址
	private String ip;
	//访问时间
	private Date gmtCreate;
	//
	private String url;

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：用户id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户id
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * 获取：用户名
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * 设置：用户操作
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}
	/**
	 * 获取：用户操作
	 */
	public String getOperation() {
		return operation;
	}
	/**
	 * 设置：响应时间
	 */
	public void setTime(Integer time) {
		this.time = time;
	}
	/**
	 * 获取：响应时间
	 */
	public Integer getTime() {
		return time;
	}
	/**
	 * 设置：请求方法
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * 获取：请求方法
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * 设置：请求参数
	 */
	public void setParams(String params) {
		this.params = params;
	}
	/**
	 * 获取：请求参数
	 */
	public String getParams() {
		return params;
	}
	/**
	 * 设置：IP地址
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * 获取：IP地址
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * 设置：访问时间
	 */
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	/**
	 * 获取：访问时间
	 */
	public Date getGmtCreate() {
		return gmtCreate;
	}
	/**
	 * 设置：
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * 获取：
	 */
	public String getUrl() {
		return url;
	}
}

/*
*toString
*return ReflectionToStringBuilder.toString(this);     
*
*
*/
