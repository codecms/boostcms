package com.boostcms.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import java.util.HashMap;

import com.boostcms.application.dao.DictonarytypeDao;
import com.boostcms.application.domain.DictonarytypeDO;
import com.boostcms.application.service.DictonarytypeService;



@Service
public class DictonarytypeServiceImpl implements DictonarytypeService {
	@Autowired
	private DictonarytypeDao dictonarytypeDao;
	
	@Override
	public DictonarytypeDO get(Integer id){
		return dictonarytypeDao.get(id);
	}
	
	@Override
	public List<DictonarytypeDO> list(Map<String, Object> map){
		return dictonarytypeDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return dictonarytypeDao.count(map);
	}
	
	@Override
	public int save(DictonarytypeDO dictonarytype){
		return dictonarytypeDao.save(dictonarytype);
	}
	
	@Override
	public int update(DictonarytypeDO dictonarytype){
		return dictonarytypeDao.update(dictonarytype);
	}
	
	@Override
	public int remove(Integer id){
		return dictonarytypeDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return dictonarytypeDao.batchRemove(ids);
	}
	
	
		
	@Override
	public Map<Integer,DictonarytypeDO> getAllMap(Map<String, Object> map){
		List<DictonarytypeDO> typeList=list(map);
		Map<Integer,DictonarytypeDO> rest=new HashMap<>();
		for(DictonarytypeDO one:typeList) {
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
		
		List<DictonarytypeDO> typeList=list(paraMap);
		
		List<Map<String,Object>> li = new LinkedList<>();
		
		for(DictonarytypeDO em:typeList) {
	         Map<String,Object> one=new HashMap<>();
	         one.put("name", String.valueOf(em.getId()));
	         one.put("value",em.getValue());
	         li.add(one);
		}

		return li;
	}
*/	
}
