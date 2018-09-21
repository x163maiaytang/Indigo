package com.fire.core.dbcs.daoproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.fire.core.dbcs.executor.ExecutorProxy;
/**
 * DAO接口代理类实现
 * 
 * @author 
 * 
 */
public class DaoExecutorProxyEntrance implements InvocationHandler
{
	
	private static final Logger logger = Logger.getLogger(DaoExecutorProxyEntrance.class);
	
	private ExecutorProxy excutorProxy;
	public DaoExecutorProxyEntrance(ExecutorProxy excutorProxy)
	{
		this.excutorProxy=excutorProxy;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		if(excutorProxy!=null){
			
			Object invoke = excutorProxy.invokeStatement(method.getName(),args);
			
			if(invoke != null){
				
				return invoke;
			}
			
			
			Class<?> returnType = method.getReturnType();
			if(returnType != null){
				return  doReturn(invoke, returnType); 
				
			}
		}
		
		return null;
	}

	
	public Object doReturn(Object obj, Class<?> returnClass){
		
		if (obj == null) {
			if (returnClass.equals(int.class) || returnClass.equals(byte.class) || returnClass.equals(short.class)) {
				obj = 0;
			} else if (returnClass.equals(long.class)) {
				obj = 0l;
			} else if (returnClass.equals(float.class)) {
				obj = 0f;
			} else if (returnClass.equals(double.class)) {
				obj = 0d;
			} else if (returnClass.equals(boolean.class)) {
				obj = false;
			} else if (returnClass.equals(char.class)) {
				obj = "";
			}
		}
		
		return obj;
	}
	
 
	
	
}
