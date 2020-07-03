package com.boostcms.system.controller;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.boostcms.system.service.GeneratorService;
import com.boostcms.system.utils.DatabaseMetaDataUtil;

/**
 * 
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-01-30 21:11:22
 */


@RequestMapping("/system/generator")
@Controller
public class GeneratorController {
	String prefix = "system/generator";
	@Autowired
	GeneratorService generatorService;
	
	@Autowired
	DatabaseMetaDataUtil databaseMetaDataUtil;

	@GetMapping()
	String generator() {
		return prefix + "/list";
	}

	@ResponseBody
	@GetMapping("/list")
	List<Map<String, Object>> list() throws ClassNotFoundException, SQLException {
		//List<Map<String, Object>> list = generatorService.list();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		ResultSet tablesResultSet=databaseMetaDataUtil.getallTables();
		while(tablesResultSet.next()){  
		    String tableName = tablesResultSet.getString("TABLE_NAME");
		    String remarks = tablesResultSet.getString("REMARKS");
		    Map<String,Object> map=new HashMap<String,Object>();
		    map.put("tableName", tableName);
		    map.put("tableComment", remarks==null?"":remarks);
		    list.add(map);
		}
		tablesResultSet.close();
		Collections.sort(list, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> u1, Map<String, Object> u2) {
				 return u1.get("tableName").toString().compareTo(u2.get("tableName").toString());
			}
		});
		return list;
	};

	@RequestMapping("/code/{tableName}")
	public void code(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("tableName") String tableName) throws IOException, ClassNotFoundException, SQLException {
		String[] tableNames = new String[] { tableName };
		byte[] data = generatorService.generatorCode(tableNames);
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=\""+tableName+".zip\"");
		response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream; charset=UTF-8");

		IOUtils.write(data, response.getOutputStream());
	}






}
