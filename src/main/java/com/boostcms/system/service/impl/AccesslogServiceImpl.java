package com.boostcms.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boostcms.system.dao.AccesslogDao;
import com.boostcms.system.domain.AccesslogDO;
import com.boostcms.system.service.AccesslogService;

import java.util.List;
import java.util.Map;

import java.util.HashMap;





@Service
public class AccesslogServiceImpl implements AccesslogService {
	@Autowired
	private AccesslogDao accesslogDao;
	
	@Override
	public AccesslogDO get(Long id){
		return accesslogDao.get(id);
	}
	
	@Override
	public List<AccesslogDO> list(Map<String, Object> map){
		return accesslogDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return accesslogDao.count(map);
	}
	
	@Override
	public int save(AccesslogDO accesslog){
		return accesslogDao.save(accesslog);
	}
	
	@Override
	public int update(AccesslogDO accesslog){
		return accesslogDao.update(accesslog);
	}
	
	@Override
	public int remove(Long id){
		return accesslogDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return accesslogDao.batchRemove(ids);
	}
	
	
		
	@Override
	public Map<Long,AccesslogDO> getAllMap(Map<String, Object> map){
		List<AccesslogDO> typeList=list(map);
		Map<Long,AccesslogDO> rest=new HashMap<>();
		for(AccesslogDO one:typeList) {
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
		
		List<AccesslogDO> typeList=list(paraMap);
		
		List<Map<String,Object>> li = new LinkedList<>();
		
		for(AccesslogDO em:typeList) {
	         Map<String,Object> one=new HashMap<>();
	         one.put("name", String.valueOf(em.getId()));
	         one.put("value",em.getValue());
	         li.add(one);
		}

		return li;
	}
*/	
}
