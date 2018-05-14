package com.machinly.recorddemo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/socket")
@Component
@Scope("Prototype")
public class WebSocketServer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Session session;
    private static Set<WebSocketServer> chatEndpoints
            = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(
            Session session,
            @PathParam("username") String username) throws IOException {

        this.session = session;
        chatEndpoints.add(this);
        logger.info("open");
    }

    @OnMessage
    public void onMessage(Session session, byte[] message)
            throws IOException {
        logger.info(String.format("%s", message));
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        chatEndpoints.remove(this);
        logger.info("close");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.info("error");
        // Do error handling here
    }


}
