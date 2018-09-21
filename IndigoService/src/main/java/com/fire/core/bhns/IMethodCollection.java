package com.fire.core.bhns;

import java.lang.reflect.Method;

import com.fire.core.bhns.portal.MethodInfo;
/**
 * 接口所有函数按名称调用
 * 
 */
public interface IMethodCollection
{
	public Object invokeMethod(String methodSignature, Object refobj, Object[] args);

	public String getMethodSignature(Method method);
	
	public MethodInfo getMeshodOfMethodSignature(String methodSignature);
}
