package game.net.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Date;

public class NetByteUtils
{

	public static final Object readObject(InputStream in) throws Exception
	{
		byte[] bufdata = new byte[in.available()];
		in.read(bufdata);

		ByteArrayInputStream byteArrayInput = new ByteArrayInputStream(bufdata);
		ObjectInputStream objIn = new ObjectInputStream(byteArrayInput);
		Object obj = objIn.readObject();

		byteArrayInput.close();
		objIn.close();
		return obj;
	}

	public static final void writeObject(OutputStream out, Object obj) throws Exception
	{
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		ObjectOutputStream objOut = new ObjectOutputStream(byteArrayOut);
		objOut.writeObject(obj);

		byte[] bytes = byteArrayOut.toByteArray();
		writeInt(out, bytes.length, false);
		out.write(bytes);

		objOut.close();
		byteArrayOut.close();
	}

	// public final static byte[] getProtoBufBytes(ByteBuf buf) {
	// byte[] bytes = new byte[buf.readableBytes()];
	// buf.readBytes(bytes);
	// return bytes;
	// }
	//
	// public final static byte[] getBytes(ByteBuf buf) {
	// int length = buf.readInt();
	// byte[] bytes = new byte[length];
	// buf.readBytes(bytes);
	// return bytes;
	// }

	// *******************************************************
	public final static void writeString(OutputStream out, String s, boolean bigEndian)
	{
		try
		{
			if (s == null || s.length() == 0)
			{
				writeShort(out, (short) 0, bigEndian);
				return;
			}
			byte[] b = s.getBytes("UTF-8");
			writeShort(out, (short) b.length, bigEndian);
			out.write(b);
		}
		catch (Exception e)
		{

		}
	}

	/**
	 * 从Buffer中读取String
	 * 
	 * @param in
	 * @return
	 */

