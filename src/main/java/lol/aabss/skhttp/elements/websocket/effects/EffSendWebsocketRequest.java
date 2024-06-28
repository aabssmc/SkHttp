package lol.aabss.skhttp.elements.websocket.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.http.WebSocket;

@Name("Websocket Request")
@Description("Requests a websocket.")
@Examples({
        "request last websocket"
})
@Since("1.3")
public class EffSendWebsocketRequest extends Effect {

    static {
        Skript.registerEffect(EffSendWebsocketRequest.class,
                "request %websockets%"
        );
    }

    private Expression<WebSocket> webSocket;

    @Override
    protected void execute(@NotNull Event e) {
        for (WebSocket webSocket : webSocket.getArray(e)){
            webSocket.request(1);
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "send websocket request";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        this.webSocket = (Expression<WebSocket>) exprs[0];
        return true;
    }
}
