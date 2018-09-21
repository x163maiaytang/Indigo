package com.fire.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.fire.vector.Vector3D;

/**
 * 寻路核心类
 *
 */
public class PathFinding {
	
	private static final Logger logger = Logger.getLogger(PathFinding.class);
	
	public static void main(String[] args) {
		
		
//		float[] aa = findPath(100, new float[]{201.3f, 149.37f, 189.23f}, new float[]{156.68f, 171.02f, 142.35f}, 1);
//		
//		boolean in = isPosInBlock(100, new Vector3D(275.77f, 183.35f, 156.23f));
//		
//		
//		
//		boolean out = isPosInBlock(100, new Vector3D(249.07f, 176.59f, 147.31f));
		
//		float[] posHeight = posHeight(100, new float[]{201.3f, 0,  189.23f});
		
//		float[] raycast = raycast(100, new float[]{309.416f, 149.933f, 275.157f}, new float[]{310.66f, 150.02f, 296.74f }, 1);
		
//		initialize();
//		Map<Integer, Integer> navMap = new HashMap<String, Integer>();
//		navMap.put("srv_CAIBakedNavmesh1.bytes", 1);
		
//		initialize(navMap);
		
		float posHeight = posHeight(1, 201.3f, 189.23f);
		
		System.out.println();
	}
	// nav文件所在目录及其后缀
	
	private static String baseDir = PathFinding.class.getResource("/").getPath();
	private static String navDir = baseDir + "nav/";
	private static String navSuffix = ".navmesh";

	private static boolean flag = false;
	private static Map<Integer, Integer> navIdMap = new HashMap<Integer, Integer>();
	
//	public static void initialize(Map<String, Integer> tempNavMap){
//		if(flag){
//			System.loadLibrary("recast");
////			checkFilesExist();
//			init(tempNavMap);
//		}
//	}
	
	public static void initialize(Map<String, List<Integer>> nameMap){
//		if(flag){
		Properties prop = System.getProperties();
		String os = prop.getProperty("os.name");
		if(os.indexOf("Win") > -1 || os.indexOf("win") > -1){
			
			System.loadLibrary("recast");
		}else{
			
			System.load(baseDir + "libDetour.so");
			System.load(baseDir + "librecast.so");			
		}
			
			
			// 遍历nav文件所在目录
			File dir = new File(navDir);
			if(!dir.isDirectory())	return;
			
			File[] files = dir.listFiles();
			int stageSn = 0;
			for(File f : files) {
				stageSn ++;
				// 过滤不是nav文件
				String fileName = f.getName();
				if(!fileName.endsWith(navSuffix)) continue;
				
				String key = fileName.replace(navSuffix, "").replace("srv_", "");
				
				List<Integer> sceneClassIds = nameMap.get(key);
				if(sceneClassIds == null){
					logger.error("navmesh not exist scene = " + fileName);
					continue ;
				}
				
				for(int sceneClassId : sceneClassIds){
					navIdMap.put(sceneClassId, stageSn);
				}
				
				loadNavData(stageSn, f.getAbsolutePath());
				
				
				logger.info("==================load navmesh = " + fileName + "======================");
			}
//		}
	}
	
	/**
	 * 加载寻路用到的C++动态库recast.dll
	 * 初始化加载所有数据
	 */
	static {
	}
	
//	/**
//	 * 检查地图的xml和nav是否都存在
//	 */
//	private static void checkFilesExist() {
//		Collection<ConfStage> confList = ConfStage.findAll();
//		List<ConfStage> xmlLost = new ArrayList<>();
//		List<ConfStage> navLost = new ArrayList<>();
//		for(ConfStage conf : confList) {
//			int sn = conf.sn;
//			//判断xml文件和nav文件是否都存在，如果不存在则直接抛出异常
//			File navFile = new File(navDir + sn + navSuffix);
//			if(!navFile.exists()) {
//				navLost.add(conf);
//			}
//		}
//		
//		if(!navLost.isEmpty()) {
//			System.out.println("+++++navLost+++++以下地图缺失nav文件+++++navLost+++++");
//			for(ConfStage conf : navLost) {
//				System.out.println(conf.name + " " + conf.sn);
//			}
//		}
//		if(!xmlLost.isEmpty() || !navLost.isEmpty()) {
//			throw new SysException(Utils.createStr("有地图的xml或者nav文件缺失，无法启动！！！！！"));
//		}
//	}
	
	/**
	 * 遍历nav导航网格数据所在文件夹，初始化加载所有地图导航数据
	 * @param tempNavMap 
	 * @param navMap 
	 */
//	private static void init(Map<String, Integer> tempNavMap) {
//		
//		String path = PathFinding.class.getResource("/nav").getPath();
//		// 遍历nav文件所在目录
//		File dir = new File(path);
//		if(!dir.isDirectory())	return;
//		
//		File[] files = dir.listFiles();
//		for(File f : files) {
//			// 过滤不是nav文件
//			String fileName = f.getName();
//			if(!fileName.endsWith(navSuffix)) continue;
//			
//			Integer sceneClassId = tempNavMap.get(fileName.trim());
//			if(sceneClassId != null){
//				navMap.put(sceneClassId, fileName.trim());
//				loadNavData(sceneClassId, f.getAbsolutePath());
//			}else{
//				throw new RuntimeException("ERROR NAV FILE NAME = " + fileName.trim());
//			}
//		}
//	}
	
	/**
	 * 初始化并加载nav数据
	 * @param navPath
	 */
	private static native void loadNavData(int sceneClassId, String navPath);
	
//	private static native void loadNavData(int sceneClassId, byte[] bytes);
	
