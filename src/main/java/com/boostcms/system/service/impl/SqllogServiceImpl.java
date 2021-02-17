package com.boostcms.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boostcms.system.dao.SqllogDao;
import com.boostcms.system.domain.SqllogDO;
import com.boostcms.system.service.SqllogService;

import java.util.List;
import java.util.Map;

import java.util.HashMap;





@Service
public class SqllogServiceImpl implements SqllogService {
	@Autowired
	private SqllogDao sqllogDao;
	
	@Override
	public SqllogDO get(Long id){
		return sqllogDao.get(id);
	}
	
	@Override
	public List<SqllogDO> list(Map<String, Object> map){
		return sqllogDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return sqllogDao.count(map);
	}
	
	@Override
	public int save(SqllogDO sqllog){
		return sqllogDao.save(sqllog);
	}
	
	@Override
	public int update(SqllogDO sqllog){
		return sqllogDao.update(sqllog);
	}
	
	@Override
	public int remove(Long id){
		return sqllogDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return sqllogDao.batchRemove(ids);
	}
	
	
		
	@Override
	public Map<Long,SqllogDO> getAllMap(Map<String, Object> map){
		List<SqllogDO> typeList=list(map);
		Map<Long,SqllogDO> rest=new HashMap<>();
		for(SqllogDO one:typeList) {
			rest.put(one.getId(), one);
		}
		return rest;
	}
	
/*	
	@Override
	public List<Map<String,Object>> buildSelectOption(){
		
		
		Map<String, Object> paraMap=new HashMap<>();
		paraMap.put("sort", "name");
		paraMap.put("order", "desc");
		
		List<SqllogDO> typeList=list(paraMap);
		
		List<Map<String,Object>> li = new LinkedList<>();
		
		for(SqllogDO em:typeList) {
	         Map<String,Object> one=new HashMap<>();
	         one.put("name", String.valueOf(em.getId()));
	         one.put("value",em.getValue());
	         li.add(one);
		}

		return li;
	}
*/	
}
