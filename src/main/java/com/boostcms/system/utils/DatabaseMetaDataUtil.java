package com.boostcms.system.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;


/**
 * 
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-01-30 21:11:22
 */

@Service
public class DatabaseMetaDataUtil {
	
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	

	
	
	
	public  ResultSet getallTables() throws SQLException, ClassNotFoundException {
		
		Connection conn=sqlSessionFactory.openSession().getConnection();
		
		DatabaseMetaData dm=conn.getMetaData();
		String catalog =null; 

		String schema=null;
		if(dm.getDriverName().toUpperCase().contains("ORACLE")) {
			schema=dm.getUserName().toUpperCase();
			catalog=conn.getCatalog(); //catalog 其实也就是数据库名  
		}
		String tableNamePattern = null; 
		if(dm.getDriverName().toUpperCase().contains("SERVER")) {
			tableNamePattern="%"; 
		}
		ResultSet allTablesResultSet = dm.getTables(catalog,schema,tableNamePattern,new String[]{"TABLE"});  
		conn.close();
		return allTablesResultSet;
	}
	
	public  ResultSet getTableInfo(String tableName) throws SQLException, ClassNotFoundException {
		Connection conn=sqlSessionFactory.openSession().getConnection();
		DatabaseMetaData dm = conn.getMetaData();
		String catalog =null; 

		String schema=null;
		if(dm.getDriverName().toUpperCase().contains("ORACLE")) {
			schema=dm.getUserName().toUpperCase();
			catalog=conn.getCatalog(); //catalog 其实也就是数据库名  
		}
		ResultSet tablesResultSet = dm.getTables(catalog, schema, tableName, new String[] { "TABLE" });
		conn.close();
		return tablesResultSet;
	}
	
	
	public  ResultSet getColumnInfo(String tableName) throws SQLException, ClassNotFoundException {
		Connection conn=sqlSessionFactory.openSession().getConnection();
		
		DatabaseMetaData dm2 = conn.getMetaData();
		String schema=null;
		if(dm2.getDriverName().toUpperCase().contains("ORACLE")) {
			schema=dm2.getUserName().toUpperCase();
		}
		
		ResultSet columnSet = dm2.getColumns(null, schema,tableName, "%");
		
	//	columnSet.cl
		conn.close();
		return columnSet;
	}

	public String getDbDrive() throws SQLException {
		Connection conn=sqlSessionFactory.openSession().getConnection();
		DatabaseMetaData dm2 = conn.getMetaData();
		conn.close();
		return dm2.getDriverName();
	}
	
	public Set<String> getPrimaryKeys(String tableName) throws SQLException {
		Connection conn=sqlSessionFactory.openSession().getConnection();
		DatabaseMetaData dm2 = conn.getMetaData();
		String schema=null;
		if(dm2.getDriverName().toUpperCase().contains("ORACLE")) {
			schema=dm2.getUserName().toUpperCase();
		}
		
		ResultSet  primary=dm2.getPrimaryKeys(null, schema, tableName);
		
		Set<String> res=new HashSet<String>();
		
        while (primary.next()) {
            String columnName = primary.getString("COLUMN_NAME"); //$NON-NLS-1$
            res.add(columnName);
        }
        conn.close();
		
		return res;
	}
	
	
}
