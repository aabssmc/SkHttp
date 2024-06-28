package lol.aabss.skhttp.objects.websocket.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.net.http.WebSocket;

public class WebsocketBinaryEvent extends Event {

    private final static HandlerList handlers = new HandlerList();
    private final WebSocket webSocket;
    private final String data;
    private final boolean last;


    public WebsocketBinaryEvent(WebSocket webSocket, String data, boolean last) {
        this.webSocket = webSocket;
        this.data = data;
        this.last = last;
    }

    public WebSocket getWebSocket(){
        return webSocket;
    }

    public String getData(){
        return data;
    }

    public boolean getLast(){
        return last;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
