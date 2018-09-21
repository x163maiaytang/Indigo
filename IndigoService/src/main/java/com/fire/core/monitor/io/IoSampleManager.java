package com.fire.core.monitor.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

public class IoSampleManager
{
	private static final Logger logger = Logger.getLogger(IoSampleManager.class);

	/**
	 * 缓存样本队列 key为methodname
	 */
	private static Map<Integer, IoSample> cache = new ConcurrentHashMap<Integer, IoSample>();
	private static String excelName = "io_monitor.csv";

	private IoSampleManager()
	{
	}

	public void release()
	{
		cache.clear();
	}

	/**
	 * 接收分析切片
	 */
	public boolean analy(int commandId,boolean isUp,int size)
	{
		IoSample sample = cache.get(commandId);
		if (sample == null)
		{
			sample = new IoSample();
			sample.setCommandId(commandId);
			cache.put(commandId, sample);
		}
		sample.addUp(isUp,size);
		return true;
	}

	public void putOutInfo()
	{
		OutputStreamWriter pw = null;
		try
		{
			File f = new File(excelName);
			if (f.exists())
				f.delete();

			if (!f.exists())
				f.createNewFile();

			pw = new OutputStreamWriter(new FileOutputStream(f));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		List<IoSample> list = new ArrayList<IoSample>(cache.values());
		Collections.sort(list, new Comparator<IoSample>()
		{
			@Override
			public int compare(IoSample o1, IoSample o2)
			{
				return o1.getTotalUpIo() > o2.getTotalUpIo() ? 1 : -1;
			}
		});

		try
		{
			pw.write(getCsvTitle() + "\r\n");
			for (IoSample sample : list)
			{
				pw.write(sample.csv() + "\r\n");
			}
			pw.flush();
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				pw.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	}

	private String getCsvTitle()
	{
		return IoSample.csvTitle();
	}

	// //////////////////////////////////////////////////////
	private static IoSampleManager instance = new IoSampleManager();

	public static IoSampleManager getInstance()
	{
		return instance;
	}
}
