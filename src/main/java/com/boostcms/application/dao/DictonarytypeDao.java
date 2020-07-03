package com.boostcms.application.dao;

import com.boostcms.application.domain.DictonarytypeDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-02-13 15:32:53
 */
@Mapper
public interface DictonarytypeDao {

	DictonarytypeDO get(Integer id);
	
	List<DictonarytypeDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(DictonarytypeDO dictonarytype);
	
	int update(DictonarytypeDO dictonarytype);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
}
