package com.fire.core.dbcs.executor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fire.core.dbcs.DBCSConst;
import com.fire.core.dbcs.config.PackageItemInfo;
import com.fire.core.dbcs.config.SqlMapXmlReader;
import com.fire.core.dbcs.daoproxy.IDaoExecutor;
import com.fire.core.dbcs.dbspace.DBSpace;

/**
 * DAO接口函数执行代理管理器
 * 
 * @author
 * 
 */
public class ExecutorManager
{
	private static Map<String, ExecutorProxy> executorMap = new HashMap<String, ExecutorProxy>();
	private static List<ExecutorProxy> executorList = new ArrayList<ExecutorProxy>();
	
	
//	static{
//		
//		DBTaskService.getInstance().scheduleAtFixedRate(new DefaultActorTask(new IActorTaskImpl(){
//			@Override
//			public void doTask() throws Pausable {
//				doUpdate();
//			}
//			
//		}, null),
//		1000, 1000); 
//	}

	public static void initialize()
	{
		String tmpBeanName;
		String tmpPkgName;
		Collection<PackageItemInfo> tmpPkgItemInfos = DBSpace.getMapperItems();
		if (tmpPkgItemInfos != null)
		{
			for (PackageItemInfo tmpPkgInfo : tmpPkgItemInfos)
			{
				tmpBeanName = tmpPkgInfo.BeanName;
				tmpBeanName = tmpBeanName.substring(0, tmpBeanName.length()
						- SqlMapXmlReader.SUFFIX.length());
				tmpPkgName = tmpPkgInfo.PackageName.replace('/', '.');

				regMapperExecutor(tmpBeanName, tmpPkgName, false);
			}
		}

	}

	protected static void doUpdate() {
		long now = System.currentTimeMillis();
		for(ExecutorProxy proxy : executorList){
			proxy.doUpdate(now);
		}
	}

	@SuppressWarnings("unchecked")
	public static void regMapperExecutor(String beanname, String namespace,
			boolean isGameDataZone)
	{
		String tmpExecutorClassName = namespace + "."
				+ DBCSConst.EXECUTOR_INTERFACE_PREFIX + beanname
				+ DBCSConst.EXECUTOR_INTERFACE_SUFFIX;
		if (executorMap.containsKey(tmpExecutorClassName))
			return;

		Class<? extends IDaoExecutor> tmpExecutorClass = null;
		try
		{
			tmpExecutorClass = (Class<? extends IDaoExecutor>) Class
					.forName(tmpExecutorClassName);
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		if (tmpExecutorClass == null)
		{
			// XXX: 待处理，暂时返回NULL
			return;
		}

		if (!tmpExecutorClass.isInterface())
		{
			return;
		}

		ExecutorProxy tmpMapperProxy = new ExecutorProxy(beanname);
		tmpMapperProxy.initialize(tmpExecutorClass);
		executorMap.put(tmpMapperProxy.getExcutorInterfaceName(),
				tmpMapperProxy);
		executorList.add(tmpMapperProxy);
	}

	@SuppressWarnings("unchecked")
	public static <T extends IDaoExecutor> T getExector(Class<T> interfaceClass)
	{
		ExecutorProxy tmpMapperProxy = executorMap.get(interfaceClass
				.getSimpleName());
		return (tmpMapperProxy != null && tmpMapperProxy.isAvailable()) ? (T) tmpMapperProxy
				.getExecutor() : null;
	}

}
