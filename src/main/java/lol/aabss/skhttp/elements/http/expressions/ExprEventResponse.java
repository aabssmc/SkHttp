package lol.aabss.skhttp.elements.http.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import lol.aabss.skhttp.elements.http.sections.EffSecSendHttpRequest;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.http.HttpResponse;

@Name("Event Response")
@Description("Gets a event-response.")
@Examples({
        "send the response"
})
@Since("1.6")
public class ExprEventResponse extends SimpleExpression<HttpResponse> {

    static {
        Skript.registerExpression(ExprEventResponse.class, HttpResponse.class, ExpressionType.SIMPLE,
                "(the |event-)[http[ |-]]response"
        );
    }

    @Override
    protected HttpResponse @NotNull [] get(@NotNull Event e) {
        if (e instanceof EffSecSendHttpRequest.SendHttpRequestEvent) {
            return new HttpResponse[]{((EffSecSendHttpRequest.SendHttpRequestEvent) e).getResponse()};
        }
        return null;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends HttpResponse> getReturnType() {
        return HttpResponse.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "event response";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }
}
