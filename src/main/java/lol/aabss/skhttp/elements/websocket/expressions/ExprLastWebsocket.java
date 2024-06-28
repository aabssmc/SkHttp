package lol.aabss.skhttp.elements.websocket.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.http.WebSocket;

import static lol.aabss.skhttp.SkHttp.LAST_WEBSOCKET;

@Name("Last Http Websocket")
@Description("Returns the last http websocket.")
@Examples({
        "set {_r} to last websocket"
})
@Since("1.3")
public class ExprLastWebsocket extends SimpleExpression<WebSocket> {

    static {
        Skript.registerExpression(ExprLastWebsocket.class, WebSocket.class, ExpressionType.SIMPLE,
                "[the] last [http] websocket"
        );
    }

    @Override
    protected WebSocket @NotNull [] get(@NotNull Event e) {
        return new WebSocket[]{LAST_WEBSOCKET};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends WebSocket> getReturnType() {
        return WebSocket.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "last websocket";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }
}
