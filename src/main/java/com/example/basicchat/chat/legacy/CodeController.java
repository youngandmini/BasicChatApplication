package com.example.basicchat.chat.legacy;


import com.example.basicchat.chat.CodeMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

//@Controller
public class CodeController {

    /**
     * /hello 경로로 메시지가 오면
     * greeting()메서드가 호출되고
     * 메시지의 페이로드가 HelloMessage객체로 바인딩된다.
     * <p>
     * 메서드가 return 될때,
     * /topic/greetings 경로의 구독자들에게 브로드캐스트된다.
     */
    @MessageMapping("/update-code")
    @SendTo("/topic/codes")
    public CodeMessage updateCode(CodeMessage message) throws Exception {

        return new CodeMessage(message.getCode());
    }
}
