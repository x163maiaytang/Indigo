package com.fire.core.bhns.portal;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.fire.core.bhns.AbstractSimpleService;
import com.fire.core.bhns.IMethodCollection;
import com.fire.core.bhns.IServiceable;
import com.fire.core.bhns.MethodAnnoInfo;
import com.fire.core.bhns.ServiceAnnotationInfo;
import com.fire.core.bhns.ServiceParentAnnotationInfo;
import com.fire.core.bhns.ServicePortalInfo;

/**
 * 方法调用快速索引
 * 
 */
public class SimpleMethodCollection implements IMethodCollection
{
	
	private static Map<String, String> filterMethodMap = new HashMap<String, String>();
	
	static{
		
		for(Method method : Object.class.getDeclaredMethods()){
			filterMethodMap.put(method.getName(), method.getName());
		}
		
	}
	
	/**
	 * key :方法的名字。拼的串。Value 方法。
	 */
	private Map<String, MethodInfo> methodMap;
	/**
	 * key :方法。String 方法的名字。拼的串
	 */
	private Map<MethodInfo, String> methodSignatureMap;

	public SimpleMethodCollection(Class<? extends IServiceable> refclass)
	{
		methodMap = new HashMap<String, MethodInfo>();
		methodSignatureMap = new HashMap<MethodInfo, String>();
		init(refclass);
	}

	private void init(Class<?> refclass)
	{
		if (refclass != null)
		{
			int timeout = 0;
			boolean asyn = false;
			
			if(AbstractSimpleService.class.isAssignableFrom(refclass)){
				
				ServiceAnnotationInfo annotation = refclass.getAnnotation(ServiceAnnotationInfo.class);
				if(annotation != null){
					timeout = annotation.timeout();
					asyn = annotation.asyn();
				}
			} else if(AbstractLocalPortal.class.isAssignableFrom(refclass)){
				ServicePortalInfo annotation = refclass.getAnnotation(ServicePortalInfo.class);
				if(annotation != null){
					timeout = annotation.timeout();
					asyn = annotation.asyn();
				}
			}
			Class<?> superclass = refclass.getSuperclass();
			
			collectMethod(refclass.getDeclaredMethods(), timeout, asyn);
			if(superclass != null && superclass.getAnnotation(ServiceParentAnnotationInfo.class) != null){
				
				collectMethod(superclass.getDeclaredMethods(), timeout, asyn);
			}
		}
	}
	
	
	private void collectMethod(Method[] tmpMethods, int timeout, boolean asyn){
		if (tmpMethods != null)
		{
			Class<?>[] tmpParams;
			StringBuffer tmpSignatureBuffer;
			String tmpSignature;
			MethodAnnoInfo methodAnnoInfo;
			MethodInfo  methodInfo;
			for (Method tmpMethod : tmpMethods)
			{
				if(filterMethodMap.containsKey(tmpMethod.getName())){
					continue;
				}
				methodAnnoInfo = tmpMethod.getAnnotation(MethodAnnoInfo.class);
				tmpSignatureBuffer = new StringBuffer(tmpMethod.getName());
				tmpParams = tmpMethod.getParameterTypes();
				if (tmpParams != null)
				{
					for (Class<?> tmpParamClass : tmpParams)
					{
						tmpSignatureBuffer.append(tmpParamClass.getSimpleName());
					}
				}
				tmpSignature = tmpSignatureBuffer.toString();
				
				if(methodAnnoInfo != null){
					methodInfo = new MethodInfo(tmpMethod, methodAnnoInfo.timeout(), methodAnnoInfo.asyn());
				}else{
					methodInfo = new MethodInfo(tmpMethod, timeout, asyn);
				}
				
				methodMap.put(tmpSignature, methodInfo);
				methodSignatureMap.put(methodInfo, tmpSignature);
			}
		}
	}
	/**
	 * 调用方法 处理.
	 * methodSignature : 方法名+参数类型名+参数类型名....多个
	 * refobj 对象。
	 * args 所有的参数。
	 */
	public Object invokeMethod(String methodSignature, Object refobj, Object[] args)
	{
		if (methodSignature == null || refobj == null)
			return null;

		MethodInfo tmpMethod = this.methodMap.get(methodSignature);
		if (tmpMethod == null)
			return null;

		try
		{
			return tmpMethod.getMethod().invoke(refobj, args);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public MethodInfo getMeshodOfMethodSignature(String methodSignature)
	{
		return (methodSignature != null) ? methodMap.get(methodSignature) : null;
	}

	public String getMethodSignature(Method method)
	{
		return (method != null) ? methodSignatureMap.get(method) : null;
	}
	
	
}
