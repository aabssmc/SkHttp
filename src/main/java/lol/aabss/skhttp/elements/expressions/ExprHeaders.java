package lol.aabss.skhttp.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import lol.aabss.skhttp.objects.RequestObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.http.HttpResponse;

@Name("HTTP Headers")
@Description("Returns the headers of a http response of request.")
@Examples({
        "send headers of {_request}."
})
@Since("1.2")
public class ExprHeaders extends SimplePropertyExpression<Object, String> {

    static {
        register(ExprHeaders.class, String.class, "[http] headers", "httpresponses/httprequests");
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "headers";
    }

    @Override
    public @Nullable String convert(Object object) {
        if (object instanceof HttpResponse<?>){
            return ((HttpResponse<?>) object).headers().map().toString();
        } else if (object instanceof RequestObject){
            return ((RequestObject) object).request.headers().map().toString();
        }
        return "";
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }
}
