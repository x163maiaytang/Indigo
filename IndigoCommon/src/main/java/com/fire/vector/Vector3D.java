package com.fire.vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Vector3D extends Vector2D {
	
	private static AtomicInteger pathSerialIdGener = new AtomicInteger(0);
	private int pathSerialId;
	
//	private int x;
	private int y;
//	private int z;
	
	
	public Vector3D() {
		super();
		pathSerialId = pathSerialIdGener.addAndGet(1);
		
		if(pathSerialIdGener.get() >= Integer.MAX_VALUE){
			pathSerialIdGener.set(0);
		}
	}

	public Vector3D(int x, int y, int z) {
		this();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
//	public int getX() {
//		return x;
//	}
//	public void setX(int x) {
//		this.x = x;
//	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
//	public int getZ() {
//		return z;
//	}
//	public void setZ(int z) {
//		this.z = z;
//	}

	public int getSerialId(){
		return pathSerialId;
	}
	
	public static int convert2Int(double faceDir) {
		return (int) (faceDir * 100);
	}

//	public static double vector2Dir(Vector2D faceVector){
//		return TacticsUtils.atan2(faceVector.getX(), faceVector.getZ());
//	}
	
	public void moveTo(Vector3D pos){
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + z;
		return result;
	}

	@Override
	public String toString() {
		return "Vector3D [pathSerialId=" + pathSerialId + ", x=" + x + ", y="
				+ y + ", z=" + z + "]";
	}

 

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector3D other = (Vector3D) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}

	public static List<Vector3D> toArrayList(int[] posx, int[] posy, int[] posz) {
		List<Vector3D> tempList = new ArrayList<Vector3D>();
		for(int index = 0; index < posx.length; index ++){
			tempList.add(new Vector3D(posx[index], posy[index], posz[index]));
		}
		
		return tempList;
	}
	
	public static Vector3D toArray(int posx, int posy, int posz) {
		return new Vector3D(posx, posy ,posz);
	}

	public float[] toDetourFloat3() {
		return new float[]{x * 0.01f, y * 0.01f, z * 0.01f};
	}
	
}
