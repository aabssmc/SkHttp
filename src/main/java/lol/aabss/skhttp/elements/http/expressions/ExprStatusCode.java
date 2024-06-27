package lol.aabss.skhttp.elements.http.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import lol.aabss.skhttp.objects.server.HttpExchange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.http.HttpResponse;


@Name("Response Status Code")
@Description("Returns the status code of a response/exchange.")
@Examples({
        "if status code of {_r} = 404:",
        "\tsend \"bad response\" to console"
})
@Since("1.0")
public class ExprStatusCode extends SimplePropertyExpression<Object, Integer> {

    static {
        register(ExprStatusCode.class, Integer.class, "(status|response) code", "httpresponses/httpexchanges");
    }


    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }


    @Override
    protected @NotNull String getPropertyName() {
        return "status code";
    }

    @Override
    public @Nullable Integer convert(Object object) {
        if (object instanceof HttpResponse<?>){
            return ((HttpResponse<?>) object).statusCode();
        } else if (object instanceof HttpExchange) {
            return ((HttpExchange) object).code();
        }
        return 0;
    }
}
