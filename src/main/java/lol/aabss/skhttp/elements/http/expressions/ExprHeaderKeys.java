package lol.aabss.skhttp.elements.http.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Name("HTTP Header Keys")
@Description("Gets all header keys.")
@Examples({
        "send header keys of {_request}"
})
@Since("1.3")
public class ExprHeaderKeys extends SimplePropertyExpression<Object, String[]> {

    static {
        register(ExprHeaderKeys.class, String[].class, "header keys", "httpresponses/httprequests");
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "header keys";
    }

    @Override
    public @Nullable String[] convert(Object object) {
        if (object instanceof HttpResponse<?>){
            return ((HttpResponse<?>) object).headers().map().keySet().toArray(String[]::new);
        } else if (object instanceof HttpRequest){
            return ((HttpRequest) object).headers().map().keySet().toArray(String[]::new);
        }
        return new String[]{};
    }

    @Override
    public @NotNull Class<? extends String[]> getReturnType() {
        return String[].class;
    }
}
