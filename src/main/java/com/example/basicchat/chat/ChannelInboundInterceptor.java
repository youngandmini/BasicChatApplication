package com.example.basicchat.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class ChannelInboundInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor header = StompHeaderAccessor.wrap(message);
        log.info("command: {}", header.getCommand());
        log.info("destination: {}", header.getDestination());
        if (StompCommand.CONNECT.equals(header.getCommand())) {
            log.info("name header: {}", header.getFirstNativeHeader("name"));
            Map<String, Object> attributes = header.getSessionAttributes();
            attributes.put("name", header.getFirstNativeHeader("name"));
            header.setSessionAttributes(attributes);
        }

        return message;
    }
}
