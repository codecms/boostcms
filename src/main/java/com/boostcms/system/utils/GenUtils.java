package com.boostcms.system.utils;


import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.boostcms.common.utils.BDException;

import com.boostcms.system.domain.ColumnDO;
import com.boostcms.system.domain.TableDO;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-01-30 21:11:22
 */
public class GenUtils {


    public static List<String> getTemplates(String drivename) {
        List<String> templates = new ArrayList<String>();
        templates.add("templates/system/generator/domain.java.vm");
        templates.add("templates/system/generator/Dao.java.vm");
        //templates.add("templates/system/generator/Mapper.java.vm");
        if(drivename.toLowerCase().contains("oracle")) {
        	templates.add("templates/system/generator/OracleMapper.xml.vm");
        }else if(drivename.toLowerCase().contains("server")) {
        	templates.add("templates/system/generator/SqlServerMapper.xml.vm");
        }else{
        	templates.add("templates/system/generator/Mapper.xml.vm");
        }
        templates.add("templates/system/generator/Service.java.vm");
        templates.add("templates/system/generator/ServiceImpl.java.vm");
        templates.add("templates/system/generator/Controller.java.vm");
        templates.add("templates/system/generator/list.html.vm");
        templates.add("templates/system/generator/add.html.vm");
        templates.add("templates/system/generator/edit.html.vm");
        templates.add("templates/system/generator/list.js.vm");
        templates.add("templates/system/generator/add.js.vm");
        templates.add("templates/system/generator/edit.js.vm");
        templates.add("templates/system/generator/common.js.vm");
        
        templates.add("templates/system/generator/ControllerTest.java.vm");
        templates.add("templates/system/generator/ServiceImplTest.java.vm");
        return templates;
    }

    /**
     * 生成代码
     */


