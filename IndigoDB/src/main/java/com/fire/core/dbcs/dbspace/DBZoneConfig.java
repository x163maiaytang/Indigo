package com.fire.core.dbcs.dbspace;

import com.fire.core.util.xml.ISimpleParamters;

public class DBZoneConfig {
	
	private String id;
	private String type;
	private String ip;
	private String port;
	private String dbName;
	private String userName;
	private String password;
	private String sqlMapTemplate;
	private String dataSourceName;
	private String dataSourceConfig;
	private int rate;
	
	public DBZoneConfig(){
		
	}
	
	public DBZoneConfig(ISimpleParamters paramter){
		this.id = paramter.getValue("ID");
		this.type = paramter.getValue("TYPE");
		this.ip = paramter.getValue("IP");
		this.port = paramter.getValue("PORT");
		this.dbName = paramter.getValue("DBNAME");
		this.userName = paramter.getValue("USERNAME");
		this.password = paramter.getValue("PASSWORD");
		this.sqlMapTemplate = paramter.getValue("SQLMAP");
		this.dataSourceName = paramter.getValue("DATASOURCE");
		this.dataSourceConfig = paramter.getValue("DBCONFIG");
		this.rate = Integer.valueOf(paramter.getValue("RATE"));
	}
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSqlMapTemplate() {
		return sqlMapTemplate;
	}
	public void setSqlMapTemplate(String sqlMapTemplate) {
		this.sqlMapTemplate = sqlMapTemplate;
	}
	public String getDataSourceName() {
		return dataSourceName;
	}
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
	public String getDataSourceConfig() {
		return dataSourceConfig;
	}
	public void setDataSourceConfig(String dataSourceConfig) {
		this.dataSourceConfig = dataSourceConfig;
	}
	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}
	
}
