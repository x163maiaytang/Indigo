package com.fire.core.util;
//package com.fire.core.util;
//
//import java.nio.ByteBuffer;
//
//import com.esotericsoftware.kryo.Kryo;
//
///**
// * 对象序列化，反序列化工具
// * @author liudongsheng
// */
//public class SerizObjectUtil
//{
//
//	private final static Kryo kryo = new Kryo();
//	//序列化对象
//	public static  byte[] getBytes(Object object ,Class T) {
//	
//		// kryo.setReferences(true);
//                // kryo.setRegistrationRequired(true);
//		// kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
//		//注册类	
//		RegisteredClass registration = kryo.register(T);
//		ByteBuffer buffer=ByteBuffer.allocate(4096);
//		kryo.writeObject(buffer, object);
//		byte[] bb = buffer.array();
//		return bb;
//	}
//	
//    //反序列化对象
//	public static <T> T getObject(byte[] bb ,Class T) {
//		
//		//注册类	
//		RegisteredClass registration = kryo.register(T);
//
//		ByteBuffer bufferObject=ByteBuffer.wrap(bb);
//		T object = (T) kryo.readObject(bufferObject, registration.getType());
//		return object;
//	}
//	
//	public static void main(String[] args)
//	{
////		PlayerNode student = new PlayerNode("zhangsan", 23);
////
////		long startTime=System.currentTimeMillis();
////		for(int i=0;i<=1000000;i++)
////		{
////		byte[] bb =SerizObjectUtil.getBytes(student,PlayerNode.class);
////		PlayerNode result=SerizObjectUtil.getObject(bb, PlayerNode.class);
//////		System.out.println(result.toString());
////		}
////		long endTime=System.currentTimeMillis();
////		System.out.println("time:" + (endTime-startTime)/(double)1000 +" s");
//	}
//	
//}
