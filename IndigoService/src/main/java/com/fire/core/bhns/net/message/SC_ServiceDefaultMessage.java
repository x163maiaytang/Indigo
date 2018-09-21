package com.fire.core.bhns.net.message;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.fire.core.bhns.proxy.ReturnObj;

import game.net.message.AbstractMessage;

public class SC_ServiceDefaultMessage extends AbstractMessage
{
//	private long synKey;

	private ReturnObj result;

	public SC_ServiceDefaultMessage()
	{
		super(1);
		result = new ReturnObj();
	}

	@Override
	public void release()
	{
		result = null;
	}

	@Override
	public void decode(byte[] bufdata)
	{
		try
		{
//			ByteArrayInputStream in = new ByteArrayInputStream(bufdata);
//			synKey = NetByteUtils.readLong(in, false);
	
	//		try
	//		{
	//			result.setObj(AbstractMessage.readObject(in));
	//		} catch (Exception e)
	//		{
	//			e.printStackTrace();
	//		}
			
			protostuffForDecode(bufdata);
			
//			System.out.println(bean.getMethodSignature() + " size = " + bufdata.length);
//			in.close();
//			in = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void protostuffForDecode(byte[] bufdata) throws Exception {
		if(bufdata != null){
//			byte[] bytes =NetByteUtils.readBytes(bufdata, false);
			Schema<ReturnObj> schema = RuntimeSchema.getSchema(ReturnObj.class);
			ProtostuffIOUtil.mergeFrom(bufdata, result, schema);
		}
	}
	
	@Override
	public byte[] encode()
	{
//		try
//		{
//			ByteArrayOutputStream out = new ByteArrayOutputStream();
//			NetByteUtils.writeLong(out, synKey, false);
//			try
//			{
//				AbstractMessage.writeObject(out, (Serializable) result.getObj());
//			} catch (Exception e)
//			{
//				e.printStackTrace();
//			}
//			
			return protostuffForEncode();
//			byte[] msgdata = out.toByteArray();
//			out.close();
//			out = null;
//			return msgdata;
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		return EMPTY_BYTES;
	}
	
	private byte[] protostuffForEncode(){
		
		if(result != null){
			
			Schema<ReturnObj> schema = RuntimeSchema.getSchema(ReturnObj.class);
			LinkedBuffer buffer = LinkedBuffer.allocate(4096);
			byte[] protostuff = null;
			try {
				protostuff = ProtostuffIOUtil.toByteArray(result, schema, buffer);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				buffer.clear();
			}
			
//			if (protostuff != null) {
//				NetByteUtils.writeByets(out, protostuff, false);
//			}
			
			return protostuff;
		}
		
		return EMPTY_BYTES;
	}


	public long getSynKey()
	{
		return result.getSynKey();
	}

	public void setSynKey(long synKey)
	{
		this.result.setSynKey(synKey);
	}

	public Object getResult()
	{
		return result.getObj();
	}

	public void setResult(Object resData)
	{
		this.result.setObj(resData);
	}
	
}
