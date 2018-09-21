package com.fire.core.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class PackageScaner
{
	private static final Logger logger = Logger.getLogger(PackageScaner.class);

	public static String[] scanNamespaceFiles(String namespace, String fileext, boolean checkSub)
	{
		String respath = namespace.replace('.', '/');
		respath = respath.replace('.', '/');
		
		
//		Resource resource = new ClassPathResource(respath);
		
		List<String> tmpNameList = new ArrayList<String>();
		
		try {
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			//获取所有匹配的文件
			Resource[] resources = resolver.getResources("/" + respath + "/**/*" + fileext);
			
			for(Resource r : resources) {
				String filename = r.getFilename();
				if(filename.indexOf(fileext) >= 0) {
					
					tmpNameList.add(filename);
				}
			}
			
//			BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
//			String fileName = null;
//			while((fileName = br.readLine()) != null) {
//				if(fileName.indexOf(".") >= 0) {
//					if(fileName.indexOf(fileext) >= 0) {
//						
//						tmpNameList.add(fileName);
//					}
//				}else {
//					String[] scanNamespaceFiles = scanNamespaceFiles(respath + "/" + fileName, fileext, checkSub);
//					if(scanNamespaceFiles != null) {
//						
//						for(String s : scanNamespaceFiles) {
//							tmpNameList.add(fileName + "." + s);
//						}
//					}
//				}
//			}
			
			
			String[] r = new String[tmpNameList.size()];
			tmpNameList.toArray(r);
			
			return r;
		}catch (Exception e) {
			logger.error("", e);
		}

//		try
//		{
//			URL url = null;
//			if (!respath.startsWith("/"))
//				url = PackageScaner.class.getResource("/" + respath);
//			else
//				url = PackageScaner.class.getResource(respath);
//			if(url == null){
//				System.out.println();
//			}
//			URLConnection tmpURLConnection = url.openConnection();
//			String tmpItemName;
//			if (tmpURLConnection instanceof JarURLConnection)
//			{
//				JarURLConnection tmpJarURLConnection = (JarURLConnection) tmpURLConnection;
//				int tmpPos;
//				String tmpPath;
//				JarFile jarFile = tmpJarURLConnection.getJarFile();
//				Enumeration<JarEntry> entrys = jarFile.entries();				
//				while (entrys.hasMoreElements())
//				{
//					JarEntry tmpJarEntry = entrys.nextElement();
//					if (!tmpJarEntry.isDirectory())
//					{
//						tmpItemName = tmpJarEntry.getName();
//						if (tmpItemName.indexOf('$') < 0
//								&& (fileext == null || tmpItemName.endsWith(fileext)))
//						{
//							tmpPos = tmpItemName.lastIndexOf('/');
//							if (tmpPos > 0)
//							{
//								tmpPath = tmpItemName.substring(0, tmpPos);
//								if(checkSub){
//									if (tmpPath.startsWith(respath))
//									{
//										
//										String r = tmpItemName.substring(respath.length()+1).replaceAll("/", ".");
//										tmpNameList.add(r);
//									}	
//								}else{
//									if (respath.equals(tmpPath))
//									{
//										tmpNameList.add(tmpItemName.substring(tmpPos + 1));
//									}
//								}
//								
//							}
//						}
//					}
//				}
//			}
//			else if (tmpURLConnection instanceof FileURLConnection)
//			{
//				File file = new File(url.getFile());
//				if (file.exists() && file.isDirectory())
//				{
//					File[] fileArray = file.listFiles();
//					for (File f : fileArray)
//					{
//						if(f.isDirectory() && f.getName().indexOf(".")!=-1)
//							continue;
//						
//						tmpItemName = f.getName();
//						if(f.isDirectory() && checkSub){
//							String[] inner = scanNamespaceFiles(namespace+"."+tmpItemName, fileext,checkSub);
//							if(inner == null){
//								continue;
//							}
//							for(String i : inner){
//								if(i!=null){
//									tmpNameList.add(tmpItemName+"."+i);
//								}
//							}
//						}else if(fileext == null || tmpItemName.endsWith(fileext) )
//						{
//							tmpNameList.add(tmpItemName);
//						}else{
//							continue;// 明确一下，不符合要求就跳过
//						}
//					}
//				}
//				else
//				{
//					logger.error("scaning stop,invalid package path:" + url.getFile());
//				}
//			}
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		if (tmpNameList.size() > 0)
//		{
//			String[] r = new String[tmpNameList.size()];
//			tmpNameList.toArray(r);
//			tmpNameList.clear();
//			return r;
//		}
		return null;
	}

	public static void main(String[] args) throws IOException
	{
//		JarPathLoader.getNewJarLoader("/dist/server-core-1.0.0.jar");
//		URLClassLoader c = JarClassLoader.getClassLoad("D:/t4game/workspace3/GameServerCore/dist", true);
//		
//		System.out.println(c.getResource("com/t4game/test/classloader/test.txt"));
//		String[] files = scanNamespaceFiles("com.t4game.test", "txt",false);
//		for (int i = 0; files != null && i < files.length; i++)
//		{
//			System.out.println(files[i]);
//		}
//		System.out.println("**********************");
//		files = scanNamespaceFiles("com.fire.game.action.activity", ".class");
//		for (int i = 0; files != null && i < files.length; i++)
//		{
//			System.out.println(files[i]);
//		}
		
//		JarFile jarFile = new JarFile("dist/server-core-1.0.0.jar");
//		Enumeration<JarEntry> entrys=jarFile.entries();    
//		
//	    while(entrys.hasMoreElements())   
//	    {    
//              JarEntry  entry = (JarEntry)entrys.nextElement();    
//              if   (entry.isDirectory())  
//            	  continue;    
//              System.out.println(entry.getName());
//	    } 
	}
}
