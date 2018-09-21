package com.fire.core.dbcs;

import java.util.Map;

public enum DBCSManager {
	DBCSMANAGER;
	
	/**
	 * 索引服务器
	 */
	private DBCS dbIndex;
	
	/**
	 * 逻辑服务器列表
	 */
	private Map<String,DBCS> dbLogicMap;
	
	
	public DBCS getDbIndex() {
		return dbIndex;
	}

	public void setDbIndex(DBCS dbIndex) {
		this.dbIndex = dbIndex;
	}

	public Map<String, DBCS> getDbLogicMap() {
		return dbLogicMap;
	}

	public void setDbLogicMap(Map<String, DBCS> dbLogicMap) {
		this.dbLogicMap = dbLogicMap;
	}
	
	
}
