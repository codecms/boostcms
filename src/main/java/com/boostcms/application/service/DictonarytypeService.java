package com.boostcms.application.service;

import com.boostcms.application.domain.DictonarytypeDO;

import java.util.List;
import java.util.Map;
import java.util.HashMap;


/**
 * 
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-02-13 15:32:53
 */
public interface DictonarytypeService {
	
	DictonarytypeDO get(Integer id);
	
	List<DictonarytypeDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(DictonarytypeDO dictonarytype);
	
	int update(DictonarytypeDO dictonarytype);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
	
	Map<Integer,DictonarytypeDO> getAllMap(Map<String, Object> map);
	
	//List<Map<String,Object>> buildSelectOption();
}
