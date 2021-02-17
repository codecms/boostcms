package com.boostcms.system.utils;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
@Configuration
public class ThymeleafGlobal {

	@Resource
    private void configureThymeleafStaticVars(ThymeleafViewResolver viewResolver) {
        if(viewResolver != null) {
        	 Map<String, Object> vars = new HashMap<>();
            vars.put("picUrl1", ("picUrlpara set value"));
            vars.put("baseUrl1", ("baseUrlpara set value"));
            viewResolver.setStaticVariables(vars);
        }
    }
}
