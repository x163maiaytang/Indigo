package com.fire.core.util;

/**
 * synkey = ip4AddrCode| pid | threadid
 */
public class SynKeyUtil
{
//	private static long synkeyPrefix;
//
//	static
//	{
//		try
//		{
//			long ip4AddrCode = Inet4Address.getLocalHost().getHostAddress()
//					.hashCode();
//			long pid = Short.parseShort(ManagementFactory.getRuntimeMXBean()
//					.getName().split("@")[0]);
//			synkeyPrefix = ip4AddrCode << 48 | pid << 32;
//		} catch (UnknownHostException e)
//		{
//			e.printStackTrace();
//		}
//	}

	public static long getSynkey()
	{
//		if(Thread.currentThread().getClass() == WorkerThread.class)
//		{
//			Task runing = ((WorkerThread)(Thread.currentThread())).runningTask;
//			if(runing != null)
//				return runing.id();
//		}
//			return (Long) (CoreServiceConstants.IP4_ADDR_CODE << 32 | Thread.currentThread().getId());
		
		return 0;
	}

//	public static void main(String[] args)
//	{
//		System.out.println(getSynkey());
//	}
}
