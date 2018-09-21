package com.fire.datastore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fire.core.util.dependence.DepandenceSorter;
import com.fire.core.util.dependence.DependenceScaner;
import com.fire.core.util.dependence.IDependence;
import com.fire.core.util.dependence.IDependenceAssembly;

public class DataStore implements IDependenceAssembly
{
	private static final Logger logger = Logger.getLogger(DataStore.class);

	private Map<String, DataBlockInfo> blockInfoMap = new HashMap<String, DataBlockInfo>();
	
	private Map<String, IDataBlock> tempMap = new HashMap<String, IDataBlock>();
	
	private Map<Class<? extends IDataBlock>, IDataBlock> blockImplMap = new HashMap<Class<? extends IDataBlock>, IDataBlock>();
	
	/**
	 * 排序后的List
	 */
	private List<String> dataBlockSortedList = new ArrayList<String>();
	
	@Override
	public IDependence getItem(String name)
	{
		if (name != null)
			return blockInfoMap.get(name);
		else
			return null;
	}

	public void init(List<Class<? extends IDataBlock>> classes)
	{
		for(Class c:classes)
		{
			DataBlockInfo tmpBlockInfo = new DataBlockInfo(c);
			this.blockInfoMap.put(c.getSimpleName(), tmpBlockInfo);
		}
		
		boolean check = this.checkDependRelation();
		if (!check)
			throw new RuntimeException("DataStore depaendRelation error");
		
		DepandenceSorter sorter = new DepandenceSorter();
		for (DataBlockInfo info : blockInfoMap.values())
			sorter.addElement(String.valueOf(info.getImplClass().getSimpleName()),
					info.getDependence());

		dataBlockSortedList = sorter.sort();
	}

	public void initAllDataBlock()
	{
		for(String dataBlockKey:dataBlockSortedList)
			this.loadDataBlock(dataBlockKey);
	}
	
	public boolean checkDependRelation()
	{
		Collection<DataBlockInfo> tmpBlockInfos = blockInfoMap.values();
		if (tmpBlockInfos != null)
		{
			for (DataBlockInfo tmpInfo : tmpBlockInfos)
			{
				if (!DependenceScaner.checkValidation(String.valueOf(tmpInfo.getImplClass().getSimpleName()),
						this))
				{
					logger.info("dependence of " + tmpInfo.getImplClass().getSimpleName()
							+ " is invalid");
					return false;
				}
			}
		}
		return true;
	}

	
	public void loadDataBlock(String blockname)
	{
		if (blockname == null)
			return;

		DataBlockInfo tmpBlockInfo = blockInfoMap.get(blockname);
		if (tmpBlockInfo == null)
		{
			logger.error("datablock " + blockname + " not registered");
			return;
		}
		
		if (!DependenceScaner.checkValidation(tmpBlockInfo.getImplClass().getSimpleName(), this))
		{
			logger.error("dependence of " + blockname + " is invalid");
			return;
		}

		IDataBlock tmpDataBlock = tmpBlockInfo.createImplementInstance();
		if (tmpDataBlock == null)
		{
			logger.error("datablock " + blockname
					+ " can't create instance");
			return;
		}

		blockImplMap.put(tmpBlockInfo.getImplClass(), tmpDataBlock);
		tempMap.put(tmpDataBlock.getDataClz().getSimpleName(), tmpDataBlock);
		if (!tmpDataBlock.initialize(false))
		{
			logger.error("datablock " + blockname + " can't initialize'");
		}
	}
	
	public void reload(Collection<String> reloadList)
	{
		
		if(reloadList != null){
			for(String blockname : reloadList)
			{
				IDataBlock block = tempMap.get(blockname + "Data") ;
				if(block != null){
					
					block.initialize(true);
				}else{
					System.out.println("reload 失败 = " + blockname);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends IDataBlock> T getDataBlock(Class<T> datablockclass)
	{
		if (datablockclass != null)
			return (T) blockImplMap.get(datablockclass);
		else
			return null;
	}

	private static DataStore instance;

	private synchronized static DataStore getInstance()
	{
		if (instance == null)
		{
			instance = new DataStore();
		}
		return instance;

	}

	public static void initialize(List<Class<? extends IDataBlock>> classes)
	{
		DataStore tmpDataStore = getInstance();
		if (tmpDataStore != null)
			tmpDataStore.init(classes);
	}
	
	public static void initDataBlock()
	{
		DataStore tmpDataStore = getInstance();
		if (tmpDataStore != null)
			tmpDataStore.initAllDataBlock();
	}

	public static <T extends IDataBlock> T findDataBlock(
			Class<T> datablockinterface)
	{
		DataStore tmpDataStore = getInstance();
		if (tmpDataStore != null)
		{
			return (T) tmpDataStore.getDataBlock(datablockinterface);
		}
		return null;
	}
	
	public static void reloadAllDataBlock(Collection<String> reloadList)
	{
		DataStore tmpDataStore = getInstance();
		if (tmpDataStore != null)
			tmpDataStore.reload(reloadList);
	}
}