	/**
	 * 根据起点终点坐标找到路径
	 * @param startPos
	 * @param endPos
	 * @return
	 */
	public static List<Vector3D> findPaths(int sceneClassId, Vector3D startPos, Vector3D endPos) {
		return findPaths(sceneClassId, startPos, endPos, PathFindingFlagKey.walk);
	}
	
	/**
	 * 根据起点，终点，掩码寻路，返回Vector2路径列表
	 * @param startPos
	 * @param endPos
	 * @param flag
	 * @return
	 */
	public static List<Vector3D> findPaths(int sceneClassId, Vector3D startPos, Vector3D endPos, int flag) {
		
		Integer stagen = navIdMap.get(sceneClassId);
		if(stagen != null){
			
			// 转换坐标为float[]
			float[] start = startPos.toDetourFloat3();
			float[] end = endPos.toDetourFloat3();
			
			// 寻路结果
			float[] paths = findPath(stagen, start, end, flag);
			int len = paths.length;
			
			// 转成需要的Vector2
			List<Vector3D> result = new ArrayList<>();
			for(int i=0; i<len; i+=3) {
				Vector3D point = new Vector3D((int)(paths[i] * 100), (int)(paths[i+1]* 100) ,(int)(paths[i+2] *100));
				
				result.add(point);
			}
			
			return result;
		}
		
		return null;
	}
//	
	/**
	 * 根据起点，终点，掩码寻路
	 * @param startPos
	 * @param endPos
	 * @param flag
	 * @return
	 */
	private static native float[] findPath(int sceneClassId, float[] startPos, float[] endPos, int flag);
//	
//	/**
//	 * 判断坐标是否在阻挡区域内
//	 * @param pos
//	 * @return
//	 */
//	public static boolean isPosInBlock(int stageSn, Vector3D pos){
//		return isPosInBlock(stageSn, new float[]{(float)pos.y, (float)pos.z, (float)pos.x});
//	}
	
//	/**
//	 * 判断坐标是否在阻挡区域内
//	 * @param pos
//	 * @return
//	 */
//	private static native boolean isPosInBlock(int stageSn, float[] pos);
	
	/**
	 * 检测起点到终点是否有阻挡，有则返回阻挡坐标，无则返回终点坐标
	 * @param startPos
	 * @param endPos
	 * @return
	 */
	public static Vector3D raycast(int sceneClassId, Vector3D startPos, Vector3D endPos) {
		return raycast(sceneClassId, startPos, endPos, PathFindingFlagKey.walk);
	}
	
	/**
	 * 检测起点到终点是否有阻挡，有则返回阻挡坐标，无则返回终点坐标
	 * @param startPos
	 * @param endPos
	 * @param flag
	 * @return
	 */
	public static Vector3D raycast(int sceneClassId, Vector3D startPos, Vector3D endPos, int flag) {
		Integer stagen = navIdMap.get(sceneClassId);
		if(stagen != null){
			
			//转换坐标为float[]
			float[] start = startPos.toDetourFloat3();
			float[] end = endPos.toDetourFloat3();
			
			// 检测结果
			float[] paths = raycast(stagen, start, end, flag);
			// 转成需要的Vector2
			Vector3D result = new Vector3D((int)(paths[0] * 100), (int)(paths[1] * 100), (int)(paths[2] * 100));
			
			return result;
		}
		
		return endPos;
		
	}
	
	public static float[] raycast(int sceneClassId, float[] startPos, float[] endPos){
		return raycast(sceneClassId, startPos, endPos, PathFindingFlagKey.walk);
	}
	
	/**
	 * 检测起点到终点是否有阻挡，有则返回阻挡坐标，无则返回终点坐标
	 * @param startPos
	 * @param endPos
	 * @param flag
	 * @return
	 */
	private static native float[] raycast(int sceneClassId, float[] startPos, float[] endPos, int flag);
	
//	/**
//	 * 判断两个点是否能到达，判断标准为recase找到的终点与给定终点距离小于0.1
//	 * @param stageSn
//	 * @param startPos
//	 * @param endPos
//	 * @param flag
//	 * @return
//	 */
//	public static boolean canReach(int stageSn, Vector3D startPos, Vector3D endPos, int flag) {
//		List<Vector3D> paths = findPaths(stageSn, startPos, endPos, flag);
//		if(paths.isEmpty()) return false;
//		Vector3D pathEnd = paths.get(paths.size() - 1);
//		if(pathEnd.distance(endPos) <= 0.1) return true;
//		
//		return false;
//	}
	
	/**
	 * 获得坐标对应的高度
	 * @param pos
	 * @return
	 */
	private static native float[] posHeight(int sceneClassId, float[] pos);
	
//	public static Vector3D posHeight(int stageSn, Vector2D pos){
//		float[] temp = posHeight(stageSn, new float[]{(float)pos.y, 0, (float)pos.x});
//		Vector3D result = new Vector3D();
//		result.x = pos.x;
//		result.y = pos.y;
//		result.z = temp[1];
//		return result;
//	}
	
//	public static boolean posHeight(int sceneClassId, Vector3D pos){
//		Integer stagen = navIdMap.get(sceneClassId);
//		if(stagen != null){
//			
//			float[] temp = posHeight(stagen, new float[]{pos.getX() * 00.1f, 0, pos.getZ() * 0.01f});
//			pos.setY((int) (temp[1] * 100));
//			if(pos.getY() != 0) {
//				return true;
//			}
//			return false;
//		}
//		
//		return false;
//	}
	
	public static float posHeight(int sceneClassId, float x, float z){
		Integer stagen = navIdMap.get(sceneClassId);
		if(stagen != null){
			
			float[] temp = posHeight(stagen, new float[]{x, 0, z});
			
			return temp[1];
		}
		
		return 0;
	}
}
