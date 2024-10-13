package lol.aabss.skhttp.elements.http.effects;

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

import java.net.http.HttpClient;
import java.util.List;

@Name("Close Http Client")
@Description({
        "Closes/shutdowns an http client",
        "Required Java 21+"
})
@Examples({
        "close http client {_client}"
})
@Since("1.5")
public class EffCloseHttpClient extends Effect {

    static {
        if (Skript.methodExists(HttpClient.class, "shutdown")) {
            Skript.registerEffect(EffCloseHttpClient.class,
                    "(close|shutdown:shutdown [:now]) http[ ]client %httpclients%"
            );
        }
    }

    private Expression<HttpClient> client;
    private List<String> tags;

    @Override
    protected void execute(@NotNull Event event) {
        for (HttpClient client : client.getArray(event)) {
            if (tags.contains("now")) {
                client.shutdownNow();
            } else if (tags.contains("shutdown")) {
                client.shutdown();
            } else {
                client.close();
            }
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean b) {
        return "close http client";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int i, @NotNull Kleenean kleenean, SkriptParser.@NotNull ParseResult parseResult) {
        client = (Expression<HttpClient>) expressions[0];
        tags = parseResult.tags;
        return true;
    }
}
