package com.example.basicchat.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ChatController + CodeController를 합치고 id에 따라 다른 채팅방에 연결되도록 설정
 */
@Controller
@RequiredArgsConstructor
public class ChatRoomController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/enter/{roomId}")
    public void enter(StompHeaderAccessor session, @DestinationVariable("roomId") String roomId) throws Exception {

        ChatResponse chatResponse = new ChatResponse(session.getSessionAttributes().get("name") + "님께서 입장하셨습니다!");
        simpMessagingTemplate.convertAndSend("/topic/chat-messages/" + roomId, chatResponse);
    }

    @MessageMapping("/exit/{roomId}")
    public void exit(StompHeaderAccessor session, @DestinationVariable("roomId") String roomId) throws Exception {

        ChatResponse chatResponse = new ChatResponse(session.getSessionAttributes().get("name") + "님께서 퇴장하셨습니다!");
        simpMessagingTemplate.convertAndSend("/topic/chat-messages/" + roomId, chatResponse);
    }

    @MessageMapping("/chat/{roomId}")
    public void chat(ChatRequest message, StompHeaderAccessor session, @DestinationVariable("roomId") String roomId) throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String currentTime = format.format(now);

        ChatResponse chatResponse = new ChatResponse(HtmlUtils.htmlEscape(session.getSessionAttributes().get("name") + " : "+message.getChat()+"  ["+currentTime+"]"));
        simpMessagingTemplate.convertAndSend("/topic/chat-messages/" + roomId, chatResponse);
    }

    @MessageMapping("/update-code/{roomId}")
    public void updateCode(CodeMessage message, @DestinationVariable("roomId") String roomId) throws Exception {

        CodeMessage codeMessage = new CodeMessage(message.getCode());
        simpMessagingTemplate.convertAndSend("/topic/codes/" + roomId, codeMessage);
    }

}
