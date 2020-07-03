package com.boostcms.system.controller;




import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.boostcms.common.controller.BaseController;
import com.boostcms.common.domain.Tree;
import com.boostcms.common.utils.MD5Utils;
import com.boostcms.common.utils.PageUtils;
import com.boostcms.common.utils.Query;
import com.boostcms.common.utils.QueryStruct;
import com.boostcms.common.utils.R;
import com.boostcms.system.domain.DeptDO;
import com.boostcms.system.domain.RoleDO;
import com.boostcms.system.domain.UserDO;
import com.boostcms.system.domain.UserStatusEnum;
import com.boostcms.system.domain.UserVO;
import com.boostcms.system.service.DeptService;
import com.boostcms.system.service.RoleService;
import com.boostcms.system.service.UserService;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-01-30 21:11:22
 */

@RequestMapping("/system/user")
@Controller
public class UserController extends BaseController {
	private String prefix="system/user"  ;
	@Autowired
	UserService userService;
	@Autowired
	RoleService roleService;
	@Autowired
	DeptService dictService;
	
	@Autowired
	private DeptService sysDeptService;
	
	@RequiresPermissions("system:user:user")
	@GetMapping("")
	String user(Model model) {
		return prefix + "/user";
	}

	@GetMapping("/list")
	@ResponseBody
	PageUtils list(UserDO user,QueryStruct queryStruct) throws IllegalAccessException {
		// 查询列表数据
		Map<String,Object> map1=QueryStruct.objectToMap(queryStruct);
		Map<String,Object> params=QueryStruct.objectToMap(user);
		params.putAll(map1);
        Query query = new Query(params);
		List<UserDO> sysUserList = userService.list(query);
		int total = userService.count(query);
		PageUtils pageUtil = new PageUtils(sysUserList, total);
		return pageUtil;
	}

	@RequiresPermissions("system:user:add")
	@GetMapping("/add")
	String add(Model model) {
		List<RoleDO> roles = roleService.list();
		model.addAttribute("roles", roles);
		return prefix + "/add";
	}

	@RequiresPermissions("system:user:edit")
	@GetMapping("/edit/{id}")
	String edit(Model model, @PathVariable("id") Long id) {
		UserDO userDO = userService.get(id);
		model.addAttribute("user", userDO);
		List<RoleDO> roles = roleService.list(id);
		Map<Long,DeptDO> deptMap=sysDeptService.getAllMap(new HashMap<>());
		
		model.addAttribute("deptMap", deptMap);
		model.addAttribute("roles", roles);
		return prefix+"/edit";
	}

	
	@GetMapping("/getPicture")
	public void getPicture(HttpServletRequest request, HttpServletResponse response, Long deviceId) {
		try {
			response.setContentType("image/jpeg");// 设置相应类型,告诉浏览器输出的内容为图片
			response.setHeader("Pragma", "No-cache");// 设置响应头信息，告诉浏览器不要缓存此内容
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expire", 0);

			UserDO d = userService.get(deviceId);
			if (d.getImg() == null) {
				return;
			}
			try {
				ByteArrayInputStream bais = new ByteArrayInputStream(d.getImg());
				BufferedImage bi1 = ImageIO.read(bais);
				// 将内存中的图片通过流动形式输出到客户端
				ImageIO.write(bi1, "JPEG", response.getOutputStream());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@RequiresPermissions("system:user:add")
	@PostMapping("/save")
	@ResponseBody
	R save(@RequestParam(value="imgs",required=false) MultipartFile file1,UserDO user) throws IOException {
		
		if(null!=file1) {
			if(file1.getBytes().length!=0) {
				user.setImg(file1.getBytes());
			}
		}
		

		user.setPassword(MD5Utils.encrypt(user.getUsername(), user.getPassword()));
		if (userService.save(user) > 0) {
			return R.ok();
		}
		return R.error();
	}

	@RequiresPermissions("system:user:edit")
	@PostMapping("/update")
	@ResponseBody
	R update(@RequestParam(value="imgs",required=false) MultipartFile file1,UserDO user) throws IOException {
		if(null!=file1) {
			if(file1.getBytes().length!=0) {
				user.setImg(file1.getBytes());
			}
		}

		if (userService.update(user) > 0) {
			return R.ok();
		}
		return R.error();
	}




	@RequiresPermissions("system:user:remove")
	@PostMapping("/remove")
	@ResponseBody
	R remove(Long id) {

		if (userService.remove(id) > 0) {
			return R.ok();
		}
		return R.error();
	}



	@PostMapping("/exit")
	@ResponseBody
	boolean exit(@RequestParam Map<String, Object> params) {
		// 存在，不通过，false
		return !userService.exit(params);
	}

	@RequiresPermissions("system:user:resetPwd")
	@GetMapping("/resetPwd/{id}")
	String resetPwd(@PathVariable("id") Long userId, Model model) {

		UserDO userDO = new UserDO();
		userDO.setUserId(userId);
		model.addAttribute("user", userDO);
		return prefix + "/reset_pwd";
	}

	@PostMapping("/resetPwd")
	@ResponseBody
	R resetPwd(UserVO userVO) {

		try{
			userService.resetPwd(userVO,getUser());
			return R.ok();
		}catch (Exception e){
			return R.error(1,e.getMessage());
		}

	}
	@RequiresPermissions("system:user:resetPwd")
	@PostMapping("/adminResetPwd")
	@ResponseBody
	R adminResetPwd(UserVO userVO) {

		try{
			userService.adminResetPwd(userVO);
			return R.ok();
		}catch (Exception e){
			return R.error(1,e.getMessage());
		}

	}
	@GetMapping("/tree")
	@ResponseBody
	public Tree<DeptDO> tree() {
		Tree<DeptDO> tree = new Tree<DeptDO>();
		tree = userService.getTree();
		return tree;
	}

	@GetMapping("/treeView")
	String treeView() {
		return  prefix + "/userTree";
	}

	
	@ResponseBody
	@GetMapping("/UserStatusEnum")
	public List<Map<String,String>> UserStatusEnumList(){
		
		List<Map<String,String>> li = new LinkedList<>();
		UserStatusEnum [] arr=UserStatusEnum.values();
		for(UserStatusEnum em: arr) {
	         Map<String,String> one=new HashMap<>();
	         one.put("name", em.toString());
	         one.put("value",em.getName());
	         li.add(one);
		}

		return li;
	}

}