    public static void generatorCode(Map<String, String> table,
                                     List<Map<String, String>> columns, ZipOutputStream zip,String drivename) {
        //配置信息
        Configuration config = getConfig();
        //表信息
        TableDO tableDO = new TableDO();
        tableDO.setTableName(table.get("tableName"));
        tableDO.setComments(table.get("tableComment"));
        //表名转换成Java类名
        String className = tableToJava(tableDO.getTableName(), config.getString("tablePrefix"), config.getString("autoRemovePre"));
        tableDO.setClassName(className);
        tableDO.setClassname(StringUtils.uncapitalize(className));

        List<String> generatorEnumList=new LinkedList<>();
        
        //列信息
        List<ColumnDO> columsList = new ArrayList<>();
        for (Map<String, String> column : columns) {
            ColumnDO columnDO = new ColumnDO();
            columnDO.setColumnName(column.get("columnName"));
            columnDO.setDataType(column.get("dataType"));
            if(column.get("dataType").trim().toUpperCase().equals("ENUM")) {
            	columnDO.setDataType("ENUM");
            	columnDO.setIsSelect(1);
            }
            columnDO.setComments(column.get("columnComment"));
            if(column.get("columnComment")!=null && column.get("columnComment").startsWith("ENUM:")) {
            	columnDO.setIsSelect(1);
            	columnDO.setDataType("ENUM");
            	columnDO.setComments(column.get("columnComment").replace("ENUM:", ""));
            }
            if(column.get("columnComment")!=null && column.get("columnComment").startsWith("isExtenalID:")) {
            	columnDO.setIsSelect(1);
            	columnDO.setComments(column.get("columnComment").replace("isExtenalID:", ""));
            }
            
            if(column.get("columnComment")!=null && column.get("columnComment").startsWith("isJAVAENUM:")) {
            	columnDO.setIsSelect(1);
            	columnDO.setDataType("ISJAVAENUM");
            	columnDO.setComments(column.get("columnComment").replace("isJAVAENUM:", ""));
            }
            if(columnDO.getComments() != null  ) {
            	int index=columnDO.getComments().indexOf(";");
            	if(index>0) {
            		columnDO.setComments(columnDO.getComments().substring(0, index));
            	}
            	index=columnDO.getComments().indexOf("；");            	
               if(index>0) {
            		columnDO.setComments(columnDO.getComments().substring(0, index));
            	}
            }
            
            columnDO.setExtra(column.get("extra"));

            columnDO.setMaxSize(Integer.parseInt(column.get("columnSize")));
            
            columnDO.setNullable(column.get("nullable"));
            
            //列名转换成Java属性名
            String attrName = columnToJava(columnDO.getColumnName());
            columnDO.setAttrName(attrName);
            columnDO.setAttrname(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            String attrType = config.getString(columnDO.getDataType(), columnDO.getDataType());
            if(columnDO.getDataType().trim().toUpperCase().equals("ENUM")
            		|| columnDO.getDataType().trim().toUpperCase().equals("ISJAVAENUM")) {
            	attrType=className+attrName+"Enum";
            }
            columnDO.setAttrType(attrType);

            //是否主键
            if ("PRI".equalsIgnoreCase(column.get("columnKey")) && tableDO.getPk() == null) {
                tableDO.setPk(columnDO);
            }

  
            
            columsList.add(columnDO);
        }
        tableDO.setColumns(columsList);

        //没主键，则第一个字段为主键
        if (tableDO.getPk() == null) {
            tableDO.setPk(tableDO.getColumns().get(0));
        }

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);

        //封装模板数据
        Map<String, Object> map = new HashMap<>(16);
        map.put("tableName", tableDO.getTableName());
        map.put("comments", tableDO.getComments());
        map.put("pk", tableDO.getPk());
        map.put("className", tableDO.getClassName());
        map.put("classname", tableDO.getClassname());
        map.put("pathName", config.getString("package").substring(config.getString("package").lastIndexOf(".") + 1));
        map.put("baseName", config.getString("package").substring(0,config.getString("package").lastIndexOf(".")+1));
        map.put("columns", tableDO.getColumns());
        List<ColumnDO> revlist=new LinkedList<>();
        revlist.addAll(tableDO.getColumns());
        Collections.reverse(revlist);
        map.put("columnsRes", revlist);
        map.put("package", config.getString("package"));
        map.put("author", config.getString("author"));
        map.put("email", config.getString("email"));
        map.put("datetime",org.apache.commons.lang3.time.DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        VelocityContext context = new VelocityContext(map);

        //获取模板列表
        List<String> templates = getTemplates(drivename);
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
 
            tpl.merge(context, sw);

            try {
                //添加到zip
                zip.putNextEntry(new ZipEntry(getFileName(template, tableDO.getClassname(), tableDO.getClassName(),config )));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            } catch (IOException e) {
                throw new BDException("渲染模板失败，表名：" + tableDO.getTableName(), e);
            }
        }
        

        generatorCodeEnum(map,columsList,zip,tableDO,config);
    }

