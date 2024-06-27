package lol.aabss.skhttp.elements.expressions;

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
import lol.aabss.skhttp.elements.sections.SecSendAsyncHttpRequest;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.net.http.HttpResponse;

@Name("HTTP Response")
@Description("Returns the http response in a send async http event.")
@Examples({
        "send event-httpresponse"
})
@Since("1.2")
public class ExprEventResponse extends EventValueExpression<HttpResponse> {

    static {
        Skript.registerExpression(ExprEventResponse.class, HttpResponse.class, ExpressionType.SIMPLE,
                "[the] [event-][http[ |-]]response"
        );
    }

    public ExprEventResponse() {
        super(HttpResponse.class);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (getParser().isCurrentEvent(SecSendAsyncHttpRequest.SendAsyncHttpEvent.class)){
            return true;
        }
        Skript.error("'httpresponse' can not be used outside of Send Async Http Event");
        return false;
    }

    @Override
    protected HttpResponse<?> @NotNull [] get(@NotNull Event event) {
        return new HttpResponse<?>[]{((SecSendAsyncHttpRequest.SendAsyncHttpEvent) event).getResponse()};
    }
}
