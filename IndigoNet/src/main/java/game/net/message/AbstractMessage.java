package game.net.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;

public abstract class AbstractMessage implements Serializable
{

	private static Logger logger = Logger.getLogger(AbstractMessage.class);
	
	public final static byte[] EMPTY_BYTES=new byte[0];
	/**
	 * 消息commandId索引位置
	 */
	public static final int MESSAGE_COMMANDID_INDEX = 2;
	
	public static final String UP_SEQ_KEY = "UP_SEQ";
	public static final String DOWN_SEQ_KEY = "DOWN_SEQ";
	public static final String SEQ_ERROR_NUM = "SEQ_ERROR_NUM";

	/**
	 * 协议命令ID
	 */
	protected transient int commandId;
	
	/**
	 * length + commandId + sequenceId + body[] 
	 */
	protected transient int totalLength;
	/**
	 * 消息总长度
	 */
//	protected transient int bufferLength;
	/**
	 * 时间戳
	 */
	protected transient long timeStamp;

	protected long sessionId;

	private long curRoleId;
	
	/**
	 * 引用计数器
	 */
	protected transient AtomicInteger refCount =  new AtomicInteger();
	
	public AbstractMessage(int commandId)
	{
		this.commandId = commandId;
	}

	public AbstractMessage()
	{
	}


	@Override
	public String toString()
	{
		return getClass().getSimpleName() + ": commandId=" + commandId;
	}

	public void releaseMessage()
	{
		int left = this.refCount.decrementAndGet();
		if(left <= 0)
		{
			this.refCount.set(0);
			this.release();
		}
	}
	
	public void setRefNum(int num)
	{
		this.refCount.set(num);
	}
	
	public void addRefNum()
	{
		this.refCount.incrementAndGet();
	}
	
	public abstract void release();

	public long getTimeStamp()
	{
		return timeStamp;
	}

	public abstract void decode(byte[] bufdata);

	public abstract byte[] encode();

	/**
	 * 从Buffer中读取String
	 * 
	 * @param in
	 * @return
	 */
	public static final String readString(ByteBuf in)
	{
		short length = in.readShort();
		try
		{
			byte[] bytes = new byte[length];
			in.readBytes(bytes);
			String result = new String(bytes, "UTF-8");
			return result;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}

	public static final Object readObject(ByteBuf in) throws Exception
	{

		byte[] bytes = getBytes(in);
		
		ByteArrayInputStream byteArrayInput = new ByteArrayInputStream(bytes);
		ObjectInputStream objIn = new ObjectInputStream(byteArrayInput);
		Object obj = objIn.readObject();
		
		byteArrayInput.close();
		objIn.close();
		return obj;
	}
	
	public static final void writeObject(ByteBuf out,Object obj) throws Exception
	{
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		ObjectOutputStream objOut = new ObjectOutputStream(byteArrayOut);
		objOut.writeObject(obj);
		
		byte[] bytes = byteArrayOut.toByteArray();
		out.writeInt(bytes.length);
		out.writeBytes(bytes);
		
		objOut.close();
		byteArrayOut.close();
	}
	
	public final static byte[] getProtoBufBytes(ByteBuf buf)
	{
		byte[] bytes = new byte[buf.readableBytes()];
		buf.readBytes(bytes);
		return bytes;
	}

	public final static byte[] getBytes(ByteBuf buf)
	{
		int length = buf.readInt();
		byte[] bytes = new byte[length];
		buf.readBytes(bytes);
		return bytes;
	}

	/**
	 * 向指定的Buffer中写入String
	 * 
	 * @param out
	 * @param s
	 */
	public final static void writeString(ByteBuf out, String s)
	{
		try
		{
			if (s == null || s.length() == 0)
			{
				out.writeShort((short) 0);
				return;
			}
			byte[] b = s.getBytes("UTF-8");
			out.writeShort((short) (b.length));
			out.writeBytes(b);
		} catch (Exception e)
		{

		}
	}

	public final void setCommandId(int commandId)
	{
		this.commandId = commandId;
	}

	public final int getCommandId()
	{
		return commandId;
	}
	
	public final void setTotalLength(int totalLength)
	{
		this.totalLength = totalLength;
	}

	public final int getTotalLength()
	{
		return totalLength;
	}

	public long getSessionId()
	{
		return sessionId;
	}

	public final static void putBoolean(ByteBuf out, boolean value)
	{
		out.writeByte(boolean2Byte(value));
	}

	public final static boolean getBoolean(ByteBuf in)
	{
		return byte2Boolean(in.readByte());
	}

//	public final int getBufferLength()
//	{
//		return bufferLength;
//	}

	public void setSessionId(long sessionId)
	{
		this.sessionId = sessionId;
	}
	
	public void setCurRoleId(long roleId){
		this.curRoleId = roleId;
	}
	
	public long getCurRoleId() {
		return curRoleId;
	}
	
	public void setTimeStamp(long timeStamp)
	{
		this.timeStamp = timeStamp;
	}

	public static byte boolean2Byte(boolean value)
	{
		return (byte) (value ? 1 : 0);
	}
	
	public static boolean byte2Boolean(byte value)
	{
		if(value == 1)
			return true;
		else if(value == 0)
			return false;
		else
			throw new IllegalArgumentException("无效的BOOLEAN预定义值:" + value);
	}
}
