package kopo.poly.config;


import kopo.poly.chat.ChatHandler;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.awt.*;
import java.util.Map;

@Slf4j
@EnableWebSocket
@RequiredArgsConstructor
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatHandler chatHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        log.info("WebSocket Excute!");

        registry.addHandler(chatHandler, "/ws/*/*")
                .setAllowedOrigins("*")
                .addInterceptors(
                        new HttpSessionHandshakeInterceptor() {

                            @Override
                            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                                           WebSocketHandler wsHandler, Map<String, Object> attributes)
                                    throws Exception {

                                String path = CmmUtil.nvl(request.getURI().getPath());
                                log.info("path : " + path);

                                String[] urlInfo = path.split("/");

                                String roomName = CmmUtil.nvl(urlInfo[2]);
                                String userName = CmmUtil.nvl(urlInfo[3]);

                                String roomNameHash = EncryptUtil.encHashSHA256(roomName);

                                log.info("roomName : " + roomName + " / userName : + " + userName);

                                attributes.put("roomName", roomName);
                                attributes.put("userName", userName);
                                attributes.put("roomNameHash", roomNameHash);
                                ; // 채팅방 언어(한국어, 영어)

                                return super.beforeHandshake(request, response, wsHandler, attributes);
                            }
                        }
                );
    }
}
