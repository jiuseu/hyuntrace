package com.example.hyuntrace.config;

import com.example.hyuntrace.security.handler.RecommendSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final RecommendSocketHandler recommendSocketHandler;

    public WebSocketConfig(RecommendSocketHandler recommendSocketHandler){
        this.recommendSocketHandler = recommendSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry){
        registry.addHandler(recommendSocketHandler, "ws-recommend").setAllowedOrigins("*");
    }
}
