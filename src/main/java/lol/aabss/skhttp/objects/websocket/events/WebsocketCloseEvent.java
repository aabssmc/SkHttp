package lol.aabss.skhttp.objects.websocket.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.net.http.WebSocket;

public class WebsocketCloseEvent extends Event {

    private final static HandlerList handlers = new HandlerList();
    private final WebSocket webSocket;
    private final int statusCode;
    private final String reason;

    public WebsocketCloseEvent(WebSocket webSocket, int statusCode, String reason) {
        this.webSocket = webSocket;
        this.statusCode = statusCode;
        this.reason = reason;
    }

    public WebSocket getWebSocket(){
        return webSocket;
    }

    public int getStatusCode(){
        return statusCode;
    }

    public String getReason(){
        return reason;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}