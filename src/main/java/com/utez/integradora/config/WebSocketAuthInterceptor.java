package com.utez.integradora.config;

import com.utez.integradora.service.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor extends HttpSessionHandshakeInterceptor {

    @Autowired
    private JwtUtils jwtUtils;
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String uri = request.getURI().toString();
        String token = uri.contains("token=") ? uri.split("token=")[1] : null;

        if (token == null || jwtUtils.validateJwt(token) == null) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return false; // Bloquear la conexión
        }

        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
}
