package lol.aabss.skhttp.elements.websocket.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import lol.aabss.skhttp.objects.websocket.events.*;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.http.WebSocket;

public class EvtWebsocketEvents extends SkriptEvent {

    static {
        Skript.registerEvent("Websocket Binary Event", EvtWebsocketEvents.class, WebsocketBinaryEvent.class,
                "websocket binary [receive[d]]"
        )
                .description("Called whenever a binary data was received.")
                .examples("on websocket binary:")
                .since("1.3");
        EventValues.registerEventValue(WebsocketBinaryEvent.class, WebSocket.class, new Getter<>() {
            @Override
            public @Nullable WebSocket get(WebsocketBinaryEvent arg) {
                return arg.getWebSocket();
            }
        }, EventValues.TIME_NOW);
        EventValues.registerEventValue(WebsocketBinaryEvent.class, String.class, new Getter<>() {
            @Override
            public @Nullable String get(WebsocketBinaryEvent arg) {
                return arg.getData();
            }
        }, EventValues.TIME_NOW);
        EventValues.registerEventValue(WebsocketBinaryEvent.class, Boolean.class, new Getter<>() {
            @Override
            public @NotNull Boolean get(WebsocketBinaryEvent arg) {
                return arg.getLast();
            }
        }, EventValues.TIME_NOW);

        Skript.registerEvent("Websocket Close Event", EvtWebsocketEvents.class, WebsocketCloseEvent.class,
                "websocket close[d]"
        )
                .description("Called whenever a websocket was closed.")
                .examples("on websocket close:")
                .since("1.3");
        EventValues.registerEventValue(WebsocketCloseEvent.class, WebSocket.class, new Getter<>() {
            @Override
            public @Nullable WebSocket get(WebsocketCloseEvent arg) {
                return arg.getWebSocket();
            }
        }, EventValues.TIME_NOW);
        EventValues.registerEventValue(WebsocketCloseEvent.class, String.class, new Getter<>() {
            @Override
            public @Nullable String get(WebsocketCloseEvent arg) {
                return arg.getReason();
            }
        }, EventValues.TIME_NOW);
        EventValues.registerEventValue(WebsocketCloseEvent.class, Integer.class, new Getter<>() {
            @Override
            public @NotNull Integer get(WebsocketCloseEvent arg) {
                return arg.getStatusCode();
            }
        }, EventValues.TIME_NOW);

        Skript.registerEvent("Websocket Error Event", EvtWebsocketEvents.class, WebsocketErrorEvent.class,
                "websocket error [receive[d]]"
        )
                .description("Called whenever a error is received.")
                .examples("on websocket error:")
                .since("1.3");
        EventValues.registerEventValue(WebsocketErrorEvent.class, WebSocket.class, new Getter<>() {
            @Override
            public @Nullable WebSocket get(WebsocketErrorEvent arg) {
                return arg.getWebSocket();
            }
        }, EventValues.TIME_NOW);
        EventValues.registerEventValue(WebsocketErrorEvent.class, String.class, new Getter<>() {
            @Override
            public @Nullable String get(WebsocketErrorEvent arg) {
                return arg.getError();
            }
        }, EventValues.TIME_NOW);

        Skript.registerEvent("Websocket Open Event", EvtWebsocketEvents.class, WebsocketOpenEvent.class,
                "websocket open[ed|s]"
        )
                .description("Called whenever a websocket opens.")
                .examples("on websocket open:")
                .since("1.3");
        EventValues.registerEventValue(WebsocketOpenEvent.class, WebSocket.class, new Getter<>() {
            @Override
            public @Nullable WebSocket get(WebsocketOpenEvent arg) {
                return arg.getWebSocket();
            }
        }, EventValues.TIME_NOW);

        Skript.registerEvent("Websocket Ping Event", EvtWebsocketEvents.class, WebsocketPingEvent.class,
                "websocket ping[ed]"
        )
                .description("Called whenever a ping get received.")
                .examples("on websocket ping:")
                .since("1.3");
        EventValues.registerEventValue(WebsocketPingEvent.class, WebSocket.class, new Getter<>() {
            @Override
            public @Nullable WebSocket get(WebsocketPingEvent arg) {
                return arg.getWebSocket();
            }
        }, EventValues.TIME_NOW);
        EventValues.registerEventValue(WebsocketPingEvent.class, String.class, new Getter<>() {
            @Override
            public @Nullable String get(WebsocketPingEvent arg) {
                return arg.getData();
            }
        }, EventValues.TIME_NOW);

        Skript.registerEvent("Websocket Pong Event", EvtWebsocketEvents.class, WebsocketPongEvent.class,
                "websocket pong[ed]"
        )
                .description("Called whenever a pong gets sent out.")
                .examples("on websocket pong:")
                .since("1.3");
        EventValues.registerEventValue(WebsocketPongEvent.class, WebSocket.class, new Getter<>() {
            @Override
            public @Nullable WebSocket get(WebsocketPongEvent arg) {
                return arg.getWebSocket();
            }
        }, EventValues.TIME_NOW);
        EventValues.registerEventValue(WebsocketPongEvent.class, String.class, new Getter<>() {
            @Override
            public @Nullable String get(WebsocketPongEvent arg) {
                return arg.getData();
            }
        }, EventValues.TIME_NOW);

        Skript.registerEvent("Websocket Text Event", EvtWebsocketEvents.class, WebsocketTextEvent.class,
                "websocket text [receive[ed]]"
        )
                .description("Called whenever a websocket sends out a text to all listeners.")
                .examples("on websocket text:")
                .since("1.3");
        EventValues.registerEventValue(WebsocketTextEvent.class, WebSocket.class, new Getter<>() {
            @Override
            public @Nullable WebSocket get(WebsocketTextEvent arg) {
                return arg.getWebSocket();
            }
        }, EventValues.TIME_NOW);
        EventValues.registerEventValue(WebsocketTextEvent.class, String.class, new Getter<>() {
            @Override
            public @Nullable String get(WebsocketTextEvent arg) {
                return arg.getData();
            }
        }, EventValues.TIME_NOW);
        EventValues.registerEventValue(WebsocketTextEvent.class, Boolean.class, new Getter<>() {
            @Override
            public @NotNull Boolean get(WebsocketTextEvent arg) {
                return arg.getLast();
            }
        }, EventValues.TIME_NOW);
    }


    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern, SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(@NotNull Event event) {
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return e != null ? e.getEventName() : "websocket event";
    }
}
