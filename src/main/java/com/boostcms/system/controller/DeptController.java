package com.boostcms.system.controller;


import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.boostcms.common.aspect.Log;
import com.boostcms.common.controller.BaseController;
import com.boostcms.common.domain.Tree;
import com.boostcms.common.utils.Query;
import com.boostcms.common.utils.QueryStruct;
import com.boostcms.common.utils.R;
import com.boostcms.system.domain.DeptDO;
import com.boostcms.system.domain.UserDO;
import com.boostcms.system.service.DeptService;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-01-30 21:11:22
 */

@Controller
@RequestMapping("/system/sysDept")
public class DeptController extends BaseController {
	private String prefix = "system/dept";
	@Autowired
	private DeptService sysDeptService;

	@Log("部门列表")
	@GetMapping()
	@RequiresPermissions("system:sysDept:sysDept")
	String dept() {
		return prefix + "/dept";
	}


	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("system:sysDept:sysDept")
	public List<DeptDO> list(DeptDO dept,QueryStruct queryStruct) throws IllegalAccessException {
		Map<String,Object> map1=QueryStruct.objectToMap(queryStruct);
		Map<String,Object> params=QueryStruct.objectToMap(dept);
		params.putAll(map1);
		
		////每个人的只可见自己所在的部门以及子部门
		UserDO loginUser = getUser();
		Long deptid=loginUser.getDeptId();
		if(getUser().getUsername().equals("admin")) {
			deptid=0L;	
		}
		if(params.get("deptId")!= null && !params.get("deptId").toString().isEmpty()) {
			
		}else {
			if(deptid != 0L) {
				params.put("deptId", deptid);
			}
		}
		
        Query query = new Query(params);
		List<DeptDO> sysDeptList = sysDeptService.list(query);
		
		 ////当使用树形分支时保证可以显示
		if(deptid!=0L) {
			for(DeptDO one:sysDeptList) {
				if(one.getDeptId().equals(deptid)) {
					one.setParentId(0L);
				}
			}
		}
		
		return sysDeptList;
	}

	@GetMapping("/add/{pId}")
	@RequiresPermissions("system:sysDept:add")
	String add(@PathVariable("pId") Long pId, Model model) {
		////每个人的只可见自己所在的部门以及子部门
		UserDO loginUser = getUser();
		Long deptid=loginUser.getDeptId();
		if(!getUser().getUsername().equals("admin")) {
			if(pId==0L) {
				pId=deptid;
			}
		}
		
		model.addAttribute("pId", pId);
		if (pId == 0L) {
			model.addAttribute("pName", "总部门");
		} else {
			model.addAttribute("pName", sysDeptService.get(pId).getName());
		}
		return  prefix + "/add";
	}

	@GetMapping("/edit/{deptId}")
	@RequiresPermissions("system:sysDept:edit")
	String edit(@PathVariable("deptId") Long deptId, Model model) {
		DeptDO sysDept = sysDeptService.get(deptId);
		model.addAttribute("sysDept", sysDept);
		if(sysDept.getParentId().equals(0L)) {
			model.addAttribute("parentDeptName", "无");
		}else {
			DeptDO parDept = sysDeptService.get(sysDept.getParentId());
			model.addAttribute("parentDeptName", parDept.getName());
		}
		return  prefix + "/edit";
	}

	/**
	 * 保存
	 */
	@Log("部门新增")
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("system:sysDept:add")
	public R save(DeptDO sysDept) {

		if (sysDeptService.save(sysDept) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 修改
	 */
	@Log("部门修改")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("system:sysDept:edit")
	public R update(DeptDO sysDept) {

		if (sysDeptService.update(sysDept) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 删除
	 */
	@Log("部门删除")
	@PostMapping("/remove")
	@ResponseBody
	@RequiresPermissions("system:sysDept:remove")
	public R remove(Long deptId) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parentId", deptId);
		if(sysDeptService.count(map)>0) {
			return R.error(1, "包含下级部门,不允许修改");
		}
		if(sysDeptService.checkDeptHasUser(deptId)) {
			if (sysDeptService.remove(deptId) > 0) {
				return R.ok();
			}
		}else {
			return R.error(1, "部门包含用户,不允许修改");
		}
		return R.error();
	}

	/**
	 * 删除
	 */
	@Log("部门批量删除")
	@PostMapping("/batchRemove")
	@ResponseBody
	@RequiresPermissions("system:sysDept:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] deptIds) {

		sysDeptService.batchRemove(deptIds);
		return R.ok();
	}

	@GetMapping("/tree")
	@ResponseBody
	public Tree<DeptDO> tree() {
		UserDO user = getUser();
		Long deptid=user.getDeptId();
		if(getUser().getUsername().equals("admin")) {
			deptid=0L;	
		}
		Tree<DeptDO> tree = new Tree<DeptDO>();
		tree = sysDeptService.getTree(deptid);
		return tree;
	}

	@GetMapping("/treeView")
	String treeView() {
		return  prefix + "/deptTree";
	}

}
