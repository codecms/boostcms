package com.boostcms.common.aspect;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
 
import javax.servlet.http.HttpServletRequest;
 
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.servlet.ShiroHttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.boostcms.common.utils.IPUtils;
import com.boostcms.system.domain.UserDO;





 
 
@Intercepts({
		@Signature(type = Executor.class, method = "update", args = {
				MappedStatement.class,
				Object.class }),
								 
								 
								 @Signature(type = Executor.class, method = "query", args = {
								 MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class })
								  })
@Component
public class ExecuteSqlLogInterceptor implements Interceptor {
	
	/*
	 * @Autowired private UserInfoService userInfoService;
	 */
 
	private static final Logger log = LoggerFactory.getLogger(ExecuteSqlLogInterceptor.class);
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		Object parameter = null;
		if (invocation.getArgs().length > 1) {
			parameter = invocation.getArgs()[1];
		}
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);
		Configuration configuration = mappedStatement.getConfiguration();
		String exexcSql = getRuntimeExeSql(configuration, boundSql);
		//System.out.println("----Sql----"+exexcSql);
		exexcSql=exexcSql.toLowerCase().trim();
		if(!exexcSql.contains("insert into sys_log") ) {
			log.info(exexcSql);
		}
		
		String username = "";
		String ip = "";
		boolean bWrite=false;
		
		//保存修改和删除操作日志
		if(exexcSql.startsWith("update") || exexcSql.startsWith("delete")
				|| ( exexcSql.startsWith("insert") 
						&& !exexcSql.contains("insert into sys_log")  
						&&  !exexcSql.contains("insert into sql_log")
						&&  !exexcSql.contains("insert into sys_sqlinter"))
			){	

			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			if(requestAttributes != null){ //登录后不为空,没登录执行定时任务时为空
				HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
				ShiroHttpSession session=(ShiroHttpSession) request.getSession();
			
			//	NetworkUtils.getIpAddress(request);
	            if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
	             //   continue;
	            } else {
	                SimplePrincipalCollection principalCollection = (SimplePrincipalCollection) session
	                        .getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
	                UserDO userDO = (UserDO) principalCollection.getPrimaryPrincipal();
	                username=userDO.getUsername();
	                ip=IPUtils.getIpAddr(request);
	                //  UserDO currUser = ShiroUtils.getUser();
	            }       	
			}
			
			

			//userInfoService.insertSys_Log(p);
			
			bWrite=true;	
		}
		Object returnValue=null;
		try {
			returnValue = invocation.proceed();
			if(bWrite) {
			//	writeSql(exexcSql,username,ip);
			}
			
		} catch (Exception e) {
			throw e;
		}
		return returnValue;
	}
 
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}
 
	@Override
	public void setProperties(Properties props) {
	}
 
	private static String getRuntimeExeSql(Configuration configuration,BoundSql boundSql) {
		Object parameterObject = boundSql.getParameterObject();
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
		if (parameterMappings.size() > 0 && parameterObject != null) {
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
				sql = sql.replaceFirst("\\?",getParameterValue(parameterObject));
			} else {
				MetaObject metaObject = configuration.newMetaObject(parameterObject);
				for (ParameterMapping parameterMapping : parameterMappings) {
					String propertyName = parameterMapping.getProperty();
					if (metaObject.hasGetter(propertyName)) {
						Object obj = metaObject.getValue(propertyName);
						sql = sql.replaceFirst("\\?", getParameterValue(obj));
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						Object obj = boundSql.getAdditionalParameter(propertyName);
						sql = sql.replaceFirst("\\?", getParameterValue(obj));
					}
				}
			}
		}
		return sql;
	}
 
	private static String getParameterValue(Object obj) {
		String value = null;
		if (obj instanceof String) {
			value = "'" + obj.toString() + "'";
		} else if (obj instanceof Date) {
			DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
			value = "'" + formatter.format(obj) + "'";
		} else {
			if (obj != null) {
				value = obj.toString();
			} else {
				value = "null";
			}
 
		}
		return value;
	}

}
