package com.boostcms.system.dao;



import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.boostcms.system.domain.SqllogDO;



/**
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-09-22 09:46:36
 */
@Mapper
public interface SqllogDao {

	SqllogDO get(Long id);
	
	List<SqllogDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(SqllogDO sqllog);
	
	int update(SqllogDO sqllog);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
