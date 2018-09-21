package com.fire.core.messageagent;

import game.net.message.AbstractMessage;

public class DefaultMessage extends AbstractMessage
{
	private static final long serialVersionUID = -2318543364969328542L;

	private byte[] data;

	public DefaultMessage(int messageId, byte[] data)
	{
		super(messageId);
		this.data = data;
	}

	@Override
	public void release()
	{
		data = null;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(byte[] data)
	{
		this.data = data;
	}

	@Override
	public void decode(byte[] bufdata)
	{
	}

	@Override
	public byte[] encode()
	{
		return data;
	}
}