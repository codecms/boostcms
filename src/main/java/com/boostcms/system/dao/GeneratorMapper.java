package com.boostcms.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;


/**
 * 
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-01-30 21:11:22
 */

public interface GeneratorMapper {
	@Select("select table_name tableName, engine, table_comment tableComment, create_time createTime from information_schema.tables"
			+ " where table_schema = (select database())")
	List<Map<String, Object>> list();

	@Select("select count(*) from information_schema.tables where table_schema = (select database())")
	int count(Map<String, Object> map);

	@Select("select table_name tableName, engine, table_comment tableComment, create_time createTime from information_schema.tables \r\n"
			+ "	where table_schema = (select database()) and table_name = #{tableName}")
	Map<String, String> get(String tableName);

	@Select("select column_name columnName, data_type dataType, column_comment columnComment, column_key columnKey, IS_NULLABLE nullable,extra from information_schema.columns\r\n"
			+ " where table_name = #{tableName} and table_schema = (select database()) order by ordinal_position")
	List<Map<String, String>> listColumns(String tableName);
	
	
	@Select("SELECT convert(varchar(100), A.name)  AS tableName, convert(varchar(100), B.name) AS columnName,convert(varchar(100), C.value) AS column_description  FROM sys.tables A INNER JOIN sys.columns B ON B.object_id = A.object_id LEFT JOIN sys.extended_properties C ON C.major_id = B.object_id AND C.minor_id = B.column_id\r\n" 
			+ "WHERE A.name = #{0} ")
	List<Map<String, String>> SqlServerColumnsRemarks(String tableName);
	
	@Select("select t.column_name as COLUMNNAME, t.COMMENTS as COMMENTS from user_col_comments t where t.table_name = #{tableName}")
	List<Map<String, String>> OracleColumnsRemarks(String tableName);
}
