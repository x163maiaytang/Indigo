package com.fire.excel;


public class TemplateConfig
{
	private String fileName;
	private Class<? extends TemplateObject> clz;

	public TemplateConfig(String fileName, Class<? extends TemplateObject> clz)
	{
		this.fileName = fileName;
		this.clz = clz;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getFileName()
	{
		return fileName;
	}

	public Class<? extends TemplateObject> getClz()
	{
		return clz;
	}

	public void setClz(Class<? extends TemplateObject> clz)
	{
		this.clz = clz;
	}
}
