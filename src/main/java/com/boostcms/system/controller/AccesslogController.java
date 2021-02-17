package com.boostcms.system.controller;


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

import com.boostcms.common.aspect.Log;
import com.boostcms.common.utils.PageUtils;
import com.boostcms.common.utils.Query;
import com.boostcms.common.utils.QueryStruct;
import com.boostcms.common.utils.R;
import com.boostcms.system.domain.AccesslogDO;
import com.boostcms.system.service.AccesslogService;




/**
 * 
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-09-22 11:11:29
 */
 
@Controller
@RequestMapping("/system/accesslog")
public class AccesslogController {
	@Autowired
	private AccesslogService accesslogService;
	
	@Log("系统日志列表")
	@GetMapping()
	@RequiresPermissions("system:accesslog:accesslog")
	String Accesslog(Model model){
	    //model.addAttribute("paraMap", paraMap);
	    return "system/accesslog/accesslog";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("system:accesslog:accesslog")
	public PageUtils list(AccesslogDO accesslog,QueryStruct queryStruct) throws IllegalAccessException{
		//查询列表数据
		Map<String,Object> map1=QueryStruct.objectToMap(queryStruct);
		Map<String,Object> params=QueryStruct.objectToMap(accesslog);
		params.putAll(map1);
        Query query = new Query(params);
		List<AccesslogDO> accesslogList = accesslogService.list(query);
		int total = accesslogService.count(query);
		PageUtils pageUtils = new PageUtils(accesslogList, total);
		return pageUtils;
	}
	






	
	


	
}
