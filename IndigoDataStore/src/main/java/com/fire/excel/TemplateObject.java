package com.fire.excel;

import java.io.Serializable;


@ExcelRowBinding
public abstract class TemplateObject implements Serializable
{

	@ExcelCellBinding
	public int id;
	
	public TemplateObject()
	{
	}

	public final int getId()
	{
		return id;
	}

	public final void setId(int id)
	{
		this.id = id;
	}

	public void check() throws TemplateConfigException
	{
		
	}
	
	public void onLoadFinished()
	{
		
	}

	public void patchUp() throws Exception
	{
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TemplateObject other = (TemplateObject) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Id=["+id+"],";
	}

}
