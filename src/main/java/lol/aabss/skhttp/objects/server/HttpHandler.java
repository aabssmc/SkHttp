package lol.aabss.skhttp.objects.server;

import java.io.IOException;

public class HttpHandler {
    public HttpHandler(com.sun.net.httpserver.HttpHandler handler) {
        this.handler = handler;
    }

    public com.sun.net.httpserver.HttpHandler handler;

    public void handle(HttpExchange exchange){
        try {
            handler.handle(exchange.exchange);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
