package com.boostcms.system.service;

import java.util.List;
import java.util.Map;
import java.util.Set;


import org.springframework.stereotype.Service;


import org.springframework.web.multipart.MultipartFile;

import com.boostcms.common.domain.Tree;
import com.boostcms.system.domain.DeptDO;
import com.boostcms.system.domain.UserDO;
import com.boostcms.system.domain.UserVO;

@Service
public interface UserService {
	UserDO get(Long id);

	List<UserDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(UserDO user);

	int update(UserDO user);

	int remove(Long userId);



	boolean exit(Map<String, Object> params);

	Set<String> listRoles(Long userId);

	int resetPwd(UserVO userVO,UserDO userDO) throws Exception;
	int adminResetPwd(UserVO userVO) throws Exception;
	Tree<DeptDO> getTree();




 }
