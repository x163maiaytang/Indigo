package game.net.message;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import game.net.handler.IAction;

public class MessageFactory
{
	private static final Logger logger = Logger.getLogger(MessageFactory.class);

	private static final Class<?>[] EMPTY_PARAMS = new Class[0];

	private final Map<Integer, Class<? extends AbstractMessage>> messageClasses = new HashMap<Integer, Class<? extends AbstractMessage>>();

	private Map<Integer, ISubTypeAction<? extends AbstractMessage, ?>> protocolHandlers = new ConcurrentHashMap<Integer, ISubTypeAction<? extends AbstractMessage, ?>>();

	private IAction<?> handlerForAll;

	private MessageFactory()
	{
	}

	public final boolean isHaveMessage(int commandId)
	{
		Class<? extends AbstractMessage> cls = messageClasses.get(commandId);
//		if (cls == null)
//			throw new IllegalArgumentException("commandId:" + commandId
//					+ " donnt have message class");

		return cls != null;
	}
	/**
	 * 获取消息对象
	 * 
	 * @param commandId
	 * @return
	 * @throws Exception
	 */
	public final AbstractMessage getMessage(int commandId)
	{
		Class<? extends AbstractMessage> cls = messageClasses.get(commandId);
		if (cls == null)
			throw new IllegalArgumentException("commandId:" + commandId
					+ " donnt have message class");

		return get_message(cls);
	}

	public final <T extends AbstractMessage> T get_message(Class<T> clz)
	{
		try
		{
			if (clz == null)
			{
				return null;
			}
			T message = clz.newInstance();
			return message;
		} catch (Exception e)
		{
			logger.error("getMessage(int) - messageClz=" + clz + ". ", e);
		}
		return null;
	}

	/**
	 * 释放消息对象
	 * 
	 * @param message
	 */
	public final void free_message(AbstractMessage message)
	{
		try
		{
			message.releaseMessage();
		} catch (Exception e)
		{
			logger.error(
					"freeMessage(AbstractMessage) - message=" + message + ". ", e); //$NON-NLS-1$
		}

	}

	public void addMessage(int commandId,
			Class<? extends AbstractMessage> messageClass)
	{
		if (messageClass == null)
		{
			throw new NullPointerException("messageClass");
		}

		try
		{
			messageClass.getConstructor(EMPTY_PARAMS);
		} catch (NoSuchMethodException e)
		{
			throw new IllegalArgumentException(
					"The specified class doesn't have a public default constructor: commandId="
							+ commandId);
		}

		if (messageClasses.get(commandId) == messageClass)
		{
			return ;
//			throw new IllegalArgumentException(
//					"The messageClasses has already the commandId mapping class: commandId="
//							+ commandId);
		}

		boolean registered = false;
		if (AbstractMessage.class.isAssignableFrom(messageClass))
		{
//			logger.error("commnadId = " + commandId + "messageClass = " + messageClass.toString() + "+++++++++++++++++++++++++++");
			messageClasses.put(commandId, messageClass);
			registered = true;
		}

		if (!registered)
		{
			throw new IllegalArgumentException("Unregisterable type: "
					+ messageClass);
		}

	}

	public final IAction<? extends AbstractMessage> getMessageHandler(
			int commandId)
	{
		if (handlerForAll != null)
		{
			return handlerForAll;
		}
		return protocolHandlers.get(commandId);
	}

	public void addMessageAndAction(int commandId,
			Class<? extends AbstractMessage> messageClass,
			IAction<?> messageHandler)
	{
		if (messageClass != null)
 		{
			this.addMessage(commandId, messageClass);
		}

		if (messageHandler == null)
			return;

		if (commandId != -1)
			protocolHandlers
					.put(commandId,
							(ISubTypeAction<? extends AbstractMessage, ?>) messageHandler);
		else
			handlerForAll = messageHandler;
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (Entry<Integer, Class<? extends AbstractMessage>> entry : messageClasses
				.entrySet())
		{
			Class<? extends AbstractMessage> messageClass = entry.getValue();
			IAction handler = protocolHandlers.get(entry.getKey());

			sb.append("\n[").append(entry.getKey());
			sb.append("]  MessageClass=");
			sb.append(messageClass == null ? "null" : messageClass
					.getSimpleName());
			if (handler != null)
				sb.append("  ActionClass=").append(
						handler.getClass().getSimpleName());
		}
		return sb.toString();
	}

	// **********************************************************//
	private static final MessageFactory instance = new MessageFactory();

	public static final MessageFactory getInstance()
	{
		return instance;
	}

	public static final <T extends AbstractMessage> T getMessage(Class<T> clz)
	{
		return instance.get_message(clz);
	}

	public static final void freeMessage(AbstractMessage message)
	{
		instance.free_message(message);
	}

	// class _PoolableMessageFactory extends BasePoolableObjectFactory
	// {
	// int commandId;
	//
	// _PoolableMessageFactory(int commandId)
	// {
	// this.commandId = commandId;
	// }
	//
	// @Override
	// public Object makeObject() throws Exception
	// {
	// Class<? extends AbstractMessage> cls = messageClasses
	// .get(commandId);
	// if (cls == null)
	// {
	// return null;
	// }
	// AbstractMessage message = messageClasses.get(commandId)
	// .newInstance();
	// return message;
	// }
	//
	// @Override
	// public void passivateObject(Object arg0) throws Exception
	// {
	// AbstractMessage message = (AbstractMessage) arg0;
	// message.release();
	// }
	// }

}
