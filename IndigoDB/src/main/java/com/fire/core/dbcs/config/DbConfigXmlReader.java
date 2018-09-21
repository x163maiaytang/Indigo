package com.fire.core.dbcs.config;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.fire.core.dbcs.dbspace.DBZone;

/**
 * 数据库配置文件列表解析器
 * 
 * @author 
 * 
 */
public class DbConfigXmlReader extends AbstractTagFileReader
{
	private static final Logger logger = Logger.getLogger(DbConfigXmlReader.class);

	private static final String MYSQL_IP_TAG = "mysqlIp";
	private static final String MYSQL_DB_NAME_TAG = "mysqlDbName";
	private static final String MYSQL_USER_NAME_TAG = "mysqlUserName";
	private static final String MYSQL_PASSWORD_TAG = "mysqlPassword";
	private static final String MYSQL_PORT_TAG = "port";
	private static final String MYSQL_ID_TAG = "id";

	public static final String SUFFIX = ".xml";

	private DBZone refDbZone;
	private String content;

	public DbConfigXmlReader(DBZone dbzone)
	{
		this.refDbZone = dbzone;
		this.content = null;
	}

	public String getContent()
	{
		if (content == null)
		{
			try
			{
				content = this.read(refDbZone.getDataSourceConfig());
			}
			catch (IOException e)
			{
				content = null;
				e.printStackTrace();
			}
		}
		return content;
	}

	@Override
	public String onTag(String tag)
	{
		if (MYSQL_IP_TAG.equals(tag))
			return refDbZone.getConnectionInfo().getIp();
		else if (MYSQL_DB_NAME_TAG.equals(tag))
			return refDbZone.getConnectionInfo().getDbName();
		else if (MYSQL_USER_NAME_TAG.equals(tag))
			return refDbZone.getConnectionInfo().getUsername();
		else if (MYSQL_PASSWORD_TAG.equals(tag))
			return refDbZone.getConnectionInfo().getPassword();
		else if (MYSQL_PORT_TAG.equals(tag))
			return refDbZone.getConnectionInfo().getPort();
		else if (MYSQL_ID_TAG.equals(tag))
			return String.valueOf(refDbZone.getConnectionInfo().getId());
		else
			return null;
	}
}
