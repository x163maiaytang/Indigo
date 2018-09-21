package com.fire.core.bhns.net;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

import com.fire.core.bhns.net.initializer.ServiceClientMessageInitalizer;
import com.fire.task.Task;
import com.fire.task.taskpool.CommonTask;

import game.net.server.SimpleClient;
import game.net.server.SimpleSSNettyInitializer;

public class ServiceClientFactory
{
	
	private static final Logger logger = Logger.getLogger(ServiceClientFactory.class);
	
//	private static final long MAX_TIME_OUT = 5 * 1000;

	private static CopyOnWriteArrayList<Client> clientList = new CopyOnWriteArrayList<>();
	
	static class Client{
		
//		private String key;
		
		private String ip;
		private int port;
		
		private SimpleClient client;

		public Client(String ip, int port, SimpleClient client) {
			super();
			this.ip = ip;
			this.port = port;
			this.client = client;
		}

		public String getKey() {
			return ip + ":" + port;
		}

		public SimpleClient getClient() {
			return client;
		}

		public String getIp() {
			return ip;
		}

		public int getPort() {
			return port;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((ip == null) ? 0 : ip.hashCode());
			result = prime * result + port;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Client other = (Client) obj;
			if (ip == null) {
				if (other.ip != null)
					return false;
			} else if (!ip.equals(other.ip))
				return false;
			if (port != other.port)
				return false;
			return true;
		}
		
		
	}
	
	static{
		CommonTask.getInstance().scheduleAtFixedRate(new Task() {
			@Override
			public void run() {
				for(Client c : clientList){
					
					if(!c.getClient().isConnected()){
						
						String ip = c.getIp();
						int port = c.getPort();
						SimpleClient value = createClientManager(ip, port);
						
						
						if(value != null){
							if(value.isConnected()){
								clientList.remove(c);
//								c = new Client(ip, port, value);
								
								clientList.add(new Client(ip, port, value));
							}
						}
					}
				}
			}
		}, 1000, 1000);
	}
	
	
	public static SimpleClient getClient(String ip, int port){
		
		for(Client c : clientList){
			if(c.getIp().equals(ip) && c.getPort() == port){
				return c.getClient();
			}
		}
		return null;
	}

	private static SimpleClient createClientManager(String ip,
			int port){
		ServiceClientMessageInitalizer messageInitializer = new ServiceClientMessageInitalizer("ServiceClient");
		SimpleSSNettyInitializer ssInitializer = new SimpleSSNettyInitializer(messageInitializer, null);
		SimpleClient client = new SimpleClient();
		try
		{
			client.initialize(ssInitializer, messageInitializer);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		SocketAddress address = new InetSocketAddress(ip, port);
		
		boolean connectTo = client.connectTo(address, 10);
		
		if(connectTo){
			return client;
		}
		logger.error("error remote addr = " + ip + ":" + port);
		
		return null;
		
	}
	
	
	public synchronized static void registe(String ip, int port, int createNum){
//		String key = ip + ":" + port;
		
		SimpleClient client = createClientManager(ip, port);
		
		if(client != null){
			clientList.add(new Client(ip, port, client));
		}
//		connectPool.setInitNum(createNum);
	}
	public static void unRegiste(String ip, int port){
		for(Client c : clientList){
			if(c.getIp().equals(ip) && c.getPort() == port){
				clientList.remove(c);
				
				return;
			}
		}
	}
	
	
}
