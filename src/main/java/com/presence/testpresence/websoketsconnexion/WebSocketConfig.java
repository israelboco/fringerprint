package com.presence.testpresence.websoketsconnexion;
// This is the configuration class for WebSocket
// connections. It enables WebSocket and registers the
// SocketConnectionHandler class as the handler for the
// "/hello" endpoint. It also sets the allowed origins to
// "*" so that other domains can also access the socket.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.handler.ExceptionWebSocketHandlerDecorator;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

// web socket connections is handled
// by this class
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    // Overriding a method which register the socket
    // handlers into a Registry

//    @Override
//    public void registerWebSocketHandlers(
//            WebSocketHandlerRegistry webSocketHandlerRegistry)
//    {
//        // For adding a Handler we give the Handler class we
//        // created before with End point Also we are managing
//        // the CORS policy for the handlers so that other
//        // domains can also access the socket
//        webSocketHandlerRegistry
//                .addHandler(myHandler(),"")
//                //.setAllowedOrigins("*");
//                .addInterceptors(new HttpSessionHandshakeInterceptor());
//    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        /*
        Here we register the single endpoints
         */
        registry.addEndpoint("")
                .setAllowedOrigins("*")
                .withSockJS();

        registry.addEndpoint("")
                .setAllowedOrigins("*")
                .withSockJS();
    }
    @Bean
    public SocketConnectionHandler myHandler() {
        return new SocketConnectionHandler();
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }
}
