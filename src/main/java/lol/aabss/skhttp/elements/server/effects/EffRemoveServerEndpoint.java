package lol.aabss.skhttp.elements.server.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import lol.aabss.skhttp.objects.server.HttpContext;
import lol.aabss.skhttp.objects.server.HttpServer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Remove Server Endpoint")
@Description("Removes a server http endpoint/context.")
@Examples({
        "remove endpoint \"info\" from last http server"
})
@Since("1.3")
public class EffRemoveServerEndpoint extends Effect {

    static {
        Skript.registerEffect(EffRemoveServerEndpoint.class,
                "(remove|delete) endpoint %httpcontexts/strings% from [http[server]] %httpserver%"
        );
    }

    private Expression<Object> endpoint;
    private Expression<HttpServer> server;

    @Override
    protected void execute(Event e) {
        HttpServer server = this.server.getSingle(e);
        if (server == null){
            return;
        }
        for (Object endpoint : this.endpoint.getArray(e)){
            if (endpoint instanceof String){
                server.deleteEndpoint((String) endpoint);
            } else if (endpoint instanceof HttpContext){
                server.deleteEndpoint((HttpContext) endpoint);
            }
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "remove server endpoint";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        endpoint = (Expression<Object>) exprs[0];
        server = (Expression<HttpServer>) exprs[1];
        return true;
    }
}
