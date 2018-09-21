package com.fire.core.dbcs.executor;

public class BatchBean {

	private Object[] args;
	private StatementProxy proxy;

	public BatchBean(StatementProxy proxy, Object[] args) {
		this.proxy = proxy;
		this.args = args;
	}

	public Object[] getArgs() {
		return args;
	}

	public StatementProxy getProxy() {
		return proxy;
	}

}
