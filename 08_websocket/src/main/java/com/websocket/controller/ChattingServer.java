package com.websocket.controller;

import java.io.IOException;
import java.util.Set;

import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.websocket.vo.Message;

@ServerEndpoint(value="/chatting",
		decoders = {JsonDecoder.class}, // json형식의 데이터를 자바클래스로 변경
		encoders = {JsonEncoder.class} // 자바클래스를 json형식으로 변경
) // chatting으로 주소 매핑한곳으로

public class ChattingServer {
	
//	private Set<Session> clients=new ArrayList(); // 사용자 관리하기 위해서 Session 객체 만듬 
	
	@OnOpen
	public void open(Session session, EndpointConfig config) {
		// 클라이트가 접속요청을 하면 실행되는 함수
		System.out.println(session.getId());
		System.out.println("서버 접속!");
//		clients.add(session);  // 접속될때마다 세션 넣어줌 
	}
	
	
	@OnMessage
	public void message(Session session, Message m) {
		// js에서  socket.send("메세지")함수를 실행했을 때 실행되는 메소드
		// send()함수의 인자 값이 두번째 매개변수에 저장이 된다.
		// 클라이언트가 보낸 데이터가 두번째 매개변수에 저장된다.
		//System.out.println(msg);
		
		//Message m = new Gson().fromJson(msg,Message.class);
		
		System.out.println(m);
		
		switch(m.getType()) {
			case "접속" : addClient(session,m); break;
			case "채팅" : sendMessage(session,m); break;
		}
		
//		// 접속한 session을 가져올 수 있는 메소드 제공
//		Set<Session> clients = session.getOpenSessions(); // 접속한 모든 세션들 가져옴 -> getOpenSessions() 메소드
//		System.out.println(clients.size());
//		
//		//session에 대한 구분자 값을 저장하기
//		session.getUserProperties().put("msg", msg);
//		
//		// 접속한 사용자에게 받은 메세지를 전달
//		// 매개변수 session은 send한 사용자의 session
//		// -> 여러 브라우저가 접속할 수 있는데 각 브라우저의 session이 들어옴
//		try {
//			for(Session client : clients) {
//				
//				
//				client.getBasicRemote().sendText(msg);
//			}
////			session.getBasicRemote().sendText(msg); // stream이기때문에 예외처리해야함
//		}catch(IOException e) {
//			e.printStackTrace();
//		}
		
	}
	
	private void addClient(Session session, Message msg) {
		// session을 구분할 수 있는 데이터를 저장하기
		session.getUserProperties().put("msg", msg);
		sendMessage(session,Message.builder().type("알람").msg(msg.getSender()+"님이 접속하셨습니다.").build());
	}
	
	private void sendMessage(Session session, Message msg) {
		// 접속한 클라이언트에게 메세지를 전송해주는 기능
		Set <Session> clients=session.getOpenSessions();
		try {
		if(msg.getReceiver()==null|| msg.getReceiver().isBlank()) {
			// 전체접속자에게 전송
			for(Session client:clients) {
				client.getBasicRemote().sendObject(msg);//.sendText(new Gson().toJson(msg));
			}
			
		}else {
			//받는 사람한테만 전송
			for(Session client:clients) {
				Message c=(Message)client.getUserProperties().get("msg");
				if(c.getSender().equals(msg.getReceiver())) {
					client.getBasicRemote().sendObject(msg);//.sendText(new Gson().toJson(msg));
				}
			}	
			
		}
	}catch(IOException | EncodeException e) {
			e.printStackTrace();
		}
	}
	
}
