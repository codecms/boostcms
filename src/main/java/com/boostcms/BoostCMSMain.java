package com.boostcms;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.boostcms.system.service.GeneratorService;


@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@EnableTransactionManagement
@ServletComponentScan
@MapperScan("com.boostcms.*.dao")
@SpringBootApplication
@EnableCaching
public class BoostCMSMain {
    private final static Logger logger = LoggerFactory.getLogger(BoostCMSMain.class);
    
    public static void main(String[] args) throws Exception {
        
    	System.out.println(Version.version);
    	logger.info(Version.version);
    	
    	ConfigurableApplicationContext context= SpringApplication.run(BoostCMSMain.class, args);
        logger.info("start ok");
        System.out.println("boostcms 启动成功  ﾞ");

       // generator(context);
		
    }
    
    static public void generator(ConfigurableApplicationContext context) throws Exception {
    	String[] tableNames = new String[] { "test_writefile" };
    	
       GeneratorService generatorService=context.getBean(GeneratorService.class);
   	   byte[] data = generatorService.generatorCode(tableNames);
   		
      	File directory = new File(""); 
      	String path= directory.getCanonicalPath(); 
   		try (ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(data))) {
   		    ZipEntry entry = null;
   		    while ((entry = zip.getNextEntry()) != null) {
   		        String name = entry.getName();
   		       
   		        if (!entry.isDirectory()) {
   		        	ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
   		
   		            byte[] byte_s=new byte[1024];
   		            int num=-1;
   		            while((num=zip.read(byte_s,0,byte_s.length))>-1){
   		                 byteArrayOutputStream.write(byte_s,0,num);
   		            }
   		            byte_s = byteArrayOutputStream.toByteArray();
   		            String fileNeirong=new String(byte_s,"UTF-8");           
   		            
   		            String   path_tmp=path+"\\src\\"+name;
                    createNewFile(path_tmp);
   		            FileWriter writer=new FileWriter(path_tmp);
   		            writer.write(fileNeirong);
   		            writer.close();
   		            System.out.println(path_tmp);
   		        }
   		    }
   		}

   		
   		System.exit(0);
    	
    }
    

    
    
	static public boolean createNewFile(String destFileName) {
		File file = new File(destFileName);  
		if(file.exists()) {            
			return false;  
		}        
		if (destFileName.endsWith(File.separator)) {
			return false; 
		}
		if(!file.getParentFile().exists()) {
			if(!file.getParentFile().mkdirs()) {
				return false;   
			}
		}        
		try {			
			if (file.createNewFile()) {
				return true;  
			}
			else { 
				return false;     
			}
		} catch (IOException e) {
				e.printStackTrace();
				return false;
		} 
	}
}



