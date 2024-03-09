package lol.aabss.skhttp.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Name("Http Url")
@Description("Returns the url of a http response or request.")
@Examples({
        "send url of {_r}"
})
@Since("1.0")
public class ExprURL extends PropertyExpression<Object, String> {

    static {
        register(ExprURL.class, String.class, "[repsonse|request] ur(l|i)", "httpresponses/httprequests");
    }

    @Override
    protected String @NotNull [] get(@NotNull Event event, Object @NotNull [] source) {
        List<String> urls = new ArrayList<>();
        for (Object response : source){
            if (response instanceof HttpResponse<?>) {
                urls.add(((HttpResponse<?>) response).uri().toString());
            } else if (response instanceof HttpRequest){
                urls.add(((HttpRequest) response).uri().toString());
            }
        }
        return urls.toArray(String[]::new);
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "url";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        setExpr(exprs[0]);
        return true;
    }
}
