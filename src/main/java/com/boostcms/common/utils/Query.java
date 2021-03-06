package com.boostcms.common.utils;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-01-30 21:11:22
 */

public class Query extends LinkedHashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	// 
	private int offset=0;
	// 每页条数
	private int limit=10000;

	public Query(Map<String, Object> params) {
		this.putAll(params);
		// 分页参数
		if(params.containsKey("offset") && params.containsKey("limit")) {
			this.offset = Integer.parseInt(params.get("offset").toString());
			this.limit = Integer.parseInt(params.get("limit").toString());
			this.put("offset", offset);
			this.put("page", offset / limit + 1);
			this.put("limit", limit);
		}
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.put("offset", offset);
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}
