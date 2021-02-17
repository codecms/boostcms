package com.boostcms.system.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.*;


import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import org.springframework.web.multipart.MultipartFile;

import com.boostcms.common.domain.Tree;
import com.boostcms.common.utils.BuildTree;
import com.boostcms.common.utils.MD5Utils;
import com.boostcms.system.dao.DeptDao;
import com.boostcms.system.dao.UserDao;
import com.boostcms.system.dao.UserRoleDao;
import com.boostcms.system.domain.DeptDO;
import com.boostcms.system.domain.UserDO;
import com.boostcms.system.domain.UserRoleDO;
import com.boostcms.system.domain.UserVO;
import com.boostcms.system.service.DeptService;
import com.boostcms.system.service.UserService;

import javax.imageio.ImageIO;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userMapper;
    @Autowired
    UserRoleDao userRoleMapper;
    @Autowired
    DeptDao deptMapper;


    @Autowired
    DeptService deptService;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
//    @Cacheable(value = "user",key = "#id")
    public UserDO get(Long id) {
        List<Long> roleIds = userRoleMapper.listRoleId(id);
        UserDO user = userMapper.get(id);
        user.setRoleIds(roleIds);
        return user;
    }

    @Override
    public List<UserDO> list(Map<String, Object> map) {
        String deptId ="";
        if(map.containsKey("deptId")) {
        	Object deptIdObj=map.get("deptId");
        	if(deptIdObj != null) {
        		deptId=deptIdObj.toString();
        	}
        }
        if (StringUtils.isNotBlank(deptId)) {
            Long deptIdl = Long.valueOf(deptId);
            List<Long> childIds = deptService.listChildrenIds(deptIdl);
            childIds.add(deptIdl);
            map.put("deptId", null);
            map.put("deptIds",childIds);
        }
        return userMapper.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
    	
        String deptId ="";
        if(map.containsKey("deptId")) {
        	Object deptIdObj=map.get("deptId");
        	if(deptIdObj != null) {
        		deptId=deptIdObj.toString();
        	}
        }
        if (StringUtils.isNotBlank(deptId)) {
            Long deptIdl = Long.valueOf(deptId);
            List<Long> childIds = deptService.listChildrenIds(deptIdl);
            childIds.add(deptIdl);
            map.put("deptId", null);
            map.put("deptIds",childIds);
        }
    	
        return userMapper.count(map);
    }

    @Transactional
    @Override
    public int save(UserDO user) throws Exception {
    	if(user.getDeptId()==null || user.getDeptId()==0L) {
      		 throw new Exception("部门不能为空！");
      	}
        int count = userMapper.save(user);
        Long userId = user.getUserId();
        List<Long> roles = user.getRoleIds();
        userRoleMapper.removeByUserId(userId);
        List<UserRoleDO> list = new ArrayList<>();
        for (Long roleId : roles) {
            UserRoleDO ur = new UserRoleDO();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        if (list.size() > 0) {
            userRoleMapper.batchSave(list);
        }
        return count;
    }

    @Override
    public int update(UserDO user) throws Exception {
        UserDO userDO = get(user.getUserId());

        if ("admin".equals(userDO.getUsername()) ) {
       	 if(!"admin".equals(user.getUsername())) {
                 throw new Exception("超级管理员的账号不能修改登录名！");
            }
            user.setDeptId(0L);
       }else {
       	if(user.getDeptId()==null || user.getDeptId()==0L) {
       		 throw new Exception("部门不能为空！");
       	}
       }
        
        int r = userMapper.update(user);
        Long userId = user.getUserId();
        List<Long> roles = user.getRoleIds();
        userRoleMapper.removeByUserId(userId);
        List<UserRoleDO> list = new ArrayList<>();
        for (Long roleId : roles) {
            UserRoleDO ur = new UserRoleDO();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        if (list.size() > 0) {
            userRoleMapper.batchSave(list);
        }
        return r;
    }

    //    @CacheEvict(value = "user")
    @Override
    public int remove(Long userId) throws Exception {
        UserDO userDO = get(userId);
        if ("admin".equals(userDO.getUsername())) {
                   throw new Exception("超级管理员的账号不能删除！");
        }
        userRoleMapper.removeByUserId(userId);
        return userMapper.remove(userId);
    }

    @Override
    public boolean exit(Map<String, Object> params) {
        boolean exit;
        exit = userMapper.list(params).size() > 0;
        return exit;
    }

    @Override
    public Set<String> listRoles(Long userId) {
        return null;
    }

    @Override
    public int resetPwd(UserVO userVO, UserDO loginuser) throws Exception {
    	loginuser = get(loginuser.getUserId());
        if (Objects.equals(userVO.getUserDO().getUserId(), loginuser.getUserId())) {
            if (Objects.equals(MD5Utils.encrypt(loginuser.getUsername(), userVO.getPwdOld()), loginuser.getPassword())) {
            	loginuser.setPassword(MD5Utils.encrypt(loginuser.getUsername(), userVO.getPwdNew()));
                return userMapper.update(loginuser);
            } else {
                throw new Exception("输入的旧密码有误！");
            }
        } else {
            throw new Exception("你修改的不是你登录的账号！");
        }
    }

    @Override
    public int adminResetPwd(UserVO userVO,UserDO login) throws Exception {
        UserDO userDO = get(userVO.getUserDO().getUserId());
        if(!"admin".equals(login.getUsername())) {
      	  throw new Exception("只有超级管理员能重置密码！");
      }
        if ("admin".equals(userDO.getUsername())) {
        	if(!"admin".equals(login.getUsername())) {
                  throw new Exception("超级管理员的账号不允许其他人重置！");
        	}
        }
        userDO.setPassword(MD5Utils.encrypt(userDO.getUsername(), userVO.getPwdNew()));
        return userMapper.update(userDO);


    }



    @Override
    public Tree<DeptDO> getTree() {
        List<Tree<DeptDO>> trees = new ArrayList<Tree<DeptDO>>();
        List<DeptDO> depts = deptService.list(new HashMap<String, Object>(16));
        Long[] pDepts = deptService.listParentDept();
        Long[] uDepts = userMapper.listAllDept();
        Long[] allDepts = (Long[]) ArrayUtils.addAll(pDepts, uDepts);
        for (DeptDO dept : depts) {
            if (!ArrayUtils.contains(allDepts, dept.getDeptId())) {
                continue;
            }
            Tree<DeptDO> tree = new Tree<DeptDO>();
            tree.setId(dept.getDeptId().toString());
            tree.setParentId(dept.getParentId().toString());
            tree.setText(dept.getName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            state.put("mType", "dept");
            tree.setState(state);
            trees.add(tree);
        }
        List<UserDO> users = userMapper.list(new HashMap<String, Object>(16));
        for (UserDO user : users) {
            Tree<DeptDO> tree = new Tree<DeptDO>();
            tree.setId(user.getUserId().toString());
            tree.setParentId(user.getDeptId().toString());
            tree.setText(user.getName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            state.put("mType", "user");
            tree.setState(state);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<DeptDO> t = BuildTree.build(trees,"0");
        return t;
    }





}
