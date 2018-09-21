package com.fire.core.bhns.net.action;

import org.apache.log4j.Logger;

import com.fire.core.bhns.ServiceInfo;
import com.fire.core.bhns.ServiceManager;
import com.fire.core.bhns.net.message.CS_ServiceDefaultMessage;
import com.fire.core.bhns.net.message.SC_ServiceDefaultMessage;
import com.fire.core.bhns.proxy.ProxyBean;
import com.fire.core.bhns.proxy.ProxyConst;

import game.net.channelobj.IRemoteNode;
import game.net.handler.AbstractSessionAction;

public class DefaultServerAction extends
		AbstractSessionAction<CS_ServiceDefaultMessage>
{
	private static final Logger logger = Logger.getLogger(DefaultServerAction.class.getName());

	private static DefaultServerAction instance = new DefaultServerAction();

	public static DefaultServerAction getInstance()
	{
		return instance;
	}

	@Override
	public void processMessage(CS_ServiceDefaultMessage message, IRemoteNode session)
			 {
		
		long tmpserviceId = message.getRoleId();
		long synKey = message.getSynKey();
		ProxyBean bean = message.getBean();
		int tmpPortalId = bean.getPortalId();
		byte tmpInternalFlag = bean.getInternalFlag();
		int tmpSrcEndpointId = bean.getEndpointId();
		String tmpMethodSignature = bean.getMethodSignature();

		// Monitor.mark(tmpMethodSignature, tmpPortalId);
		// Monitor.recordGetCallIoSize(arg1.getMessageLength());

//		MessageProcessorActor task = (MessageProcessorActor) Task.getCurrentTask();
//		if(bean.getWaitActorIdList() != null)
//			task.getWaitActorIdList().addAll(bean.getWaitActorIdList());
		Object resData = null;
		SC_ServiceDefaultMessage encoderMessage = new SC_ServiceDefaultMessage();
		encoderMessage.setSynKey(synKey);
		try
		{
			ServiceInfo tmpServiceInfo = ServiceManager.getServiceInfo(tmpPortalId);
			if (tmpServiceInfo == null)
				throw new RuntimeException("service info null");

			if (tmpInternalFlag == ProxyConst.FLAG_PORTAL_INVOKE){
				resData = tmpServiceInfo.source().invokePortalMethod(tmpSrcEndpointId, tmpMethodSignature, bean.getParameter());
			}else if (tmpInternalFlag == ProxyConst.FLAG_SERVICE_ACTIVE){
				tmpServiceInfo.source().getLocalEndPoint().activeService(tmpserviceId);
			}else if (tmpInternalFlag == ProxyConst.FLAG_SERVICE_DESTROY){
				tmpServiceInfo.source().getLocalEndPoint().destroyService(tmpserviceId);
			}else if (tmpInternalFlag == ProxyConst.FLAG_SERVICE_INVOKE){
				resData = tmpServiceInfo.source().invokeServiceMethod(
						tmpserviceId, tmpMethodSignature, bean.getParameter());
			}
		} catch (Exception e)
		{
			logger.error("tmpMethodSignature = " + tmpMethodSignature);
		}finally{
			
			if(synKey == 0){
				return;
			}
		}
		// Monitor.recordSendReturnIoSize(encoderMessage.getBuffRealLength());
		// Monitor.recordSimpleInfo();

		encoderMessage.setResult(resData);
		session.writeAndFlush(encoderMessage);
		
//		task.getWaitActorIdList().clear();
	}

}
