package com.fire.excel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.fire.core.util.PackageScaner;

public class TemplateService implements ITemplateService
{
	private Map<Class<?>, Map<Integer, TemplateObject>> templateObjects;
	private Map<String, TemplateConfig> templateConfigs = new LinkedHashMap<String, TemplateConfig>();
	private TemplateFileParser objectsParser;
	private Map<String, byte[]> configDataMap;
	
	private Map<String, Class> nameClassMap = new HashMap<String, Class>();
	private static final Logger logger = Logger.getLogger(TemplateService.class);

	private TemplateService()
	{
	}

	public void init(Map<String, byte[]> configDataMap, String dataStorePackage)
	{
		
		try {
			reloadDataClass(dataStorePackage);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
		this.configDataMap = configDataMap;
		templateObjects = new LinkedHashMap<Class<?>, Map<Integer, TemplateObject>>();
		objectsParser = new TemplateFileParser();
		InputStream is = null;
		String fileName = null;

		for (Entry<String, byte[]> entry: configDataMap.entrySet())
		{
			try
			{	
				fileName = entry.getKey();
				TemplateConfig cfg = this.loadConfig(fileName);
				
				if(cfg != null){
					is = new ByteArrayInputStream(entry.getValue());
					this.objectsParser.parseXlsFile(cfg.getClz(), templateObjects, is);
					is.close();
				}
			} catch (Exception e)
			{
				throw new ConfigException(
						"Errors occurs while parsing xls file:" + fileName, e);
			} finally
			{
				if (is != null)
				{
					try
					{
						is.close();
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void reloadDataClass(String eventPackagePath) throws ClassNotFoundException
	{
		String path = eventPackagePath + ".exceldata";
		String ext = ".class";
		String[] include = PackageScaner.scanNamespaceFiles(path, ext, true);
		
		if (include != null && include.length > 0)
		{
			String key;
			String fileName;
			for (String includeClass : include)
			{
				fileName = includeClass.substring(0, includeClass.indexOf("."));
				key = path + "." + fileName;

				Class dataBlockClass = Class.forName(key);
				if (dataBlockClass.getAnnotation(ExcelRowBinding.class) != null)
				{
					nameClassMap.put(fileName, dataBlockClass);
				}
			}
		}
	}

	private TemplateConfig loadConfig(String fileName)
	{
		try
		{
			Class clz = nameClassMap.get(fileName+"Data");
			
			if(clz == null){
				logger .error("======================" + fileName + "服务器未解析 ==================");
				
				return null;
			}
			if (!TemplateObject.class.isAssignableFrom(clz))
				throw new RuntimeException("exceldata的类没有继承TemplateObject:"
						+ clz);

			TemplateConfig templateConfig = new TemplateConfig(fileName,
					(Class<? extends TemplateObject>) clz);

			templateConfigs.put(fileName, templateConfig);
			return templateConfig;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public <T extends TemplateObject> T get(int id, Class<T> clazz)
	{
		Map<Integer, TemplateObject> map = templateObjects.get(clazz);
		return (T) map.get(id);
	}

	@Override
	public <T extends TemplateObject> Map<Integer, T> getAll(Class<T> clazz)
	{
		return (Map<Integer, T>) templateObjects.get(clazz);
	}

	public Map<String, TemplateConfig> getTemplateCfgs()
	{
		return templateConfigs;
	}

	@Override
	public <T extends TemplateObject> void add(T t)
	{
		Map<Integer, TemplateObject> objs = templateObjects.get(t.getClass());
		if (objs == null || objs.containsKey(t.getId()))
			return;

		objs.put(t.getId(), t);
	}

	@Override
	public <T extends TemplateObject> Map<Integer, T> removeAll(Class<T> class1)
	{
		return (Map<Integer, T>) templateObjects.remove(class1);
	}

	@Override
	public <T extends TemplateObject> boolean isTemplateExist(int id,
			Class<T> class1)
	{
		Map<Integer, TemplateObject> map = templateObjects.get(class1);
		if (null != map)
		{
			return null == map.get(id) ? false : true;
		}
		return false;
	}

	private boolean reload(String fileName,byte[] bytes)
	{
		try
		{
			if(bytes == null || fileName == null || "".equals(fileName))
				return false;
			
			configDataMap.put(fileName, bytes);
			TemplateConfig config = templateConfigs.get(fileName);
			if(config == null)
			{
				config = this.loadConfig(fileName);
				if(config == null)
					return false;
			}
				
			InputStream is = new ByteArrayInputStream(
					bytes);
			objectsParser.parseXlsFile(config.getClz(), templateObjects, is);
			is.close();
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * @param reloadData
	 * @return
	 */
	public boolean reload(Map<String, byte[]> reloadData)
	{
		boolean res = true;
		for(String fileName:reloadData.keySet())
			res &= this.reload(fileName,reloadData.get(fileName));
		
		return res;
	}

	// **********************************************
	private static TemplateService instance = new TemplateService();

	public static TemplateService getInstance()
	{
		return instance;
	}
}
