package com.fire.core.bhns.net;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import game.net.server.SimpleClient;

public class ConnectPool {
	
	private static final Logger logger = Logger.getLogger(ConnectPool.class);
	
	private CopyOnWriteArrayList<SimpleClient> pool = new CopyOnWriteArrayList<SimpleClient>();
	
	private AtomicInteger index = new AtomicInteger();

	private String ip;
	private int port;

	private int initNum;
	
	private static int max = Integer.MAX_VALUE - 1000;

	public ConnectPool(String ip, int port, int initNum) {
		this.ip = ip;
		this.port = port;
		this.initNum = initNum;
	}

	public void add(SimpleClient client) {
		pool.add(client);
	}

	public void clear() {
		pool.clear();
		pool = null;
		index = null;
	}

	public SimpleClient chooseClient() {
		
		int size = pool.size();
		
		if(size > 0){
			if(index.get() > max){
				index.set(0);
			}
			
			SimpleClient simpleClient = pool.get(index.addAndGet(1) % size);
			if(simpleClient != null && simpleClient.isConnected()){
				return simpleClient;
			}else{
				
				logger.error("=============== bad connect ip = " + ip + " port = " + port + "================");
				pool.remove(simpleClient);
				
				return chooseClient();
			}
		}
		return null;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public int getInitNum() {
		return initNum;
	}

	public int needConnectNum() {
		if(pool == null){
			return 0;
		}
		return initNum - pool.size();
	}

	public void setInitNum(int initNum) {
		this.initNum = initNum;
	}
	
}
