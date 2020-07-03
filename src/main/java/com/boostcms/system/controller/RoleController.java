package com.boostcms.system.controller;


import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.boostcms.common.controller.BaseController;
import com.boostcms.common.utils.R;
import com.boostcms.system.domain.RoleDO;
import com.boostcms.system.service.RoleService;

import java.util.List;

/**
 * 
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-01-30 21:11:22
 */

@RequestMapping("/system/role")
@Controller
public class RoleController extends BaseController {
	String prefix = "system/role";
	@Autowired
	RoleService roleService;

	@RequiresPermissions("system:role:role")
	@GetMapping()
	String role() {
		return prefix + "/role";
	}

	@RequiresPermissions("system:role:role")
	@GetMapping("/list")
	@ResponseBody()
	List<RoleDO> list() {
		List<RoleDO> roles = roleService.list();
		return roles;
	}


	@RequiresPermissions("system:role:add")
	@GetMapping("/add")
	String add() {
		return prefix + "/add";
	}


	@RequiresPermissions("system:role:edit")
	@GetMapping("/edit/{id}")
	String edit(@PathVariable("id") Long id, Model model) {
		RoleDO roleDO = roleService.get(id);
		model.addAttribute("role", roleDO);
		return prefix + "/edit";
	}


	@RequiresPermissions("system:role:add")
	@PostMapping("/save")
	@ResponseBody()
	R save(RoleDO role) {

		if (roleService.save(role) > 0) {
			return R.ok();
		} else {
			return R.error(1, "保存失败");
		}
	}


	@RequiresPermissions("system:role:edit")
	@PostMapping("/update")
	@ResponseBody()
	R update(RoleDO role) {

		if (roleService.update(role) > 0) {
			return R.ok();
		} else {
			return R.error(1, "保存失败");
		}
	}


	@RequiresPermissions("system:role:remove")
	@PostMapping("/remove")
	@ResponseBody()
	R save(Long id) {

		if (roleService.remove(id) > 0) {
			return R.ok();
		} else {
			return R.error(1, "删除失败");
		}
	}
	
	@RequiresPermissions("system:role:batchRemove")

	@PostMapping("/batchRemove")
	@ResponseBody
	R batchRemove(@RequestParam("ids[]") Long[] ids) {

		int r = roleService.batchremove(ids);
		if (r > 0) {
			return R.ok();
		}
		return R.error();
	}
}
