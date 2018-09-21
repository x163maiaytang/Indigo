package com.fire.core.bhns.proxy;

import java.io.Serializable;

import game.net.message.AbstractMessage;
import io.netty.buffer.ByteBuf;

public class ProxyBean implements Serializable
{
	private static final long serialVersionUID = 5298464166939143549L;
//	protected static final CharsetDecoder CHARSETDECODER = Charset.forName(
//			"UTF-8").newDecoder();
//	protected static final CharsetEncoder CHARSETENCODER = Charset.forName(
//			"UTF-8").newEncoder();
	
	private long synKey;

	private int portalId;

	private long serviceId;

	private byte internalFlag;

	private int endpointId;

	private String methodSignature;

	private Object[] parameter = null;

//	private List<Long> waitActorIdList;

	public int getPortalId()
	{
		return portalId;
	}

	public void setPortalId(int behaviorID)
	{
		this.portalId = behaviorID;
	}

	public String getMethodSignature()
	{
		return methodSignature;
	}

	public void setMethodSignature(String methodSignature)
	{
		this.methodSignature = methodSignature;
	}

	public Object[] getParameter()
	{
		return parameter;
	}

	public void setParameter(Object... parameter)
	{
		this.parameter = parameter;
	}

	public byte getInternalFlag()
	{
		return internalFlag;
	}

	public void setInternalFlag(byte internalFlag)
	{
		this.internalFlag = internalFlag;
	}

	public int getEndpointId()
	{
		return endpointId;
	}

	public void setEndpointId(int endpointId)
	{
		this.endpointId = endpointId;
	}

	public void encode(ByteBuf out)
	{
		out.writeInt(portalId);
		out.writeLong(serviceId);
		out.writeByte(internalFlag);
		out.writeInt(endpointId);
		AbstractMessage.writeString(out, methodSignature);

//		if (waitActorIdList == null || waitActorIdList.size() <= 0)
//			out.writeByte((byte) 0);
//		else
//		{
//			out.writeByte((byte) waitActorIdList.size());
//			for (long id : waitActorIdList)
//				out.writeLong(id);
//		}

		if (this.parameter == null || this.parameter.length <= 0)
			out.writeByte((byte) 0);
		else
		{
			out.writeByte((byte) this.parameter.length);
			for (Object bean : this.parameter)
				try
				{
					AbstractMessage.writeObject(out, (Serializable) bean);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
		}
	}

	public void decode(ByteBuf in)
	{
		this.portalId = in.readInt();
		this.serviceId = in.readLong();
		this.internalFlag = in.readByte();
		this.endpointId = in.readInt();
		this.methodSignature = AbstractMessage.readString(in);

//		int size = in.readByte();
//		if (size > 0)
//		{
//			waitActorIdList = new ArrayList<Long>();
//			for (int i = 0; i < size; i++)
//				waitActorIdList.add(in.readLong());
//		}
//
//		size = in.readByte();
//		if (size <= 0)
//			return;
//		else
//		{
//			parameter = new Object[size];
//			for (int i = 0; i < size; i++)
//			{
//				try
//				{
//					parameter[i] = AbstractMessage.readObject(in);
//				} catch (Exception e)
//				{
//					e.printStackTrace();
//				}
//			}
//		}
	}

//	public List<Long> getWaitActorIdList()
//	{
//		return waitActorIdList;
//	}
//
//	public void setWaitActorIdList(List<Long> waitActorIdList)
//	{
//		this.waitActorIdList = waitActorIdList;
//	}

	public long getServiceId()
	{
		return serviceId;
	}

	public void setServiceId(long serviceId)
	{
		this.serviceId = serviceId;
	}

	public long getSynKey() {
		return synKey;
	}

	public void setSynKey(long synKey) {
		this.synKey = synKey;
	}
	
	
}
