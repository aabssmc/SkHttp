package lol.aabss.skhttp.elements.http.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import lol.aabss.skhttp.objects.server.HttpExchange;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Name("Response/Request Body")
@Description("Returns the body of a response/exchange.")
@Examples({
        "send body of {_r}"
})
@Since("1.0")
public class ExprBody extends SimplePropertyExpression<Object, String> {

    static {
        register(ExprBody.class, String.class, "[:request|response] body", "httpresponses/httpexchanges");
    }

    private boolean request;

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        request = parseResult.hasTag("request");
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "body";
    }

    @Override
    public @Nullable String convert(Object object) {
        if (object instanceof HttpResponse<?>){
            return (String) ((HttpResponse<?>) object).body();
        } else if (object instanceof HttpExchange){
            if (request) {
                return ((HttpExchange) object).requestBody();
            }
            return ((HttpExchange) object).responseBody();
        }
        return null;
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }
}
