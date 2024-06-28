package lol.aabss.skhttp.objects.websocket.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.net.http.WebSocket;

public class WebsocketErrorEvent extends Event {

    private final static HandlerList handlers = new HandlerList();
    private final WebSocket webSocket;
    private final String error;

    public WebsocketErrorEvent(WebSocket webSocket, String error) {
        this.webSocket = webSocket;
        this.error = error;
    }

    public WebSocket getWebSocket(){
        return webSocket;
    }

    public String getError(){
        return error;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}