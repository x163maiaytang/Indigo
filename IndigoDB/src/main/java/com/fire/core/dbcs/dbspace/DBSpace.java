package com.fire.core.dbcs.dbspace;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fire.core.dbcs.DBCSConst;
import com.fire.core.dbcs.config.PackageItemInfo;

/**
 * 数据库连接信息集合 持有连接、映射规则等信息
 * 
 * @author
 * 
 */
public class DBSpace
{
	private static String DB_CONFIG_PATH = DBCSConst.DB_CONFIG_PATH;
	private static final String SQL_MAP_CONFIG = DBCSConst.SQL_MAP_CONFIG;

	private static Map<Integer, DBZone> dbZones = new HashMap<Integer, DBZone>();
	private static Map<String, DBZone> nameDbZones = new HashMap<String, DBZone>();

	public static DBZone getMainDBZone()
	{
		DBZone dbZone = getDBZone("main");
		if(dbZone != null) {
			return dbZone;
		}
		
		for(DBZone dz : nameDbZones.values()) {
			return dz;
		}
		
		return null;
	}
	
	public static DBZone getDBZone(String name)
	{
		return nameDbZones.get(name);
	}

	public static boolean initialize(String dbconfigpath,
			GamedbInfoBean... connectionInfos)
	{
		try
		{
			if (dbconfigpath != null)
				DB_CONFIG_PATH = dbconfigpath;
			System.setProperty("java.naming.factory.initial",
					"org.apache.naming.java.javaURLContextFactory");
			System.setProperty("java.naming.factory.url.pkgs",
					"org.apache.naming");

			for (GamedbInfoBean info : connectionInfos)
			{
				DBZone dbZone = new DBZone(info);
				dbZone.loadConfig(DB_CONFIG_PATH + "/" + SQL_MAP_CONFIG);
				dbZone.loadSqlMap();
				if(info.getType() == 1) {
					dbZones.put(info.getId(),dbZone);
				}else {
					nameDbZones.put(info.getName(), dbZone);
				}
			}
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static Map<Integer, DBZone> getDbZones()
	{
		return dbZones;
	}

	public static Collection<PackageItemInfo> getMapperItems() {
		for(DBZone dbzone : nameDbZones.values()) {
			if(dbzone != null) {
				return dbzone.getMapperItems();
			}
		}
		for(DBZone dbzone : dbZones.values()) {
			if(dbzone != null) {
				return dbzone.getMapperItems();
			}
		}
		return null;
	}
}
