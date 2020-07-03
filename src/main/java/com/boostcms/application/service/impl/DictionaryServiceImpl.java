package com.boostcms.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import java.util.HashMap;

import com.boostcms.application.dao.DictionaryDao;
import com.boostcms.application.domain.DictionaryDO;
import com.boostcms.application.service.DictionaryService;



@Service
public class DictionaryServiceImpl implements DictionaryService {
	@Autowired
	private DictionaryDao dictionaryDao;
	
	@Override
	public DictionaryDO get(Integer id){
		return dictionaryDao.get(id);
	}
	
	@Override
	public List<DictionaryDO> list(Map<String, Object> map){
		return dictionaryDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return dictionaryDao.count(map);
	}
	
	@Override
	public int save(DictionaryDO dictionary){
		return dictionaryDao.save(dictionary);
	}
	
	@Override
	public int update(DictionaryDO dictionary){
		return dictionaryDao.update(dictionary);
	}
	
	@Override
	public int remove(Integer id){
		return dictionaryDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return dictionaryDao.batchRemove(ids);
	}
	
	
		
	@Override
	public Map<Integer,DictionaryDO> getAllMap(Map<String, Object> map){
		List<DictionaryDO> typeList=list(map);
		Map<Integer,DictionaryDO> rest=new HashMap<>();
		for(DictionaryDO one:typeList) {
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
		
		List<DictionaryDO> typeList=list(paraMap);
		
		List<Map<String,Object>> li = new LinkedList<>();
		
		for(DictionaryDO em:typeList) {
	         Map<String,Object> one=new HashMap<>();
	         one.put("name", String.valueOf(em.getId()));
	         one.put("value",em.getValue());
	         li.add(one);
		}

		return li;
	}
*/	
}
