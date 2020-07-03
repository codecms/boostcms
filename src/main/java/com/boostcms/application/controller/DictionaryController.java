package com.boostcms.application.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boostcms.application.domain.DictionaryDO;
import com.boostcms.application.domain.DictonarytypeDO;
import com.boostcms.application.service.DictionaryService;
import com.boostcms.application.service.DictonarytypeService;
import com.boostcms.common.utils.PageUtils;
import com.boostcms.common.utils.Query;
import com.boostcms.common.utils.R;
import com.boostcms.common.utils.QueryStruct;

/**
 * 
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-02-13 15:32:56
 */
 
@Controller
@RequestMapping("/application/dictionary")
public class DictionaryController {
	@Autowired
	private DictionaryService dictionaryService;
	
	@Autowired
	private DictonarytypeService dictonarytypeService;
	
	@GetMapping()
	@RequiresPermissions("application:dictionary:dictionary")
	String Dictionary(Model model){
	    //model.addAttribute("paraMap", paraMap);
		
		Map<Integer,DictonarytypeDO> dictType=dictonarytypeService.getAllMap(new HashMap<>());
		model.addAttribute("dictType", dictType);
	    return "application/dictionary/dictionary";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("application:dictionary:dictionary")
	public PageUtils list(DictionaryDO dictionary,QueryStruct queryStruct) throws IllegalAccessException{
		//查询列表数据
		Map<String,Object> map1=QueryStruct.objectToMap(queryStruct);
		Map<String,Object> params=QueryStruct.objectToMap(dictionary);
		params.putAll(map1);
        Query query = new Query(params);
		List<DictionaryDO> dictionaryList = dictionaryService.list(query);
		int total = dictionaryService.count(query);
		PageUtils pageUtils = new PageUtils(dictionaryList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("application:dictionary:add")
	String add(){
	    return "application/dictionary/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("application:dictionary:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		DictionaryDO dictionary = dictionaryService.get(id);
		model.addAttribute("dictionary", dictionary);
	    return "application/dictionary/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("application:dictionary:add")
	public R save( DictionaryDO dictionary){
		if(dictionaryService.save(dictionary)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@PostMapping("/update")
	@RequiresPermissions("application:dictionary:edit")
	public R update( DictionaryDO dictionary){
		dictionaryService.update(dictionary);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("application:dictionary:remove")
	public R remove( Integer id){
		if(dictionaryService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("application:dictionary:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] ids){
		dictionaryService.batchRemove(ids);
		return R.ok();
	}
	
	
	
	//


	//中文名


	//字典值类型

	@ResponseBody
	@GetMapping("/DictionaryTypeEnum")
	public List<Map<String,String>> DictionaryTypeEnumList(){
		
		List<DictonarytypeDO> typeMap=dictonarytypeService.list(new HashMap<>());
		
		List<Map<String,String>> li = new LinkedList<>();
		
		for(DictonarytypeDO em:typeMap) {
	         Map<String,String> one=new HashMap<>();
	         one.put("name", String.valueOf(em.getId()));
	         one.put("value",String.valueOf(em.getName()));
	         li.add(one);
		}
		
		return li;
	}

	//备注


	
}
