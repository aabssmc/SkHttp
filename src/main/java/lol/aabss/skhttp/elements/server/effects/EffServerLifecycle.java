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
import lol.aabss.skhttp.objects.server.HttpExchange;
import lol.aabss.skhttp.objects.server.HttpServer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Http Server Lifecycle")
@Description("Stops/starts a http server.")
@Examples({
        "on load:",
        "\tstart {httpserver}",
        "",
        "on unload:",
        "\tstop {httpserver}"
})
@Since("1.3")
public class EffServerLifecycle extends Effect {

    static {
        Skript.registerEffect(EffServerLifecycle.class,
                "start [http[ server]] %httpservers%",
                "(stop|shut[ ]down) [http[ server]] %httpservers% [with delay %integer%]",
                "close [http[ exchange]] %httpexchanges%"
        );
    }

    private boolean shutdown;
    private Expression<HttpServer> server;
    private Expression<Integer> delay;
    private Expression<HttpExchange> exchange;

    @Override
    protected void execute(@NotNull Event e) {
        if (exchange != null){
            for (HttpExchange exchange : this.exchange.getArray(e)){
                exchange.close();
            }
            return;
        }
        int delay = 0;
        if (shutdown && this.delay != null){
            Integer expr = this.delay.getSingle(e);
            if (expr != null){
                delay = expr;
            }
        }
        for (HttpServer server : this.server.getArray(e)){
            if (shutdown){
                server.stop(delay);
            } else {
                server.start();
            }
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return (shutdown ? "stop" : "start") + " http server";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (matchedPattern == 0) {
            server = (Expression<HttpServer>) exprs[0];
        } else if (matchedPattern == 1){
            shutdown = true;
            server = (Expression<HttpServer>) exprs[0];
            delay = (Expression<Integer>) exprs[1];
        } else {
            exchange = (Expression<HttpExchange>) exprs[0];
        }
        return true;
    }
}
