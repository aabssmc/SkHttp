package lol.aabss.skhttp.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import lol.aabss.skhttp.objects.RequestObject;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

@Name("Http Method")
@Description("Returns the method of a http request.")
@Examples({
        "send method of {_r}"
})
@Since("1.0")
public class ExprMethod extends PropertyExpression<RequestObject, String> {

    static {
        register(ExprMethod.class, String.class, "[request] method", "httprequests");
    }

    @Override
    protected String @NotNull [] get(@NotNull Event event, RequestObject @NotNull [] source) {
        List<String> methods = new ArrayList<>();
        for (RequestObject requestObject : source){
            HttpRequest request = requestObject.request;
            if (request != null){
                methods.add(request.method());
            }
        }
        return methods.toArray(String[]::new);
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "method";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        setExpr((Expression<? extends RequestObject>) exprs[0]);
        return true;
    }
}
