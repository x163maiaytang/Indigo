package com.fire.core.bhns.portal;

import java.lang.reflect.Method;

public class MethodInfo {

	private Method method;
	
	private int timeout;
	
	private boolean asyn;
	
	private Class returnClass;
	
	public MethodInfo(Method method, int timeout, boolean asyn){
		this.method = method;
		this.timeout = timeout;
		this.asyn = asyn;
		if(method != null){
			
			this.returnClass = method.getReturnType();
		}
		
	}

	public Method getMethod() {
		return method;
	}

	public int getTimeout() {
		return timeout;
	}

	public boolean isAsyn() {
		return asyn;
	}
	
	
//	public static void main(String[] args) {
//		Method[] declaredMethods = MethodInfo.class.getDeclaredMethods();
//		
//		
//		for(Method mdthod : declaredMethods){
//			
//			System.out.println(mdthod.getName() + "=" + mdthod.getReturnType().equals(void.class));
//			System.out.println(mdthod.getName() + "=" + mdthod.getReturnType().equals(int.class));
//			System.out.println(mdthod.getName() + "=" + mdthod.getReturnType().equals(boolean.class));
//		}
//	}
	
	public Object getReturn(Object obj) {
		
		if(returnClass != null){
			
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
		}
		
		return obj;
	}

	public boolean haveReturn() {
		return returnClass == null;
	}

	public Class getReturnClass() {
		return returnClass;
	}
	
	
}
