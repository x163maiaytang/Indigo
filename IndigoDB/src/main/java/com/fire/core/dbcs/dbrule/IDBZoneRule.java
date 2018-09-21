package com.fire.core.dbcs.dbrule;

import com.fire.core.dbcs.dbspace.DBZone;

/**
 * @author 
 */
public interface IDBZoneRule
{
	public DBZone[] getDBZone(String ruleKey);
}