    private static void generatorCodeEnum(Map<String, Object> map,List<ColumnDO> columsList,
    		ZipOutputStream zip, TableDO tableDO, Configuration config) {
    	
    	String template="templates/system/generator/enum.java.vm";
    	
        for(ColumnDO columnDO:columsList) {
       	 if(columnDO.getDataType().trim().toUpperCase().equals("ENUM")
       			 || columnDO.getDataType().trim().toUpperCase().equals("ISJAVAENUM")) {

       		 
       		map.put("attrType", columnDO.getAttrType());
       	     VelocityContext context = new VelocityContext(map);
       	    
                StringWriter sw = new StringWriter();
                Template tpl = Velocity.getTemplate(template, "UTF-8");
                tpl.merge(context, sw);

                try {
                    //添加到zip
                    zip.putNextEntry(new ZipEntry(getFileName(template, tableDO.getClassname(), columnDO.getAttrType(), config)));
                    IOUtils.write(sw.toString(), zip, "UTF-8");
                    IOUtils.closeQuietly(sw);
                    zip.closeEntry();
                } catch (IOException e) {
                    throw new BDException("渲染模板失败，表名：" + tableDO.getTableName(), e);
                }
       		 
       	 }
       }
    }

    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String tablePrefix, String autoRemovePre) {
        
         tableName = tableName.substring(tableName.indexOf("_") + 1);
        
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replace(tablePrefix, "");
        }

        return columnToJava(tableName);
    }

    /**
     * 获取配置信息
     */
    public static Configuration getConfig() {
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            throw new BDException("获取配置文件失败，", e);
        }
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, String classname, String className,
    		 Configuration config) {
    	//config
    	String orgName=config.getString("package");
    	String packageName=orgName.substring(orgName.lastIndexOf(".") + 1);
        String packagePath = "main" + File.separator + "java" + File.separator+ orgName.replace(".", File.separator) + File.separator;;
        //String modulesname=config.getString("packageName");
        
     //   if (StringUtils.isNotBlank(packageName)) {
     //       packagePath += packageName.replace(".", File.separator) + File.separator;
     //   }
        if(template.contains("ControllerTest.java.vm")) {
        	packagePath = "test" + File.separator + "java" + File.separator+ orgName.replace(".", File.separator) + File.separator;;
            return packagePath + "controller" + File.separator + className + "ControllerTest.java";
        }
        if(template.contains("ServiceImplTest.java.vm")) {
        	packagePath = "test" + File.separator + "java" + File.separator+ orgName.replace(".", File.separator) + File.separator;;
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImplTest.java";
        }

        if (template.contains("domain.java.vm")) {
            return packagePath + "domain" + File.separator + className + "DO.java";
        }
        
        if (template.contains("enum.java.vm")) {
            return packagePath + "domain" + File.separator + className + ".java";
        }

        if (template.contains("Dao.java.vm")) {
            return packagePath + "dao" + File.separator + className + "Dao.java";
        }

//		if(template.contains("Mapper.java.vm")){
//			return packagePath + "dao" + File.separator + className + "Mapper.java";
//		}

        if (template.contains("Service.java.vm")) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }

        if (template.contains("ServiceImpl.java.vm")) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if (template.contains("Controller.java.vm")) {
            return packagePath + "controller" + File.separator + className + "Controller.java";
        }

        if (template.contains("Mapper.xml.vm")) {
            return "main" + File.separator + "resources" + File.separator + "mybatis" + File.separator + packageName + File.separator + className + "Mapper.xml";
        }

        if (template.contains("list.html.vm")) {
            return "main" + File.separator + "resources" + File.separator + "templates" + File.separator
                    + packageName + File.separator + classname + File.separator + classname + ".html";
            //				+ "modules" + File.separator + "generator" + File.separator + className.toLowerCase() + ".html";
        }
        if (template.contains("add.html.vm")) {
            return "main" + File.separator + "resources" + File.separator + "templates" + File.separator
                    + packageName + File.separator + classname + File.separator + "add.html";
        }
        if (template.contains("edit.html.vm")) {
            return "main" + File.separator + "resources" + File.separator + "templates" + File.separator
                    + packageName + File.separator + classname + File.separator + "edit.html";
        }

        if (template.contains("list.js.vm")) {
            return "main" + File.separator + "resources" + File.separator + "static" + File.separator + "js" + File.separator
                    + "appjs" + File.separator + packageName + File.separator + classname + File.separator + classname + ".js";
            //		+ "modules" + File.separator + "generator" + File.separator + className.toLowerCase() + ".js";
        }
        if (template.contains("add.js.vm")) {
            return "main" + File.separator + "resources" + File.separator + "static" + File.separator + "js" + File.separator
                    + "appjs" + File.separator + packageName + File.separator + classname + File.separator + "add.js";
        }
        if (template.contains("edit.js.vm")) {
            return "main" + File.separator + "resources" + File.separator + "static" + File.separator + "js" + File.separator
                    + "appjs" + File.separator + packageName + File.separator + classname + File.separator + "edit.js";
        }
        if (template.contains("common.js.vm")) {
            return "main" + File.separator + "resources" + File.separator + "static" + File.separator + "js" + File.separator
                    + "appjs" + File.separator + packageName + File.separator + classname + File.separator + "common.js";
        }

//		if(template.contains("menu.sql.vm")){
//			return className.toLowerCase() + "_menu.sql";
//		}

        return null;
    }
}
