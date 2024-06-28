package lol.aabss.skhttp.objects.websocket;

import lol.aabss.skhttp.objects.websocket.events.*;
import org.bukkit.Bukkit;

import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletionStage;

public class WebsocketBukkitListener implements WebSocket.Listener {
    @Override
    public void onOpen(WebSocket webSocket) {
        Bukkit.getPluginManager().callEvent(new WebsocketOpenEvent(webSocket));
        WebSocket.Listener.super.onOpen(webSocket);
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        Bukkit.getPluginManager().callEvent(new WebsocketTextEvent(webSocket, data.toString(), last));
        return WebSocket.Listener.super.onText(webSocket, data, last);
    }

    @Override
    public CompletionStage<?> onBinary(WebSocket webSocket, ByteBuffer data, boolean last) {
        Bukkit.getPluginManager().callEvent(new WebsocketBinaryEvent(webSocket, new String(data.array()), last));
        return WebSocket.Listener.super.onBinary(webSocket, data, last);
    }

    @Override
    public CompletionStage<?> onPing(WebSocket webSocket, ByteBuffer message) {
        Bukkit.getPluginManager().callEvent(new WebsocketPingEvent(webSocket, new String(message.array())));
        return WebSocket.Listener.super.onPing(webSocket, message);
    }

    @Override
    public CompletionStage<?> onPong(WebSocket webSocket, ByteBuffer message) {
        Bukkit.getPluginManager().callEvent(new WebsocketPongEvent(webSocket, new String(message.array())));
        return WebSocket.Listener.super.onPong(webSocket, message);
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        Bukkit.getPluginManager().callEvent(new WebsocketCloseEvent(webSocket, statusCode, reason));
        return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        Bukkit.getPluginManager().callEvent(new WebsocketErrorEvent(webSocket, error.getMessage()));
        WebSocket.Listener.super.onError(webSocket, error);
    }
}
