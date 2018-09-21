package com.fire.core.dbcs.dbspace;

import java.io.Serializable;

public class GamedbInfoBean implements Serializable
{
	/**
		 *  */
	private int id;
	
	/**
	 * 
	 */
	private String name;

	/**
		 *  */
	private int areaId;

	/**
		 *  */
	private String ip;

	/**
		 *  */
	private String port;

	/**
		 *  */
	private String dbName;

	/**
		 *  */
	private String username;

	/**
		 *  */
	private String password;

	/**
		 *  */
	private int type;

	public GamedbInfoBean()
	{
		init();
	}

	/** Gets */
	public int getId()
	{
		return this.id;
	}

	/** Gets */
	public int getAreaId()
	{
		return this.areaId;
	}

	/** Gets */
	public String getIp()
	{
		return this.ip;
	}

	/** Gets */
	public String getPort()
	{
		return this.port;
	}

	/** Gets */
	public String getDbName()
	{
		return this.dbName;
	}

	/** Gets */
	public String getName()
	{
		return this.name;
	}

	/** Gets */
	public String getPassword()
	{
		return this.password;
	}

	/** Gets */
	public int getType()
	{
		return this.type;
	}

	/** Initializes the values */
	public void init()
	{
		this.id = 0;
		this.areaId = 0;
		this.ip = "";
		this.port = "";
		this.dbName = "";
		this.name = "";
		this.password = "";
		this.type = 0;
	}

	/** Sets */
	public void setId(int id)
	{
		this.id = id;
	}

	/** Sets */
	public void setAreaId(int areaId)
	{
		this.areaId = areaId;
	}

	/** Sets */
	public void setIp(String ip)
	{
		this.ip = ip;
	}

	/** Sets */
	public void setPort(String port)
	{
		this.port = port;
	}

	/** Sets */
	public void setDbName(String dbName)
	{
		this.dbName = dbName;
	}

	/** Sets */
	public void setName(String name)
	{
		this.name = name;
	}

	/** Sets */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/** Sets */
	public void setType(int type)
	{
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/** Returns the String representation */
	public String toString()
	{
		return "(GamedbInfoBean) " + "id='" + id + "', " + "areaId='" + areaId
				+ "', " + "ip='" + ip + "', " + "port='" + port + "', "
				+ "dbName='" + dbName + "', " + "username='" + username + "', "
				+ "password='" + "******" + "', " + "type='" + type + "'";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		GamedbInfoBean other = (GamedbInfoBean) obj;
		if (id != other.id)
			return false;
		return true;
	}

	/** Returns the CSV String */
	public String toCSVLine()
	{
		return "\"" + id + "\"," + "\"" + areaId + "\"," + "\"" + ip + "\","
				+ "\"" + port + "\"," + "\"" + dbName + "\"," + "\"" + name
				+ "\"," + "\"" + password + "\"," + "\"" + type + "\"";
	}
	
	
	

}