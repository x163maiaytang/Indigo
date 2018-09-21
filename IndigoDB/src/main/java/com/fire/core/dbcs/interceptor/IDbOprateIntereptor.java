package com.fire.core.dbcs.interceptor;

import com.fire.core.dbcs.executor.StatementProxy;

public interface IDbOprateIntereptor
{
	public Integer before(StatementProxy statement,Object[] params);
	
	public Object after(StatementProxy statement,Object[] params,Object result);

	public void doUpdate(long now);
}
