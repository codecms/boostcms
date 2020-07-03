package com.boostcms.system.service.impl;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipOutputStream;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boostcms.system.dao.GeneratorMapper;
import com.boostcms.system.service.GeneratorService;
import com.boostcms.system.utils.DatabaseMetaDataUtil;
import com.boostcms.system.utils.GenUtils;


/**
 * 
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-01-30 21:11:22
 */



@Service
public class GeneratorServiceImpl implements GeneratorService {
	@Autowired
	GeneratorMapper generatorMapper;

	
	@Autowired
	DatabaseMetaDataUtil databaseMetaDataUtil;
	
	
	@Override
	public List<Map<String, Object>> list() {
		List<Map<String, Object>> list = generatorMapper.list();
		return list;
	}

	Map<String,String> getCommonsMap(String driveName,String tableName){
		Map<String,String> commons=new HashMap<String,String>();
		List<Map<String, String>> records=null;
		if(driveName.toLowerCase().contains("oracle")) {
			records=generatorMapper.OracleColumnsRemarks(tableName);
			if(records != null) {
				for(Map<String, String> record:records) {
					if(record.get("COLUMNNAME") != null &&
							record.get("COMMENTS") != null ) {
						commons.put(record.get("COLUMNNAME"), record.get("COMMENTS"));
					}
				}
			}
			
		}else if(driveName.toLowerCase().contains("server")){
			records=generatorMapper.SqlServerColumnsRemarks(tableName);
			if(records != null) {
				for(Map<String, String> record:records) {
					if(record.get("columnName") != null &&
							record.get("column_description") != null ) {
						commons.put(record.get("columnName"), record.get("column_description"));
					}
				}
			}
		}

		
		
		
		return commons;
	}
	
	@Override
	public byte[] generatorCode(String[] tableNames) throws ClassNotFoundException, SQLException {
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);
		//查询列信息
		List<Map<String, String>> columns = new ArrayList<Map<String,String>>();
		for(String tableName : tableNames){
			
			String tableNa=null;
			String tableRemarks=null;
			ResultSet tablesResultSet=databaseMetaDataUtil.getTableInfo(tableName);
			if (tablesResultSet.next()) {
				 tableNa = tablesResultSet.getString("TABLE_NAME");
				 tableRemarks = tablesResultSet.getString("REMARKS");
			} 
			tablesResultSet.close();
			
			Map<String,String> columnsRemarks=getCommonsMap(databaseMetaDataUtil.getDbDrive(),tableName);
			
		    Set<String> primaryKeys=databaseMetaDataUtil.getPrimaryKeys(tableName);
			
			ResultSet columnResultSet=databaseMetaDataUtil.getColumnInfo(tableName);

				while (columnResultSet.next()) {

					String remarks;
					String columnName = columnResultSet.getString("COLUMN_NAME");//列名 
					String columnType = columnResultSet.getString("TYPE_NAME"); //数据类型
					int nullable = columnResultSet.getInt("NULLABLE"); //空值,1默认为空0默认不为空
					
					
					int columnSize=columnResultSet.getInt("COLUMN_SIZE");
					int decimal_digits=columnResultSet.getInt("DECIMAL_DIGITS");
					if(columnType.toUpperCase().equals("NUMBER")) {
						if(decimal_digits==0) {
							if(columnSize<=11) {
								columnType="INTEGER";
							}else {
								columnType="BIGINT";
							}
						}else {
							columnType="DECIMAL";
						}
					}
					
					String auto ="";
					try {
						auto = columnResultSet.getString("IS_AUTOINCREMENT");//自增长
					} catch (Exception e) {
					}
					
					
					if(columnsRemarks.containsKey(columnName)) {
                        remarks=columnsRemarks.get(columnName);
					}else {
						remarks=columnResultSet.getString("REMARKS");
					}
					 
					 
					 
					Map<String,String> columnMap=new HashMap<String,String>();
					columnMap.put("columnName",columnName);
					columnMap.put("dataType",columnType);
					columnMap.put("columnComment",remarks);
					columnMap.put("columnName",columnName);
					columnMap.put("nullable",1==nullable?"YES":"NO");
					columnMap.put("extra","YES".equals(auto)?"auto_increment":"");
					columnMap.put("columnSize",String.valueOf(columnSize));
					if(primaryKeys.contains(columnName)) {
						columnMap.put("columnKey","PRI");
					}else {
						columnMap.put("columnKey","");
					}
					columns.add(columnMap);
				} 
			
			//查询表信息
			Map<String, String> table = new HashMap<String,String>();
			table.put("tableName", tableNa);
			table.put("tableComment", tableRemarks==null?"":tableRemarks);
			
			columnResultSet.close();
			
			//生成代码
			GenUtils.generatorCode(table, columns, zip,databaseMetaDataUtil.getDbDrive());
		}
		IOUtils.closeQuietly(zip);
		return outputStream.toByteArray();
	}
	
	

}
