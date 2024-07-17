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
import lol.aabss.skhttp.elements.server.sections.SecCreateEndpoint;
import lol.aabss.skhttp.objects.server.HttpExchange;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Exchange Respond")
@Description("Sends a response to the endpoint/context.")
@Examples({
        "set {server} to new http server",
        "make endpoint using {server}:",
        "\tmethod: \"GET\"",
        "\tpath: \"getTest\"",
        "\ttrigger:",
        "\t\tadd 1 to {amount}",
        "\t\trespond with code 200 and message \"{\"\"amount\"\": %{amount}%}\""
})
@Since("1.3")
public class EffExchangeRespond extends Effect {

    static {
        Skript.registerEffect(EffExchangeRespond.class,
                "respond with [response] code %integer% [and message %-object%] [using %-httpexchange%]",
                "(send|post) [response] code %integer% [and message %-object%] [using %-httpexchange%]"
        );
    }

    private Expression<Integer> code;
    private Expression<Object> message;
    private Expression<HttpExchange> exchange;

    @Override
    protected void execute(@NotNull Event e) {
        HttpExchange exchange = ((SecCreateEndpoint.CreateEndpointEvent) e).getExchange();
        if (this.exchange != null){
            HttpExchange exchangeExpr = this.exchange.getSingle(e);
            if (exchangeExpr != null){
                exchange = exchangeExpr;
            }
        }
        Integer code = this.code.getSingle(e);
        if (code == null) {
            return;
        }
        if (message == null){
            exchange.respond(code);
            return;
        }
        Object message = this.message.getSingle(e);
        if (message == null){
            exchange.respond(code);
        } else {
            exchange.respond(message, code);
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "respond exchange";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(SecCreateEndpoint.CreateEndpointEvent.class)){
            Skript.error("'exchange respond' can not be used outside of Create Endpoint Event");
            return false;
        }
        code = (Expression<Integer>) exprs[0];
        message = (Expression<Object>) exprs[1];
        exchange = (Expression<HttpExchange>) exprs[2];
        return true;
    }
}
