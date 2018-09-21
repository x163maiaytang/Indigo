package com.fire.core.dbcs.executor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fire.core.dbcs.daoproxy.DaoExecutorProxyEntrance;
import com.fire.core.dbcs.daoproxy.IDaoExecutor;
import com.fire.core.dbcs.interceptor.IDbOprateIntereptor;

/**
 * DAO接口执行代理 对应Mapper，跟一组数据库操作接对应
 * 
 * @author
 * 
 */
public class ExecutorProxy
{
	private static final Logger logger = Logger.getLogger(ExecutorProxy.class);

	private String beanName;
	private Class<? extends IDaoExecutor> excutorInterface;
	private Map<String, StatementProxy> statementMap;// method名称与实现的对应
	private IDaoExecutor daoExcutor;
	private boolean available;

	private IDbOprateIntereptor daoIntereptor;

	public ExecutorProxy(String beanName)
	{
		this.beanName = beanName;
		statementMap = new HashMap<String, StatementProxy>();
	}

	public void initialize(Class<? extends IDaoExecutor> interfaceClass)
	{
		if (interfaceClass == null)
		{
			return;
		}

		if (!IDaoExecutor.class.isAssignableFrom(interfaceClass))
		{
			logger.error("executor of " + this.beanName + "["
					+ interfaceClass.getName()
					+ "]  need inherit IDaoExecutor interface");
			return;
		}
		
//		DAOClass daoClass = interfaceClass.getAnnotation(DAOClass.class);
//		if(daoClass != null){
//			try {
//				daoIntereptor = daoClass.intereptor().newInstance();
//			} catch (InstantiationException | IllegalAccessException e) {
//				e.printStackTrace();
//			}
//		}

		String tmpClassName = interfaceClass.getSimpleName();
		Method[] tmpMethods = interfaceClass.getMethods();
		DAOInfo tmpDAOInfo;
		StatementProxy tmpStatementProxy;
		DaoFunctionAnnotation functionAnnotation;
		for (Method tmpMethod : tmpMethods)
		{
			tmpDAOInfo = (DAOInfo) tmpMethod.getAnnotation(DAOInfo.class);
			if (tmpDAOInfo != null)
			{
				functionAnnotation = new DaoFunctionAnnotation(tmpClassName
						+ "." + tmpMethod.getName(), tmpDAOInfo.Params(),
						tmpDAOInfo.dbRule(), tmpDAOInfo.resultHandler(),
						tmpMethod.getParameterTypes());
				if (functionAnnotation.isValid())
				{
					tmpStatementProxy = new StatementProxy(beanName, tmpMethod,
							functionAnnotation, tmpDAOInfo.batch());
					statementMap.put(tmpMethod.getName(), tmpStatementProxy);
				} else
				{
					logger.error(this.beanName + "." + tmpMethod.getName()
							+ ": method annotation is invalid");
				}
			}
		}

		this.excutorInterface = interfaceClass;
		Class<?> clazzProxy = Proxy.getProxyClass(
				excutorInterface.getClassLoader(), excutorInterface);
		try
		{
			Constructor<?> excutorProxyConstructor = clazzProxy
					.getConstructor(InvocationHandler.class);
			daoExcutor = (IDaoExecutor) excutorProxyConstructor
					.newInstance(new DaoExecutorProxyEntrance(this));
			available = true;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void regStatementProxy(StatementProxy statementProxy)
	{
		statementMap.put(statementProxy.getMethodName(), statementProxy);
	}

	public Object invokeStatement(String methodname, Object[] args)
	{
		if (!available)
		{
			logger.error("executor of " + this.beanName + " is invalid");
			return null;
		}

		StatementProxy tmpStatementProxy = statementMap.get(methodname);
		if (tmpStatementProxy == null)
		{
			logger.error("executor " + methodname + " of " + this.beanName
					+ " not exist");
			return null;
		}

		Object result = null;
		if (daoIntereptor != null)
			result = daoIntereptor.before(tmpStatementProxy, args);

		if (result == null)
		{
//			if(tmpStatementProxy.canBatch()){
//				batchList.add(new BatchBean(methodname, args));
//				return null;
//			}
			
			try
			{
				result = tmpStatementProxy.invoke(args);
			} catch (Exception e)
			{
//				e.printStackTrace();
				logger.error(e);
			} finally
			{
//				if (daoIntereptor != null)
//					result = daoIntereptor.after(tmpStatementProxy,
//							args, result);
			}
		}
		return result;
	}

	public IDaoExecutor getExecutor()
	{
		return this.daoExcutor;
	}

	public boolean isAvailable()
	{
		return available;
	}

	public Class<? extends IDaoExecutor> getExcutorInterface()
	{
		return excutorInterface;
	}

	public String getExcutorInterfaceName()
	{
		return (excutorInterface != null) ? excutorInterface.getSimpleName()
				: null;
	}

	public void doUpdate(long now) {
		if(daoIntereptor != null){
			daoIntereptor.doUpdate(now);
		}
	}

}
