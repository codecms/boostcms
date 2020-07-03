package com.boostcms.system.domain;

import com.fasterxml.jackson.annotation.JsonValue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * 
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-02-12 17:05:16
 */
public enum UserStatusEnum  {
	Disable(0,"禁用"),Normal(1,"正常");
	
	private int value;
	private String name;
	private UserStatusEnum(int i,String na) {
		this.value=i;
		this.name=na;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public static UserStatusEnum getInstance(int i) {
		
		UserStatusEnum [] arr=UserStatusEnum.values();
		for(UserStatusEnum baseEnum: arr) {
			if(baseEnum.getValue()==i) {
				return baseEnum;
			}
		}
			
		return Disable;
	}
	
	 @JsonValue
	 public String getJsonValue() {
	        return name;
	 }
}


