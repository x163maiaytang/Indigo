/**
 * 
 */
package com.fire.constant;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class CoreServiceConstants {
	
	public final static int BO_LOCATION = 1;
	public final static int BO_GATE = 2;
	 
	
	public static int IP4_ADDR_CODE;
	
	static
	{
		try
		{
			IP4_ADDR_CODE = Inet4Address.getLocalHost().getHostAddress().hashCode();
		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
	}

	public static List<Integer> getAllServiceId(int localService) {
		return null;
	}
	
}
