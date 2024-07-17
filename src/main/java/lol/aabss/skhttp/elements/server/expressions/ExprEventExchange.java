package lol.aabss.skhttp.elements.server.expressions;

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
import lol.aabss.skhttp.elements.server.sections.SecCreateEndpoint;
import lol.aabss.skhttp.objects.server.HttpExchange;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("HTTP Exchange")
@Description("Returns the http exchange in a create endpoint event.")
@Examples({
        "send event-httpexchange"
})
@Since("1.3")
public class ExprEventExchange extends SimpleExpression<HttpExchange> {

    static {
        Skript.registerExpression(ExprEventExchange.class, HttpExchange.class, ExpressionType.SIMPLE,
                "[the] [event-][http[s][ |-]]exchange"
        );
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (getParser().isCurrentEvent(SecCreateEndpoint.CreateEndpointEvent.class)){
            return true;
        }
        Skript.error("'httpexchange' can not be used outside of Create Endpoint Event");
        return false;
    }

    @Override
    protected HttpExchange @NotNull [] get(@NotNull Event event) {
        if (event instanceof SecCreateEndpoint.CreateEndpointEvent) {
            return new HttpExchange[]{((SecCreateEndpoint.CreateEndpointEvent) event).getExchange()};
        }
        return new HttpExchange[]{};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends HttpExchange> getReturnType() {
        return HttpExchange.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "event http exchange";
    }
}
