package com.example.hyuntrace.security.handler;

import com.example.hyuntrace.dto.RecommendResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Log4j2
public class RecommendSocketHandler extends TextWebSocketHandler {

    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        sessions.put(session.getId(), session);
        log.info("WebSocket 연결됨 {}",session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
        sessions.remove(session.getId());
        log.info("WebSocket 종료됨 {}",session.getId());
    }

    public void sendUpdate(RecommendResponseDTO dto){
       try{
          String message = objectMapper.writeValueAsString(dto);
          for(WebSocketSession session : sessions.values()){
              session.sendMessage(new TextMessage(message));
          }
       }catch (Exception e){
           log.error("WebSocket 메세지 전송 오류",e);
       }
    }
}
