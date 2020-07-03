package com.boostcms.application.service;

import com.boostcms.application.domain.DictionaryDO;

import java.util.List;
import java.util.Map;
import java.util.HashMap;


/**
 * 
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-02-13 15:32:56
 */
public interface DictionaryService {
	
	DictionaryDO get(Integer id);
	
	List<DictionaryDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(DictionaryDO dictionary);
	
	int update(DictionaryDO dictionary);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
	
	Map<Integer,DictionaryDO> getAllMap(Map<String, Object> map);
	
	//List<Map<String,Object>> buildSelectOption();
}
