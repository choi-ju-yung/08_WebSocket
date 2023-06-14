package com.websocket.controller;

import javax.websocket.DecodeException;
import javax.websocket.Decoder.Text;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;
import com.websocket.vo.Message;

public class JsonDecoder implements Text<Message> { // atl + shift + s + v

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(EndpointConfig config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Message decode(String s) throws DecodeException {
		// TODO Auto-generated method stub
		return new Gson().fromJson(s,Message.class);
		
	}

	@Override
	public boolean willDecode(String s) {
		// TODO Auto-generated method stub
		return true; // false면 위 decode 작업안함
	}

}
