package com.fire.core.bhns.net.message;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.fire.core.bhns.proxy.ProxyBean;

import game.net.message.AbstractMessage;

public class CS_ServiceDefaultMessage extends AbstractMessage
{
//	private long synKey;
	/**
	 * 角色Id
	 */
//	private long roleId;

	private ProxyBean bean;

	public CS_ServiceDefaultMessage()
	{
		super(0);
	}

	@Override
	public void release()
	{
	}

	@Override
	public void decode(byte[] bufdata)
	{
		try
		{
//			ByteArrayInputStream in = new ByteArrayInputStream(bufdata);
//			roleId = NetByteUtils.readLong(in, false);
//			synKey = NetByteUtils.readLong(in, false);

//			bean = new ProxyBean();
//			bean.decode(in);
			
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
	
	private void protostuffForDecode(byte[] bufdata) throws Exception{
		if(bufdata != null){
//			byte[] bytes =NetByteUtils.readBytes(in, false);
			Schema<ProxyBean> schema = RuntimeSchema.getSchema(ProxyBean.class);
			bean = new ProxyBean();
			ProtostuffIOUtil.mergeFrom(bufdata, bean, schema);
		}
	}
	
	@Override
	public byte[] encode()
	{
//		try
//		{
//			ByteArrayOutputStream out = new ByteArrayOutputStream();
//			NetByteUtils.writeLong(out, roleId, false);
//			NetByteUtils.writeLong(out, synKey, false);
			
//			bean.encode(out);
			
			
//			byte[] msgdata = out.toByteArray();
//			out.close();
//			out = null;
//			return msgdata;
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
		return protostuffForEncode();
	}
	
	private byte[] protostuffForEncode() {
		if(bean != null){
			Schema<ProxyBean> schema = RuntimeSchema.getSchema(ProxyBean.class);
			LinkedBuffer buffer = LinkedBuffer.allocate(4096);
			try {
				return ProtostuffIOUtil.toByteArray(bean, schema, buffer);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				buffer.clear();
			}
			
//			if (protostuff != null) {
//				NetByteUtils.writeByets(out, protostuff, false);
//			}
		}
		return EMPTY_BYTES;
	}

	public long getSynKey()
	{
		return bean.getSynKey();
	}

	public void setSynKey(long synKey)
	{
		this.bean.setSynKey(synKey);
	}

	public long getRoleId()
	{
		return bean.getServiceId();
	}

//	public void setRoleId(long roleId)
//	{
//		this.roleId = roleId;
//	}

	public ProxyBean getBean()
	{
		return bean;
	}

	public void setBean(ProxyBean bean)
	{
		this.bean = bean;
	}
}