	public static final String readString(InputStream in, boolean bigEndian)
	{
		short length = readShort(in, bigEndian);
		if (length > 0)
		{
			try
			{
				byte[] bytes = new byte[length];
				in.read(bytes, 0, length);
				String result = new String(bytes, "UTF-8");
				return result;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * 向指定的Buffer中写入pString
	 * 
	 * @param out
	 * @param s
	 */
	public final static void writePstring(OutputStream out, String s, boolean bigEndian)
	{
		try
		{
			if (s == null || s.length() == 0)
			{
				writeShort(out, (short) 0, bigEndian);
				out.write(0);// 写#0
				return;
			}
			byte[] b = s.getBytes("UTF-8");
			writeShort(out, (short) b.length, bigEndian);
			out.write(b);
			out.write(0);// 写#0
		}
		catch (Exception e)
		{

		}
	}

	/**
	 * 从Buffer中读取pString
	 * 
	 * @param in
	 * @return
	 */

	public static final String readPstring(InputStream in, boolean bigEndian)
	{
		short length = readShort(in, bigEndian);
		if (length > 0)
		{
			try
			{
				byte[] bytes = new byte[length];
				in.read(bytes, 0, length);
				in.read();// 读#0
				String result = new String(bytes, "UTF-8");
				return result;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				in.read();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}// 读#0
		}
		return "";
	}

	public static byte boolean2Byte(boolean value)
	{
		return (byte) (value ? 1 : 0);
	}

	public static boolean byte2Boolean(byte value)
	{
		if (value == 1)
			return true;
		else
			if (value == 0)
				return false;
			else
				throw new IllegalArgumentException("illegal boolean data:" + value);
	}

	public final static void putBoolean(OutputStream out, boolean value)
	{
		try
		{
			out.write(boolean2Byte(value));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public final static boolean getBoolean(InputStream in)
	{
		try
		{
			return byte2Boolean((byte) in.read());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public final static void writeBoolean(OutputStream out, boolean value)
	{
		try
		{
			out.write(boolean2Byte(value));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public final static boolean readBoolean(InputStream in)
	{
		try
		{
			return byte2Boolean((byte) in.read());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public final static byte[] intToBytes(int value, boolean bigEndian)
	{
		byte[] databuf = new byte[4];
		if (bigEndian)
		{
			databuf[0] = (byte) ((value >> 24) & 0xff);
			databuf[1] = (byte) ((value >> 16) & 0xff);
			databuf[2] = (byte) ((value >> 8) & 0xff);
			databuf[3] = (byte) (value & 0xff);
		}
		else
		{
			databuf[0] = (byte) (value & 0xff);
			databuf[0] = (byte) ((value >> 8) & 0xff);
			databuf[0] = (byte) ((value >> 16) & 0xff);
			databuf[0] = (byte) ((value >> 24) & 0xff);
		}
		return databuf;
	}

	public final static void writeInt(OutputStream out, int value, boolean bigEndian)
	{
		try
		{
			if (bigEndian)
			{
				out.write((value >> 24) & 0xff);
				out.write((value >> 16) & 0xff);
				out.write((value >> 8) & 0xff);
				out.write(value & 0xff);
			}
			else
			{
				out.write(value & 0xff);
				out.write((value >> 8) & 0xff);
				out.write((value >> 16) & 0xff);
				out.write((value >> 24) & 0xff);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public final static int bytesToInt(byte[] in, boolean bigEndian)
	{
		if(in==null || in.length<4)
			return 0;
			
		if (bigEndian)
		{
			return ((in[0] & 0xff) << 24) + ((in[1] & 0xff) << 16) + ((in[2] & 0xff) << 8)
					+ (in[3] & 0xff);
		}
		else
		{
			return (in[0] & 0xff) + ((in[1] & 0xff) << 8) + ((in[2] & 0xff) << 16)
					+ ((in[3] & 0xff) << 24);
		}
	}
	
	public final static int readInt(InputStream in, boolean bigEndian)
	{
		try
		{
			if (bigEndian)
			{
				return ((in.read() & 0xff) << 24) + ((in.read() & 0xff) << 16) + ((in.read() & 0xff) << 8)
						+ (in.read() & 0xff);
			}
			else
			{
				return (in.read() & 0xff) + ((in.read() & 0xff) << 8) + ((in.read() & 0xff) << 16)
						+ ((in.read() & 0xff) << 24);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	public final static void writeLong(OutputStream out, long value, boolean bigEndian)
	{
		try
		{
			if (bigEndian)
			{
				out.write(((int) (value >> 56) & 0xff));
				out.write(((int) (value >> 48) & 0xff));
				out.write(((int) (value >> 40) & 0xff));
				out.write(((int) (value >> 32) & 0xff));
				out.write(((int) (value >> 24) & 0xff));
				out.write(((int) (value >> 16) & 0xff));
				out.write(((int) (value >> 8) & 0xff));
				out.write(((int) (value & 0xff)));
			}
			else
			{
				out.write(((int) (value & 0xff)));
				out.write(((int) (value >> 8) & 0xff));
				out.write(((int) (value >> 16) & 0xff));
				out.write(((int) (value >> 24) & 0xff));
				out.write(((int) (value >> 32) & 0xff));
				out.write(((int) (value >> 40) & 0xff));
				out.write(((int) (value >> 48) & 0xff));
				out.write(((int) (value >> 56) & 0xff));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public final static long readLong(InputStream in, boolean bigEndian)
	{
		try
		{
			if (bigEndian)
			{
				return (((long) (in.read() & 0xff)) << 56) + (((long) (in.read() & 0xff)) << 48)
						+ (((long) (in.read() & 0xff)) << 40) + (((long) (in.read() & 0xff)) << 32)
						+ (((long) (in.read() & 0xff)) << 24) + (((long) (in.read() & 0xff)) << 16)
						+ (((long) (in.read() & 0xff)) << 8) + ((long) (in.read() & 0xff));
			}
			else
			{
				return ((long) (in.read() & 0xff)) + (((long) (in.read() & 0xff)) << 8)
						+ (((long) (in.read() & 0xff)) << 16) + (((long) (in.read() & 0xff)) << 24)
						+ (((long) (in.read() & 0xff)) << 32) + (((long) (in.read() & 0xff)) << 40)
						+ (((long) (in.read() & 0xff)) << 48) + (((long) (in.read() & 0xff)) << 56);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0l;
	}

	public final static void writeShort(OutputStream out, short value, boolean bigEndian)
	{
		try
		{
			if (bigEndian)
			{
				out.write((value >> 8) & 0xff);
				out.write(value & 0xff);
			}
			else
			{
				out.write(value & 0xff);
				out.write((value >> 8) & 0xff);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public final static short readShort(InputStream in, boolean bigEndian)
	{
		try
		{
			if (bigEndian)
			{
				return (short) (((in.read() & 0xff) << 8) + (in.read() & 0xff));
			}
			else
			{
				return (short) ((in.read() & 0xff) + ((in.read() & 0xff) << 8));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	public final static void writeByte(OutputStream out, byte value)
	{
		try
		{
			out.write(value);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public final static byte readByte(InputStream in)
	{
		try
		{
			return (byte) in.read();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	public final static void writeFloat(OutputStream out, float value, boolean bigEndian)
	{
		try
		{
			int fbit = Float.floatToIntBits(value);
			if (bigEndian)
			{
				out.write((fbit >> 24) & 0xff);
				out.write((fbit >> 16) & 0xff);
				out.write((fbit >> 8) & 0xff);
				out.write(fbit & 0xff);
			}
			else
			{
				out.write(fbit & 0xff);
				out.write((fbit >> 8) & 0xff);
				out.write((fbit >> 16) & 0xff);
				out.write((fbit >> 24) & 0xff);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public final static float readFloat(InputStream in, boolean bigEndian)
	{
		int fbit = 0;
		try
		{
			if (bigEndian)
			{
				fbit = ((in.read() & 0xff) << 24) + ((in.read() & 0xff) << 16) + ((in.read() & 0xff) << 8)
						+ (in.read() & 0xff);
			}
			else
			{
				fbit = (in.read() & 0xff) + ((in.read() & 0xff) << 8) + ((in.read() & 0xff) << 16)
						+ ((in.read() & 0xff) << 24);
			}
			return Float.intBitsToFloat(fbit);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0.0f;
	}

	public final static void writeDate(OutputStream out, Date value, boolean bigEndian)
	{
		long ld = value.getTime();
		writeLong(out, ld, bigEndian);
	}

	public final static Date readDate(InputStream in, boolean bigEndian)
	{
		long ld = readLong(in, bigEndian);
		return new Date(ld);
	}

	public final static void writeByets(OutputStream out, byte[] value, boolean bigEndian)
	{
		int ds = (value != null) ? value.length : 0;
		writeShort(out, (short) ds, bigEndian);
		if (ds > 0)
		{
			try
			{
				out.write(value);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public final static void writeBigByets(OutputStream out, byte[] value, boolean bigEndian)
	{
		int ds = (value != null) ? value.length : 0;
		writeInt(out,  ds, bigEndian);
		if (ds > 0)
		{
			try
			{
				out.write(value);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private final static byte[] EMPTY_BYTES = new byte[0];

	public final static byte[] readBytes(InputStream in, boolean bigEndian)
	{
		int ds = readShort(in, bigEndian);
		if (ds > 0)
		{
			byte[] result = new byte[ds];
			try
			{
				in.read(result);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return result;
		}
		else
			return EMPTY_BYTES;
	}
	
	public final static byte[] readBigBytes(InputStream in, boolean bigEndian)
	{
		int ds = readInt(in, bigEndian);
		if (ds > 0)
		{
			byte[] result = new byte[ds];
			try
			{
				in.read(result);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return result;
		}
		else
			return EMPTY_BYTES;
	}
}
