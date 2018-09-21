package com.fire.core.dbcs;

import com.fire.core.dbcs.daoproxy.IDaoExecutor;
import com.fire.core.dbcs.dbspace.DBSpace;
import com.fire.core.dbcs.dbspace.GamedbInfoBean;
import com.fire.core.dbcs.executor.ExecutorManager;
import com.fire.core.dbcs.interceptor.IDbOprateIntereptor;

/**
 * 分布式数据库代理服务
 * 
 * @author
 * 
 */
public class DBCS
{
	private final static String DB_CONFIG_PATH = "/db";
//	private static IDbOprateIntereptor _intereptor;

	public static void initialize(IDbOprateIntereptor intereptor,
			GamedbInfoBean... connectionInfos)
	{
//		_intereptor = intereptor;
		DBSpace.initialize(DB_CONFIG_PATH, connectionInfos);
		ExecutorManager.initialize();
	}


	public static void initialize(IDbOprateIntereptor intereptor, String ip,
			String dbName, String name, String password, String port)
	{
//		_intereptor = intereptor;
		GamedbInfoBean info = new GamedbInfoBean();
		info.setId(1);
		info.setIp(ip);
		info.setDbName(dbName);
		info.setName(name);
		info.setPassword(password);
		info.setPort(port);
		DBSpace.initialize(DB_CONFIG_PATH, info);
		ExecutorManager.initialize();
	}

	public static <T extends IDaoExecutor> T getExector(Class<T> interfaceClass)
	{
		return ExecutorManager.getExector(interfaceClass);
	}

//	public static IDbOprateIntereptor getIntereptor()
//	{
//		return _intereptor;
//	}
	
	
	

}
