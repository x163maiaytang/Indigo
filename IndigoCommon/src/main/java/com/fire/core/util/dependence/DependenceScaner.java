package com.fire.core.util.dependence;

import java.util.HashSet;
import java.util.Set;

public class DependenceScaner
{
	private static boolean internalCheckRelation(String itemname, IDependenceAssembly assembly,
			Set<String> itembuffer)
	{	
		IDependence tmpIDependence = assembly.getItem(itemname);
		if (tmpIDependence == null)
			return false;

		if (itembuffer.contains(itemname))
			return false;
		else
			itembuffer.add(itemname);

		boolean r = true;
		String[] tmpDependences = tmpIDependence.getDependence();
		if (tmpDependences != null && tmpDependences.length > 0)
		{
			for (int i = 0; r && i < tmpDependences.length; i++)
			{	
				if (itembuffer.contains(tmpDependences[i]))
					r = false;
				else
					r = internalCheckRelation(tmpDependences[i], assembly, itembuffer);
				
				if(!r)
					break;
			}
		}
		return r;
	}
	/**
	 * 
	 * @param itemname
	 * @param assembly
	 * @return false 依赖关系不合法. 
	 */
	public static boolean checkValidation(String itemname, IDependenceAssembly assembly)
	{
		if (itemname == null || assembly == null)
			return true;

		Set<String> tmpSet = new HashSet<String>();
		boolean r = internalCheckRelation(itemname, assembly, tmpSet);
		tmpSet.clear();
		tmpSet = null;
		return r;
	}

}
