package com.boostcms.application.controller;

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

import com.boostcms.application.domain.DictonarytypeDO;
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
 * @date 2020-02-13 15:32:53
 */
 
@Controller
@RequestMapping("/application/dictonarytype")
public class DictonarytypeController {
	@Autowired
	private DictonarytypeService dictonarytypeService;
	
	@GetMapping()
	@RequiresPermissions("application:dictonarytype:dictonarytype")
	String Dictonarytype(Model model){
	    //model.addAttribute("paraMap", paraMap);
	    return "application/dictonarytype/dictonarytype";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("application:dictonarytype:dictonarytype")
	public PageUtils list(DictonarytypeDO dictonarytype,QueryStruct queryStruct) throws IllegalAccessException{
		//查询列表数据
		Map<String,Object> map1=QueryStruct.objectToMap(queryStruct);
		Map<String,Object> params=QueryStruct.objectToMap(dictonarytype);
		params.putAll(map1);
        Query query = new Query(params);
		List<DictonarytypeDO> dictonarytypeList = dictonarytypeService.list(query);
		int total = dictonarytypeService.count(query);
		PageUtils pageUtils = new PageUtils(dictonarytypeList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("application:dictonarytype:add")
	String add(){
	    return "application/dictonarytype/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("application:dictonarytype:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		DictonarytypeDO dictonarytype = dictonarytypeService.get(id);
		model.addAttribute("dictonarytype", dictonarytype);
	    return "application/dictonarytype/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("application:dictonarytype:add")
	public R save( DictonarytypeDO dictonarytype){
		if(dictonarytypeService.save(dictonarytype)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@PostMapping("/update")
	@RequiresPermissions("application:dictonarytype:edit")
	public R update( DictonarytypeDO dictonarytype){
		dictonarytypeService.update(dictonarytype);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("application:dictonarytype:remove")
	public R remove( Integer id){
		if(dictonarytypeService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("application:dictonarytype:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] ids){
		dictonarytypeService.batchRemove(ids);
		return R.ok();
	}
	
	
	
	//


	//中文名称


	
}
