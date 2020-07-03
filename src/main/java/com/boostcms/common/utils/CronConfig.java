package com.boostcms.common.utils;

import java.util.concurrent.Executors;

import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-01-30 21:11:22
 */

@Component 
public class CronConfig implements SchedulingConfigurer{



	    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
	        scheduledTaskRegistrar.setScheduler(Executors.newScheduledThreadPool(10));
	    }
	

}
