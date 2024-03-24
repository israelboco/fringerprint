package com.presence.testpresence.websoketsconnexion;

import com.presence.testpresence.websoketsconnexion.common.RequestMessage;
import com.presence.testpresence.websoketsconnexion.common.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

/**
 * @author Tayfun Wiechert <wiechert@campus.tu-berlin.de>
 */
@Component
public class SessionHandler extends StompSessionHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(SessionHandler.class);
    private static final int messageToSend = 12;

    @Autowired
    protected FactoryBean<RequestMessage> requestMessageFactoryBean;


    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        try {
            for(int i=0; i <messageToSend;i++) {
                RequestMessage requestMessage = requestMessageFactoryBean.getObject();
                LOG.info("Connection has been established, will send request message {}", requestMessage);
                this.subscribeAndSend(session, requestMessage);
            }
        } catch(Exception e) {
            LOG.error("Error while sending data");
        }

    }

    protected void subscribeAndSend(StompSession session, RequestMessage requestMessage) {
        session.subscribe("", this);
        session.send("", requestMessage);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return ResponseMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        LOG.info("Response has been received {}", payload);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        super.handleException(session, command, headers, payload, exception);
    }

    @Component
    public static class SessionHandlerThatExpectsNoResponse extends SessionHandler {

        protected void subscribeAndSend(StompSession session, RequestMessage requestMessage) {
            session.subscribe("", this);
            session.send("", requestMessage);
        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return Void.class;
        }
    }
}
