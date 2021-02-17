package com.boostcms.system.service;



import java.util.List;
import java.util.Map;

import com.boostcms.system.domain.AccesslogDO;

import java.util.HashMap;


/**
 * 
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-09-22 11:11:29
 */
public interface AccesslogService {
	
	AccesslogDO get(Long id);
	
	List<AccesslogDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(AccesslogDO accesslog);
	
	int update(AccesslogDO accesslog);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	Map<Long,AccesslogDO> getAllMap(Map<String, Object> map);
	
	//List<Map<String,Object>> buildSelectOption();
}
