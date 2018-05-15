package com.machinly.recorddemo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    @OnOpen
    public void onOpen(
            Session session,
            @PathParam("username") String username) throws IOException {

        this.session = session;
        this.session.setMaxBinaryMessageBufferSize(64 * 1024);
        chatEndpoints.add(this);
        logger.info("open");
    }

    @OnMessage
    public void onMessage(Session session, byte[] message)
            throws IOException {
        logger.info("onmessage");
        logger.info("rec msg length:" + String.valueOf(message.length));
        logger.info(bytesToHex(message));
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        chatEndpoints.remove(this);
        logger.info("close");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.info("error");
        logger.error(throwable.getMessage());
        // Do error handling here
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
