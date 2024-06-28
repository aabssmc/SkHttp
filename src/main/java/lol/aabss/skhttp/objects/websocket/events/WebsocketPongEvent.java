package lol.aabss.skhttp.objects.websocket.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.net.http.WebSocket;

public class WebsocketPongEvent extends Event {

    private final static HandlerList handlers = new HandlerList();
    private final WebSocket webSocket;
    private final String message;


    public WebsocketPongEvent(WebSocket webSocket, String message) {
        this.webSocket = webSocket;
        this.message = message;
    }

    public WebSocket getWebSocket(){
        return webSocket;
    }

    public String getData(){
        return message;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
