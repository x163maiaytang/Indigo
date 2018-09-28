package test;

import game.net.websocket.WebSocketClientHandler;
import game.net.websocket.WebSocketSimpleClient;

public class ClientMain {
	
    public static void main(String[] args) throws Exception{
    
    	
    	WebSocketSimpleClient client = new WebSocketSimpleClient();
    	
    	client.initialize(new WebSocketClientHandler());
    	
    	
    	boolean connectTo = client.connectTo("ws://47.105.48.56:8090/ermj", 10);
    	
    	System.out.println("okkkkkkkkk");
    }
}

