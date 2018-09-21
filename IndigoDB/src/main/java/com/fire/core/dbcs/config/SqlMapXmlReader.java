package com.fire.core.dbcs.config;

import java.io.IOException;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.fire.core.dbcs.dbspace.DBZone;
import com.fire.core.util.PackageScaner;

/**
 * @author 
 * 
 */
public class SqlMapXmlReader extends AbstractTagFileReader
{
	private static final Logger logger = Logger.getLogger(SqlMapXmlReader.class);

	private static final String SQL_MAP_TAG = "sqlMap";
	private static final String DB_SOURCE_TAG = "dbSource";

	public static final String SUFFIX = ".xml";

	private DBZone refDbZone;
	private String content;

	public SqlMapXmlReader(DBZone dbzone)
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
				content = this.read(refDbZone.getSqlMapTemplate());
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
		if (SQL_MAP_TAG.equals(tag))
			return transferSqlMapTag();
		else if (DB_SOURCE_TAG.equals(tag))
			return transferDBSourceTag();
		else
			return null;
	}

	public String transferDBSourceTag()
	{
		return refDbZone.getDataSourceName();
	}
	
	public String transferSqlMapTag()
	{

		Collection<String> tmpPackageNames = refDbZone.getMapperPackages();

		String[] files;
		StringBuilder content = new StringBuilder();
		for (String packageName : tmpPackageNames)
		{
			files = PackageScaner.scanNamespaceFiles(packageName, SUFFIX,true);
			if (files == null)
			{
				logger.error("Db Mapper path is empty:" + packageName);
			}
			else
			{
				for (String file : files)
				{
					String tmpPackageName = packageName;
					String fileName = null;
					String[] tmp = file.split("\\.");
					if(tmp.length > 2)
					{
						for(int i = 0 ; i < tmp.length - 2 ; i++)
						{
							String appendPath = tmp[i];  
							tmpPackageName += "."+appendPath;
						}
						fileName = tmp[tmp.length - 2];
						
					}
					else
						fileName = file.substring(0,file.indexOf("."));
					
					refDbZone.regMapperName(tmpPackageName, fileName + SUFFIX);
					String beanPath = tmpPackageName + "." + fileName;
					
					beanPath = beanPath.replace('.', '/');
					beanPath += SUFFIX;
					content.append("<sqlMap resource=\"");
					content.append(beanPath);
					content.append("\"/>\r\n");
				}
			}
		}
		return content.toString();
	}
}
