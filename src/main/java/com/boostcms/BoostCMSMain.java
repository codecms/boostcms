package com.boostcms;


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
    
    public static void main(String[] args) {
        
    	System.out.println(Version.version);
    	logger.info(Version.version);
    	
    	ConfigurableApplicationContext context= SpringApplication.run(BoostCMSMain.class, args);
        logger.info("start ok");
        System.out.println("boostcms 启动成功  ﾞ");
    }
}
