package lol.aabss.skhttp.objects.websocket.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.net.http.WebSocket;

public class WebsocketOpenEvent extends Event {

    private final static HandlerList handlers = new HandlerList();
    private final WebSocket webSocket;

    public WebsocketOpenEvent(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    public WebSocket getWebSocket(){
        return webSocket;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
