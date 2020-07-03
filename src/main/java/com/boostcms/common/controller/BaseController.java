package com.boostcms.common.controller;


import org.springframework.stereotype.Controller;

import com.boostcms.common.utils.ShiroUtils;
import com.boostcms.system.domain.UserDO;


/**
 * 
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-01-30 21:11:22
 */

@Controller
public class BaseController {
	public UserDO getUser() {
		return ShiroUtils.getUser();
	}

	public Long getUserId() {
		return getUser().getUserId();
	}

	public String getUsername() {
		return getUser().getUsername();
	}
}