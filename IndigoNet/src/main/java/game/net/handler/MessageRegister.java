package game.net.handler;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fire.core.util.PackageScaner;

import game.net.message.AbstractMessage;
import game.net.message.ActionAnnotation;
import game.net.message.ISubTypeAction;
import game.net.message.MessageFactory;

/**
 * MessageHandler的加载
 * 
 * @author
 * 
 * 
 */
public class MessageRegister
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MessageRegister.class);

	private String messagePackagePath;

	public MessageRegister()
	{
	}

	public void loadConfig(String messagePackagePath)
	{
		this.messagePackagePath = messagePackagePath;
		// 读取配置文件参数
		List<MessageHandlerObject> list = loadPackage();

		for (MessageHandlerObject obj : list)
			MessageFactory.getInstance().addMessageAndAction(
					obj.getCommandId(), obj.getClazz(), obj.getHandler());
	}

	private List<MessageHandlerObject> loadPackage()
	{
		String ext = ".class";

		List<MessageHandlerObject> list = new ArrayList<MessageHandlerObject>();

		String path = this.messagePackagePath;

		// 解析目录
		String[] include = PackageScaner.scanNamespaceFiles(path, ext, true);
		if (include != null && include.length > 0)
		{
			String key;
			for (String includeClass : include)
			{
				key = path
						+ "."
						+ includeClass.subSequence(0, includeClass.length()
								- (ext.length()));

				Class<?> actionClass = null;
				try
				{
					actionClass = Class.forName(key);
				} catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}
				ActionAnnotation annotation = (ActionAnnotation) actionClass
						.getAnnotation(ActionAnnotation.class);
				if (annotation != null && annotation.message() != null)
				{
					if (!checkAnnotation(actionClass, annotation.message()))
					{
						logger.error("Action注解和消息类不一致，不予添加:" + actionClass);
						continue;
					}
					processMessage(list, actionClass, annotation.message());
				}
			}
		}
		return list;
	}

	private boolean checkAnnotation(Class<?> actionClass, Class<?> messageClass)
	{
		Type t = actionClass.getGenericSuperclass();
		if (t instanceof ParameterizedType)
		{
			Type[] intypes = ((ParameterizedType) t).getActualTypeArguments();
			for (Type tt : intypes)
			{
				if (tt == messageClass)
				{
					return true;
				}
			}
		}
		return false;
	}

	private void processMessage(List<MessageHandlerObject> list,
			Class<?> actionClass, Class<? extends AbstractMessage> messageClass)
	{

		try
		{
			Constructor<?> c = messageClass.getDeclaredConstructor(null);
			c.setAccessible(true);
			int commandId = ((AbstractMessage) c.newInstance()).getCommandId(); // 获取message的commandId;
			MessageHandlerObject obj = new MessageHandlerObject();
			obj.setCommandId(commandId);
			obj.setClazz(messageClass);

			if (actionClass != null)
			{
				c = actionClass.getDeclaredConstructor(null);
				c.setAccessible(true);
				ISubTypeAction<?, ?> action = (ISubTypeAction<?, ?>) c
						.newInstance();
				obj.setHandler(action);
			}

			logger.debug("regist message: name = "
					+ obj.getClazz().getSimpleName() + "; commandId = "
					+ obj.commandId);
			list.add(obj);
		} catch (Exception e)
		{
			logger.error("解析错误actionClass=" + actionClass + ",messageClass="
					+ messageClass, e);
		}
	}

	private static class MessageHandlerObject
	{
		private int commandId;
		private Class<? extends AbstractMessage> clazz;
		private IAction handler;

		public int getCommandId()
		{
			return commandId;
		}

		public void setCommandId(int commandId)
		{
			this.commandId = commandId;
		}

		public Class<? extends AbstractMessage> getClazz()
		{
			return clazz;
		}

		public void setClazz(Class<? extends AbstractMessage> clazz)
		{
			this.clazz = clazz;
		}

		public IAction getHandler()
		{
			return handler;
		}

		public void setHandler(IAction handler)
		{
			this.handler = handler;
		}
	}

	// ***********************************************//
	private static MessageRegister _instance = new MessageRegister();

	public static MessageRegister getInstance()
	{
		return _instance;
	}
}
