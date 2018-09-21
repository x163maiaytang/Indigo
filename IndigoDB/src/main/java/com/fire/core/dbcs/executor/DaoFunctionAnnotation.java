package com.fire.core.dbcs.executor;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fire.core.dbcs.dbrule.DefaultDbRule;
import com.fire.core.dbcs.dbrule.IDBZoneRule;
import com.fire.core.dbcs.dbspace.DBZone;
import com.fire.core.dbcs.resulthandler.DefaultResultHandler;
import com.fire.core.dbcs.resulthandler.IResultHandler;

/**
 * DAO接口函数数据库映射处理器 包含参数列表、数据库映射规则信息
 * 
 * @author
 * 
 */
public class DaoFunctionAnnotation
{
	private static final Logger logger = Logger
			.getLogger(DaoFunctionAnnotation.class);

	private String functionName;
	private int paramCount;
	private String[] paramList;
	private boolean valid;

	private IDBZoneRule dbRule;
	private Class<? extends IResultHandler> handlerClz;

	public DaoFunctionAnnotation(String functionname, String annotationinfo,
			Class<? extends IDBZoneRule> dbRuleClz, Class<? extends IResultHandler> handlerClz,Class<?>[] args)
	{
		this.functionName = functionname;
		if(handlerClz != DefaultResultHandler.class)
			this.handlerClz = handlerClz;
		
		try
		{
			dbRule = dbRuleClz.newInstance();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if(dbRule == null)
			dbRule = new DefaultDbRule();
		// 预留第一个参数为rulekey
		int argCount = (args != null) ? args.length - 1 : 0;
		if (annotationinfo == null || annotationinfo.length() <= 0)
			paramList = new String[0];
		else
			paramList = annotationinfo.split(",");

		if (argCount > 1 && paramList.length != argCount)
		{
			logger.error("annotation syntax of " + functionName
					+ " error,field count not equal args count");
			paramCount = 0;
			valid = false;
			return;
		}

		paramCount = argCount;
		if(paramCount > 0 && paramList.length > 0)
		{
			int i = 0;
			while (i < paramCount)
			{
				if (paramList[i] == null || paramList[i].length() <= 0)
					valid = false;
				i++;
			}
		}
		valid = true;
	}

	public DBZone[] getDbZone(String ruleKey)
	{
		return this.dbRule.getDBZone(ruleKey);
	}

	private boolean isSimpleClass(Class<?> classtype)
	{
		return (classtype != String.class && classtype != Integer.class
				&& classtype != int.class && classtype != Byte.class
				&& classtype != byte.class && classtype != Long.class
				&& classtype != long.class && classtype != Short.class && classtype != short.class);
	}

	public int getParamCount()
	{
		return paramCount;
	}

	public String[] getParamList()
	{
		return paramList;
	}

	public boolean isValid()
	{
		return valid;
	}

	public Object createStatementMap(Object[] args)
	{
		Object tmpParam = null;
		if (this.paramCount > 0 && paramList != null)
		{
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			//预留第一个位置是rulekey
			for (int i = 0; i < paramList.length; i++)
				tmpMap.put(paramList[i], args[i + 1]);

			tmpParam = tmpMap;
			tmpMap = null;
		}
		return tmpParam;
	}
	
	public IResultHandler createResultHandler()
	{
		try
		{
			if(handlerClz == null)
				return null;
			return handlerClz.newInstance();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
