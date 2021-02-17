package com.boostcms.system.service;



import java.util.List;
import java.util.Map;

import com.boostcms.system.domain.SqllogDO;

import java.util.HashMap;


/**
 * 
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-09-22 09:46:36
 */
public interface SqllogService {
	
	SqllogDO get(Long id);
	
	List<SqllogDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(SqllogDO sqllog);
	
	int update(SqllogDO sqllog);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	Map<Long,SqllogDO> getAllMap(Map<String, Object> map);
	
	//List<Map<String,Object>> buildSelectOption();
}
