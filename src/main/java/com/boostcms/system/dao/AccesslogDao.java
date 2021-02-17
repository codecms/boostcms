package com.boostcms.system.dao;



import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.boostcms.system.domain.AccesslogDO;



/**
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-09-22 11:11:29
 */
@Mapper
public interface AccesslogDao {

	AccesslogDO get(Long id);
	
	List<AccesslogDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(AccesslogDO accesslog);
	
	int update(AccesslogDO accesslog);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
