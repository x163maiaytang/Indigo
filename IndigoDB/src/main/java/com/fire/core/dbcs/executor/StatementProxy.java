package com.fire.core.dbcs.executor;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fire.core.dbcs.dbspace.DBSpace;
import com.fire.core.dbcs.dbspace.DBZone;
import com.fire.core.dbcs.resulthandler.IResultHandler;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;

/**
 * DAO接口函数执行代理 对应Mapper中的具体Statement，是接口中的具体方法的执行
 * 
 * @author
 * 
 */
public class StatementProxy
{
	private static final Logger logger = Logger.getLogger(StatementProxy.class);

	private final static String SELECT_LIST_SUFFIX = "ForList";

	private final static int PARAM_SOURCE = 0;
	private final static int PARAM_MAP = 1;

	private final static int ACTION_NONE = 0;
	private final static int ACTION_SELECT = 1;
	private final static int ACTION_SELECT_FORLIST = 2;
	private final static int ACTION_INSERT = 3;
	private final static int ACTION_UPDATE = 4;
	private final static int ACTION_DELETE = 5;
	private final static int ACTION_SQL = 7;
	private final static int ACTION_PROCEDURE = 8;
	private final static int ACTION_SELECT_FORMAP = 9;

	private DaoFunctionAnnotation daoFunctionAnnotation;
	private String beanName;
	private String methodName;
	private String statementId;
	private int actionType;
	private int paramType;
	private boolean available;
	private boolean batch;

	public StatementProxy(String beanName, Method method,
			DaoFunctionAnnotation functionAnnotation, boolean batch)
	{
		this.beanName = beanName;
		this.methodName = method.getName();
		this.daoFunctionAnnotation = functionAnnotation;
		this.available = this.daoFunctionAnnotation.isValid();
		this.batch = batch;
		if (this.available)
			init(method);
		else
			logger.error(beanName + "." + methodName + ":annotation is invalid");
	}

