package com.fire.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

public class ServerUtil {

	public static Object byteToObject(byte[] bytes) {
		Object obj = null;
		try {
			// bytearray to object
			ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
			ObjectInputStream oi = new ObjectInputStream(bi);

			obj = oi.readObject();
			bi.close();
			oi.close();
		}
		catch (Exception e) {
			System.out.println("translation" + e.getMessage());
			e.printStackTrace();
		}
		return obj;
	}

	public static byte[] objectToByte(Object obj) {
		byte[] bytes = null;
		try {
			// object to bytearray
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);

			bytes = bo.toByteArray();

			bo.close();
			oo.close();
		}
		catch (Exception e) {
			System.out.println("translation" + e.getMessage());
			e.printStackTrace();
		}
		return bytes;
	}

	// 将二进制流转为Hexstring
	public static String toHexString(byte[] bin) {
		if (bin.length > 0) {
			return "0x" + HexBin.encode(bin);
		}

		return null;
	}

	// 将Hexstring转为二进制
	public static byte[] fromHexString(String hexString) {
		if (hexString != null && hexString.startsWith("0x")) {
			return HexBin.decode(hexString.substring(2, hexString.length()));
		}

		return null;
	}
	
	public static String objectToHexString(Object o)
	{
		return toHexString(objectToByte(o));
	}
	
	public static Object hexStringToObject(String string)
	{
		return byteToObject(fromHexString(string));  
	}
}
