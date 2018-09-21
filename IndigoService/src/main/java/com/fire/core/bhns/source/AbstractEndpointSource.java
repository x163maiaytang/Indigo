package com.fire.core.bhns.source;

public abstract class AbstractEndpointSource implements IEndpointSource{

	protected volatile boolean inited;

	public boolean isInited() {
		return inited;
	}

	public void setInited(boolean inited) {
		this.inited = inited;
	}
}
