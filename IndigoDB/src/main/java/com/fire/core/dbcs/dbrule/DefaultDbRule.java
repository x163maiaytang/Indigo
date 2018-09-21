package com.fire.core.dbcs.dbrule;

import com.fire.core.dbcs.dbspace.DBSpace;
import com.fire.core.dbcs.dbspace.DBZone;

public class DefaultDbRule implements IDBZoneRule
{
	public final static DefaultDbRule INSTNACE = new DefaultDbRule();
	
	@Override
	public DBZone[] getDBZone(String ruleKey)
	{
		return DBSpace.getDbZones().values()
				.toArray(new DBZone[DBSpace.getDbZones().size()]);
	}
}
