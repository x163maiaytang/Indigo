package com.fire.core.dbcs.dbspace;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.logicalcobwebs.proxool.configuration.JAXPConfigurator;

import com.fire.core.dbcs.config.AbstractTagFileReader;
import com.fire.core.dbcs.config.DbConfigXmlReader;
import com.fire.core.dbcs.config.PackageItemInfo;
import com.fire.core.dbcs.config.SqlMapXmlReader;
import com.fire.core.util.xml.IParamterSupport;
import com.fire.core.util.xml.ISimpleParamters;
import com.fire.core.util.xml.XmlFileSupport;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMap;
import com.ibatis.sqlmap.engine.mapping.result.ResultMap;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;
import com.ibatis.sqlmap.engine.mapping.statement.StatementType;

/**
 * 一个数据库连接 持有Mapper信息
 * 
 * @author
 * 
 */
public class DBZone implements IParamterSupport
{
	private static final Logger logger = Logger.getLogger(DBZone.class);

	private SqlMapClient sqlMapClient;

	private List<PackageItemInfo> mapperItems = new ArrayList<PackageItemInfo>();
	private List<String> mapperPackageList = new ArrayList<String>();
	
	private String sqlMapTemplate;
	private String dataSourceName;
	private String dataSourceConfig;

	private GamedbInfoBean info;

	public DBZone(GamedbInfoBean info)
	{
		this.info = info;
	}

	public void loadConfig(String sqlMapConfigFile)
	{
		XmlFileSupport.parseXmlFromResource(sqlMapConfigFile, this);
	}

	public void loadSqlMap() throws Exception
	{
		AbstractTagFileReader xmlReader = new DbConfigXmlReader(this);
		String tmpXmlContent = xmlReader.getContent();
		
//		logger.error(tmpXmlContent);
		ByteArrayInputStream tmpIn = new ByteArrayInputStream(
				tmpXmlContent.getBytes());
		Reader reader = new InputStreamReader(tmpIn);
		JAXPConfigurator.configure(reader, false);

		xmlReader = new SqlMapXmlReader(this);
		tmpXmlContent = xmlReader.getContent();
		tmpIn = new ByteArrayInputStream(tmpXmlContent.getBytes());
		// 配置数据源
		sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(tmpIn);
	}

	@Override
	public void putParamter(ISimpleParamters paramter)
	{
		if ("MAPPER".equals(paramter.getDataName()))
		{
			mapperPackageList.add(paramter.getValue("PACKAGE"));
		} else if ("DBZONE".equals(paramter.getDataName()))
		{
			sqlMapTemplate = paramter.getValue("SQLMAP");
			dataSourceName = paramter.getValue("DATASOURCE") + "-"
					+ info.getId();
			dataSourceConfig = paramter.getValue("DBCONFIG");
		}
	}

	/**
	 * @return the dbSourceName
	 */
	public String getDataSourceName()
	{
		return dataSourceName;
	}

	/**
	 * @return the dbSourcePath
	 */
	public String getDataSourceConfig()
	{
		return dataSourceConfig;
	}

	@Override
	public void onComplete()
	{
	}

	/**
	 * @return the templatePath
	 */
	public String getSqlMapTemplate()
	{
		return sqlMapTemplate;
	}

	public void regMapperName(String pcakgename, String itemname)
	{
		this.mapperItems.add(new PackageItemInfo(pcakgename, itemname));
	}

	public int mapperCount()
	{
		return mapperItems.size();
	}

	public Collection<PackageItemInfo> getMapperItems()
	{
		return mapperItems;
	}

	public Collection<String> getMapperPackages()
	{
		return mapperPackageList;
	}

	public MappedStatement getMappedStatement(String statementid)
	{
		return ((SqlMapClientImpl) sqlMapClient)
				.getMappedStatement(statementid);
	}

	public static Class<?> getMappedStatementParamClass(
			MappedStatement statement)
	{
		Class<?> pclass = null;
		ParameterMap tmpParameterMap = statement.getParameterMap();
		if (tmpParameterMap != null)
			pclass = tmpParameterMap.getParameterClass();
		if (pclass == null)
			pclass = statement.getParameterClass();
		return pclass;
	}

	public static Class<?> getMappedStatementResultClass(
			MappedStatement statement)
	{
		Class<?> pclass = null;
		ResultMap tmpResultMap = statement.getResultMap();
		if (tmpResultMap != null)
			pclass = tmpResultMap.getResultClass();
		return pclass;
	}

	public static boolean isSelectStatement(MappedStatement statement)
	{
		return (statement.getStatementType() == StatementType.SELECT);
	}

	public static boolean isUpdateStatement(MappedStatement statement)
	{
		return (statement.getStatementType() == StatementType.UPDATE);
	}

	public static boolean isDeleteStatement(MappedStatement statement)
	{
		return (statement.getStatementType() == StatementType.DELETE);
	}

	public static boolean isInsertStatement(MappedStatement statement)
	{
		return (statement.getStatementType() == StatementType.INSERT);
	}

	public static boolean isProcedureStatement(MappedStatement statement)
	{
		return (statement.getStatementType() == StatementType.PROCEDURE);
	}

	public static boolean isUnknownStatement(MappedStatement statement)
	{
		return (statement.getStatementType() == StatementType.UNKNOWN);
	}

	public Object queryForObject(String statementId, Object param)
			throws SQLException
	{
		return sqlMapClient.queryForObject(statementId, param);
	}

	public Object queryForObject(String statementId) throws SQLException
	{
		return sqlMapClient.queryForObject(statementId);
	}

	public Object queryForList(String statementId, Object param)
			throws SQLException
	{
		return sqlMapClient.queryForList(statementId, param);
	}

	public Object queryForList(String statementId) throws SQLException
	{
		return sqlMapClient.queryForList(statementId);
	}

	public Object insert(String statementId, Object param) throws SQLException
	{
		return sqlMapClient.insert(statementId, param);
	}

	public Object insert(String statementId) throws SQLException
	{
		return sqlMapClient.insert(statementId);
	}

	public Object update(String statementId, Object param) throws SQLException
	{
		return sqlMapClient.update(statementId, param);
	}

	public Object update(String statementId) throws SQLException
	{
		return sqlMapClient.update(statementId);
	}

	public Object delete(String statementId, Object param) throws SQLException
	{
		return sqlMapClient.delete(statementId, param);
	}

	public Object delete(String statementId) throws SQLException
	{
		return sqlMapClient.delete(statementId);
	}

	public boolean startTransaction() throws SQLException
	{
		try
		{
			sqlMapClient.startTransaction();
			sqlMapClient.startBatch();
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void commitTransaction() throws SQLException
	{
		sqlMapClient.executeBatch();
		sqlMapClient.commitTransaction();
	}

	public void endTransaction() throws SQLException
	{
		sqlMapClient.endTransaction();
	}

	public GamedbInfoBean getConnectionInfo()
	{
		return info;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((info == null) ? 0 : info.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DBZone other = (DBZone) obj;
		if (info == null) {
			if (other.info != null)
				return false;
		} else if (!info.equals(other.info))
			return false;
		return true;
	}
	
	
}
