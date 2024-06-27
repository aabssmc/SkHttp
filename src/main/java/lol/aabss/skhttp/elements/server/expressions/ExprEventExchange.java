package lol.aabss.skhttp.elements.server.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import lol.aabss.skhttp.elements.server.sections.SecCreateEndpoint;
import lol.aabss.skhttp.objects.server.HttpExchange;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

@Name("HTTP Exchange")
@Description("Returns the http exchange in a create endpoint event.")
@Examples({
        "send event-httpexchange"
})
@Since("1.2")
public class ExprEventExchange extends EventValueExpression<HttpExchange> {

    static {
        Skript.registerExpression(ExprEventExchange.class, HttpExchange.class, ExpressionType.SIMPLE,
                "[the] [event-][http[ |-]]exchange"
        );
    }

    public ExprEventExchange() {
        super(HttpExchange.class);
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
        return new HttpExchange[]{((SecCreateEndpoint.CreateEndpointEvent) event).getExchange()};
    }
}
