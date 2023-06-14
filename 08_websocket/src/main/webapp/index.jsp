<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>웹소켓테스트</title>
<script src="http://code.jquery.com/jquery-3.7.0.min.js"></script>
</head>
<body>
	<div id="container">
		<input type="text" id="sender" placeholder="보내는사람">
		<button id="chattingBtn">채팅접속</button>
		<input type="text" id="receiver" placeholder="받는사람">
		<Br> 
		<input type="text" id="sendMsg">&nbsp; <button id="sendBtn">전송</button>
	</div>
	<div id="msgcontainer"></div>
	<script>
		// 웹소켓서버에 접속하기
		// ws://  -> http프로콜을 이용할 때 사용 
		// wss:// -> https프로토콜을 이용할 때 사용
		// 웹소켓 서버에 접속하는 곳은 클래스로 만들어야함
		let socket;
		$("#chattingBtn").click(e=>{
			socket = new WebSocket("ws://localhost:9090/<%=request.getContextPath()%>/chatting");
			
								
			// 접속이 완료되면 실행되는 함수를 등록
			socket.onopen=e=>{
				// websocket 서버와 접속이 완료되면 실행되는 함수
				/* console.log(e); */
				/* socket.send("안녕"); */
				
				//session에 내 정보를 저장
				sendMsg(new Message("접속",$("#sender").val(),"","",""));
			}
		
			// 서버에서 전송된 메세지를 처리하는 함수
			socket.onmessage=e=>{
				// 접속된 websocket 서버에서 sendText() ||  sendObject() 메소드를 실행했을 때, 실행되는 함수
				// 서버에서 보낸 데이터를 처리하는 함수
				//console.log(e);
				const sendData=JSON.parse(e.data);   
				console.log(sendData);
				
				switch(sendData.type){
					case "채팅" : printData(sendData); break;
					case "알람" : alamPrint(sendData); break;
				}
				
				//서버에서 보낸 데이터는 매개변수객체의 data속성에 저장되어있음  -> f12번 확인
	/* 			const msg=e.data.split(",");
				const sub=$("<sup>").text(msg[0]);
				const span=$("<span>").text(msg[1]).css("fontSize",20);
				const textContainer=$("<div>").append(sub).append(span);	
				$("#msgcontainer").append(textContainer); */
			}	
		});
		
		
		const alamPrint=(sendData)=>{
			const h3=$("<h3>").text(sendData.msg).css("color","green");
			const container=$("<div>").css({"display":"flex","justifyContent":"center"});
			container.append(h3);
			$("#msgcontainer").append(container);
		}
		
		
		const printData=(sendData)=>{
			const sup=$("<sup>").text(sendData.sender).css("marginRight","2%");
			const text=$("<span>").text(sendData.msg).css("fontSize","20px");
			const container=$("<div>").css("width","100%").append(sup).append(text);
			
			if(sendData.sender == $("#sender").val()){
					container.css({
						"display":"flex",
						"justifyContent":"end"
					});
			}else{
					container.css({
						"display":"flex",
						"justifyContent":"start"
					});	
			}	
			$("#msgcontainer").append(container);
		}
		
	
		
		$("#sendBtn").click(e=>{
			const msg = $("#sendMsg").val();
			const sender=$("#sender").val(); // 보낸 사람 값을 가져옴
			const receiver=$("#receiver").val(); 
			const sendData = new Message("채팅",sender,receiver,msg,"");
			sendMsg(sendData);
		});
		
		
		function sendMsg(msg){
	/* 		if(msg.length>0){ */
				socket.send(JSON.stringify(msg)); // js객체를 파싱해줌
			/* }else{
				throw new Error("메세지가 비어있습니다");
			} */
		}
			
			class Message{
				constructor(type,sender,receiver,msg,room){
					this.type=type;
					this.sender=sender;
					this.receiver=receiver;
					this.msg=msg;
					this.room=room;
				}
		}
		
			
			// 
			//type : 채팅, 사용현황, 알림
			//sender : 보낸사람
			//receiver : 받는사람
			//msg : 
			//rom : 	
			
			
	</script>



</body>
</html>