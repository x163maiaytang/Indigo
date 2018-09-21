package com.fire.core.dbcs.resulthandler;

import java.util.List;

public class ListAddupResultHandler implements IResultHandler<List>
{
	private List resultlist;

	@Override
	public boolean handleResult(List t)
	{
		if(resultlist == null)
			resultlist = t;
		else
		{
			if(resultlist != null)
				resultlist.addAll(t);
		}
		
		return false;
	}
	
	public List getResult()
	{
		return resultlist;
	}

}