	private void init(Method method)
	{
		DBZone dbzone = DBSpace.getMainDBZone();

		String tmpStatementId = null;
		MappedStatement tmpMappedStatement = null;
		if (methodName.endsWith(SELECT_LIST_SUFFIX))
		{
			tmpStatementId = this.beanName
					+ "."
					+ methodName.substring(0, this.methodName.length()
							- SELECT_LIST_SUFFIX.length());
			try
			{
				tmpMappedStatement = dbzone.getMappedStatement(tmpStatementId);
			} catch (Exception e)
			{
			}
		}
		if (tmpMappedStatement == null)
		{
			tmpStatementId = this.beanName + "." + methodName;
			try
			{
				tmpMappedStatement = dbzone.getMappedStatement(tmpStatementId);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		if (tmpMappedStatement == null)
		{
			logger.error(beanName + "." + methodName
					+ " can't match any mappedstatement");
			this.available = false;
			return;
		}

		this.statementId = tmpStatementId;
		Class<?>[] tmpMParamList = method.getParameterTypes();
		Class<?> tmpParamClass = DBZone
				.getMappedStatementParamClass(tmpMappedStatement);
		if (tmpMParamList != null && tmpMParamList.length > 1)
		{
			if (tmpMParamList.length > 2
					&& (tmpParamClass == null || !Map.class
							.isAssignableFrom(tmpParamClass)))
			{
				logger.error(beanName + "." + methodName
						+ " matched statement " + tmpMappedStatement.getId()
						+ ",but this statement's parameterclass is"
						+ tmpParamClass);
				this.available = false;
				return;
			}

			if (tmpParamClass != null
					&& Map.class.isAssignableFrom(tmpParamClass))
			{
				this.paramType = PARAM_MAP;
			} else
			{
				this.paramType = PARAM_SOURCE;
			}
		} else
		{
			this.paramType = PARAM_SOURCE;
		}

		if (DBZone.isSelectStatement(tmpMappedStatement))
		{
			Class<?> tmpResultClass = method.getReturnType();
			if (Collection.class.isAssignableFrom(tmpResultClass))
			{
				this.actionType = ACTION_SELECT_FORLIST;
			} else
				this.actionType = ACTION_SELECT;
		} else if (DBZone.isInsertStatement(tmpMappedStatement))
		{
			this.actionType = ACTION_INSERT;
		} else if (DBZone.isUpdateStatement(tmpMappedStatement))
		{
			this.actionType = ACTION_UPDATE;
		} else if (DBZone.isDeleteStatement(tmpMappedStatement))
		{
			this.actionType = ACTION_DELETE;
		} else if (DBZone.isProcedureStatement(tmpMappedStatement))
		{
			this.actionType = ACTION_PROCEDURE;
		} else if (DBZone.isUnknownStatement(tmpMappedStatement))
		{
			this.actionType = ACTION_SQL;
		} else
			this.actionType = ACTION_NONE;

		this.available = true;
	}

	public Object invoke(Object[] args)
	{
		if (!this.available)
		{
			logger.error(this.beanName + "." + this.methodName + " is invalid");
			return null;
		}

		if (args[0] != null && args[0].getClass() != String.class)
		{
			logger.error(this.beanName + "." + this.methodName
					+ " is invalid,must first param is rulekey");
			return null;
		}

		if (daoFunctionAnnotation.getParamCount() > 1
				&& (args == null || args.length - 1 != daoFunctionAnnotation
						.getParamCount()))
		{
			logger.error(beanName + "." + methodName
					+ ":invalid invoke paramter list,this method need "
					+ daoFunctionAnnotation.getParamCount() + " paramter");
			return null;
		}
		String ruleKey = null;
		if (args[0] != null)
			ruleKey = (String) args[0];
		
		DBZone[] dbZones = daoFunctionAnnotation.getDbZone(ruleKey);
		if (dbZones == null)
		{
			logger.error(beanName + "." + methodName
					+ " can't match a valid dbzone");
			return null;
		}

		Object tmpParam = null;
		if (this.paramType == PARAM_MAP)
		{
			tmpParam = daoFunctionAnnotation.createStatementMap(args);
		} else if (daoFunctionAnnotation.getParamCount() > 0)
		{
			tmpParam = args[1];
		}

//		System.out.println(Thread.currentThread().getName() +  "  methodName = " + methodName);
		IResultHandler handler = daoFunctionAnnotation.createResultHandler();
		Object result = null;
		zones:
		for (DBZone dbZone : dbZones)
		{
			try
			{
				switch (actionType)
				{
				case ACTION_SELECT:
					result = (daoFunctionAnnotation.getParamCount() > 0) ? dbZone
							.queryForObject(statementId, tmpParam) : dbZone
							.queryForObject(statementId);
					break;
				case ACTION_SELECT_FORLIST:
					result = (daoFunctionAnnotation.getParamCount() > 0) ? dbZone
							.queryForList(statementId, tmpParam) : dbZone
							.queryForList(statementId);
					break;
				case ACTION_INSERT:
//					if(this.methodName.equals("insertRoleBean") || this.methodName.equals("insertCombatBean")){
						
						result = (daoFunctionAnnotation.getParamCount() > 0) ? dbZone
								.insert(statementId, tmpParam) : dbZone
								.insert(statementId);
//					}
					break;
				case ACTION_UPDATE:
					result = (daoFunctionAnnotation.getParamCount() > 0) ? dbZone
							.update(statementId, tmpParam) : dbZone
							.update(statementId);
					break;
				case ACTION_DELETE:
					result = (daoFunctionAnnotation.getParamCount() > 0) ? dbZone
							.delete(statementId, tmpParam) : dbZone
							.delete(statementId);
					break;
				case ACTION_PROCEDURE:
					result = null;
					break;
				case ACTION_SQL:
					result = null;
					break;
				default:
					logger.error("invalid option type");
					break;
				}
				
				if(handler != null)
				{
					if(handler.handleResult(result))
						break zones;
				}					
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		if(handler != null)
			return handler.getResult();
		return result;
	}

	public boolean isAvailable()
	{
		return available;
	}

	public String getMethodName()
	{
		return methodName;
	}

	public String getStatementId()
	{
		return statementId;
	}

	public boolean canBatch() {
		return batch;
	}
}
