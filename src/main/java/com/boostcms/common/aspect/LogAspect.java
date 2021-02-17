package com.boostcms.common.aspect;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.servlet.ShiroHttpSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.util.StringUtils;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.boostcms.common.utils.IPUtils;
import com.boostcms.system.domain.AccesslogDO;
import com.boostcms.system.domain.UserDO;
import com.boostcms.system.service.AccesslogService;





@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    AccesslogService logService;


    @Pointcut("@annotation(com.boostcms.common.aspect.Log)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        // 执行方法
        Object result = point.proceed();
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        //异步保存日志
        saveLog(point, time,result);
        return result;
    }

    void saveLog(ProceedingJoinPoint joinPoint, long time,Object result) throws InterruptedException {
    	
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AccesslogDO sysLog = new AccesslogDO();
        Log syslog = method.getAnnotation(Log.class);
        if (syslog != null) {
            // 注解上的描述
            sysLog.setOperation(syslog.value());
        }
        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");
        // 请求的参数
		if (!methodName.equalsIgnoreCase("ajaxLogin")) {
			Object[] args = joinPoint.getArgs();
			try {
				String params = StringUtils.abbreviate(JSONUtils.toJSONString(args[0]), 5000);
				sysLog.setParams(params);
			} catch (Exception e) {

			}
		} else {
			
			try {
				Object[] args = joinPoint.getArgs();
				if(args.length>0) {
					sysLog.setUsername(args[0].toString());
				}
				HashMap<String, Object> loginResult = (HashMap<String, Object>) result;
				if(loginResult.containsKey("code") && !loginResult.get("code").toString().equals("0")) {
					sysLog.setOperation("登录失败");
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
        
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if(requestAttributes != null){ //登录后不为空,没登录执行定时任务时为空
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			ShiroHttpSession session=(ShiroHttpSession) request.getSession();
           
			sysLog.setUrl(StringUtils.abbreviate(request.getRequestURI(),200));
		    sysLog.setIp(IPUtils.getIpAddr(request));
			 
			 if(!methodName.equalsIgnoreCase("ajaxLogin")) {
				 sysLog.setParams(StringUtils.abbreviate(JSONObject.toJSONString(request.getParameterMap()),5000));
			 }
		//	NetworkUtils.getIpAddress(request);
            if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
             //   continue;
            } else {
                SimplePrincipalCollection principalCollection = (SimplePrincipalCollection) session
                        .getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                UserDO userDO = (UserDO) principalCollection.getPrimaryPrincipal();

                sysLog.setUsername(userDO.getUsername());
                sysLog.setUserId(userDO.getUserId());


            }       	
		}
              
        sysLog.setTime((int) time);
        // 系统当前时间
        Date date = new Date();
        sysLog.setGmtCreate(date);
        // 保存系统日志
        logService.save(sysLog);
    }
}
