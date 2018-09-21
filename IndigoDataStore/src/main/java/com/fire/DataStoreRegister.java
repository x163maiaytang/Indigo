package com.fire;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fire.core.util.PackageScaner;
import com.fire.datastore.DataBlockAnnotationInfo;
import com.fire.datastore.DataStore;
import com.fire.datastore.IDataBlock;

public class DataStoreRegister
{
	private static final Logger logger = Logger.getLogger(DataStoreRegister.class);

	private DataStoreRegister()
	{
	}

	public void _reload(String... eventPackagePath) throws ClassNotFoundException
	{
		
		List<Class<? extends IDataBlock>> dataBlocks = new ArrayList<Class<? extends IDataBlock>>();
		for(String p : eventPackagePath) {
			
			String path = p;
			String ext = ".class";
			String[] include = PackageScaner.scanNamespaceFiles(path, ext, true);
			
			if (include != null && include.length > 0)
			{
				String key;
				for (String includeClass : include)
				{
					
					key = path
							+ "."
							+ includeClass.subSequence(0, includeClass.length()
									- (ext.length()));
					
					Class<? extends IDataBlock> dataBlockClass = (Class<? extends IDataBlock>) Class.forName(key);
					DataBlockAnnotationInfo annotation = (DataBlockAnnotationInfo) dataBlockClass
							.getAnnotation(DataBlockAnnotationInfo.class);
					if (annotation != null)
					{
						
						if (!IDataBlock.class
								.isAssignableFrom(dataBlockClass))
						{
							logger.error("dataBlockClass source is invalid,source="
									+ dataBlockClass);
							return;
						}
						
						dataBlocks.add(dataBlockClass);
					}
				}
			}
			
		}
		DataStore.initialize(dataBlocks);
	}

	// **********************************
	private static DataStoreRegister instance = new DataStoreRegister();

	public static boolean reload(String... eventPackagePath)
	{
		try
		{
			instance._reload(eventPackagePath);
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
