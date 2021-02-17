package com.boostcms.common.aspect;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import com.boostcms.system.domain.SqllogDO;
import com.boostcms.system.service.impl.SqllogServiceImpl;



@Component
public class SqlInterceptorThread  implements SmartLifecycle {
	
	public static BlockingQueue<SqllogDO> sqlDataQueue = new LinkedBlockingQueue(1000);
	
	private static final Logger log = LoggerFactory.getLogger(SqlInterceptorThread.class);

	 @Autowired 
	 private SqllogServiceImpl sqllogServiceImpl;

 private boolean isRunning = false;
	private Thread thread = null;
	 
		public void start(){
			if(isRunning()){
				stop();
			}
			isRunning = true;
			thread = new Thread(task);
			thread.start();
			
		}
		 
		 public void stop(){
			 isRunning = false;
			 thread.interrupt();
		 }
		 
		    public boolean isRunning(){
		        return isRunning;
		    }
		    
			private Runnable task = new Runnable() {
		        public void run(){
		        	
		        	log.info("sqlStart");
		        	
		            while (isRunning()) {
		                try {

		                	SqllogDO data = (SqllogDO)sqlDataQueue.take();
		                	sqllogServiceImpl.save(data);
		                	log.info(data.toString());							
					
						}
						catch (Exception ex) {
							log.error("Send Data to MQ Error:", ex);
		                }
		            }
		        }
		 };


}
