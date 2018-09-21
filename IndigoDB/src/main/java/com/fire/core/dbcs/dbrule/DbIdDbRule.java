package com.fire.core.dbcs.dbrule;

import com.fire.core.dbcs.dbspace.DBSpace;
import com.fire.core.dbcs.dbspace.DBZone;

public class DbIdDbRule implements IDBZoneRule
{

	@Override
	public DBZone[] getDBZone(String ruleKey)
	{
		return new DBZone[]{DBSpace.getDbZones().get(Integer.parseInt(ruleKey))};
	}

}
