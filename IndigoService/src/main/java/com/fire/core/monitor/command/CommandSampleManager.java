package com.fire.core.monitor.command;

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


public class CommandSampleManager
{
	private static final Logger logger = Logger.getLogger(CommandSampleManager.class);

	/**
	 * 缓存样本队列 key为methodname
	 */
	private static Map<Integer, CommandSample> cache = new ConcurrentHashMap<Integer, CommandSample>();
	private static String excelName = "command_monitor.csv";

	private CommandSampleManager()
	{
	}

	public void release()
	{
		cache.clear();
	}

	/**
	 * 接收分析切片
	 */
	public boolean analy(ClipInfo info)
	{
		CommandSample sample = cache.get(info.getCommandId());
		if (sample == null)
		{
			sample = new CommandSample();
			sample.setCommandId(info.getCommandId());
			cache.put(info.getCommandId(), sample);
		}
		sample.addUp(info);
		return true;
	}

	public void putOutInfo(int count)
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
		
		List<CommandSample> list = new ArrayList<CommandSample>(cache.values());
		Collections.sort(list, new Comparator<CommandSample>()
		{
			@Override
			public int compare(CommandSample o1, CommandSample o2)
			{
				return o1.getTotalCost() > o2.getTotalCost() ? 1 : -1;
			}
		});

		try
		{
			pw.write(getCsvTitle() + "\r\n");
			for (CommandSample sample : list)
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
		return CommandSample.csvTitle();
	}

	// //////////////////////////////////////////////////////
	private static CommandSampleManager instance = new CommandSampleManager();

	public static CommandSampleManager getInstance()
	{
		return instance;
	}
}
