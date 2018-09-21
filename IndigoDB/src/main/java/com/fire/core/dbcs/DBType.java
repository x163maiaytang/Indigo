package com.fire.core.dbcs;

public enum DBType {
	DB_INDEX(0,"索引服务器"),
	DB_LOGIC(1,"逻辑服务器");
	
	private int id;
	private String name;
	
	private DBType( int id, String name){
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
