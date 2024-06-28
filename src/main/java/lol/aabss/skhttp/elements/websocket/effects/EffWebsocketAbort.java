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

@Name("Websocket Abort")
@Description("Aborts a websocket.")
@Examples({
        "abort last websocket"
})
@Since("1.3")
public class EffWebsocketAbort extends Effect {

    static {
        Skript.registerEffect(EffWebsocketAbort.class,
                "abort %websockets%"
        );
    }

    private Expression<WebSocket> websocket;

    @Override
    protected void execute(@NotNull Event e) {
        for (WebSocket webSocket : this.websocket.getArray(e)){
            webSocket.abort();
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "abort websocket";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        websocket = (Expression<WebSocket>) exprs[0];
        return true;
    }
}
