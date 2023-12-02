package com.example.basicchat.anotherversion;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


/**
 * @Configuration으로 스프링 빈으로 등록
 * @EnableWebSocketMessageBroker가 웹소켓 메시지 브로커를 활성화
 */
//@Configuration
//@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class ChatWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // /topic 하위 경로에 대해 메모리 기반 브로커를 사용하게 해줌
        registry.enableSimpleBroker("/topic");
        // @MessageMapping의 prefix를 붙여줌 관련
        // @MessageMapping("/hello")라면 /app/hello 와 같이 호출
        registry.setApplicationDestinationPrefixes("/api/chat");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //소켓 연결의 엔드포인트를 설정 -> 이 경로로 핸드셰이크 하게됨
        registry.addEndpoint("/ws/chat")
                .setAllowedOrigins("*")
                .withSockJS();
    }
}
