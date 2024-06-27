package lol.aabss.skhttp.elements.http.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import lol.aabss.skhttp.objects.RequestObject;
import lol.aabss.skhttp.objects.server.HttpExchange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Http Method")
@Description("Returns the method of a http request/exchange.")
@Examples({
        "send method of {_r}"
})
@Since("1.0")
public class ExprMethod extends SimplePropertyExpression<Object, String> {

    static {
        register(ExprMethod.class, String.class, "[request] method", "httprequests/httpexchanges");
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "method";
    }

    @Override
    public @Nullable String convert(Object object) {
        if (object instanceof RequestObject){
            return ((RequestObject) object).request.method();
        } else if (object instanceof HttpExchange){
            return ((HttpExchange) object).method();
        }
        return null;
    }
}
