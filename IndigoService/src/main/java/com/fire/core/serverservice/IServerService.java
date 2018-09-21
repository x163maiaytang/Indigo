package com.fire.core.serverservice;



/**
 * GameServer内包含的常用的服务对象
 *
 */
public interface IServerService {
	/**
	 * 准备数据
	 * @return
	 */
	public boolean ready() throws IServerServiceException;
	/**
	 * 执行启动
	 * @return
	 */
	public boolean start()  throws IServerServiceException;
	/**
	 * 执行停止操作
	 * @return
	 */
	public boolean stop()  throws IServerServiceException;
}
