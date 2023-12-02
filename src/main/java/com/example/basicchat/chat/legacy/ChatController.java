package com.example.basicchat.chat.legacy;


import com.example.basicchat.chat.ChatResponse;
import com.example.basicchat.chat.ChatRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.util.HtmlUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

//@Controller
public class ChatController {

    /**
     * /hello 경로로 메시지가 오면
     * greeting()메서드가 호출되고
     * 메시지의 페이로드가 HelloMessage객체로 바인딩된다.
     * <p>
     * 메서드가 return 될때,
     * /topic/greetings 경로의 구독자들에게 브로드캐스트된다.
     */
    @MessageMapping("/hello")
    @SendTo("/topic/chat-messages")
    public ChatResponse greeting(ChatRequest message) throws Exception {

        return new ChatResponse("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    @MessageMapping("/enter")
    @SendTo("/topic/chat-messages")
    public ChatResponse enter(StompHeaderAccessor session) throws Exception {
        return new ChatResponse(HtmlUtils.htmlEscape(session.getSessionAttributes().get("name") + "님께서 입장하셨습니다!"));
    }

    @MessageMapping("/exit")
    @SendTo("/topic/chat-messages")
    public ChatResponse exit(StompHeaderAccessor session) throws Exception {
        return new ChatResponse(HtmlUtils.htmlEscape(session.getSessionAttributes().get("name") + "님께서 퇴장하셨습니다!"));
    }

    @MessageMapping("/chat")
    @SendTo("/topic/chat-messages")
    public ChatResponse chat(ChatRequest message, StompHeaderAccessor session) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date now = new Date();

        String currentTime = format.format(now);

        return new ChatResponse(HtmlUtils.htmlEscape(session.getSessionAttributes().get("name") + " : "+message.getChat()+"  ["+currentTime+"]"));
    }
}
