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

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Name("Http Version")
@Description("Returns the version of a http response, request or client.")
@Examples({
        "send version of {_r}"
})
@Since("1.0")
public class ExprVersion extends PropertyExpression<Object, String> {

    static {
        register(ExprVersion.class, String.class, "[repsonse|request] ur(l|i)", "httpresponses/httprequests/httpclients");
    }

    @Override
    protected String @NotNull [] get(@NotNull Event event, Object @NotNull [] source) {
        List<String> versions = new ArrayList<>();
        for (Object response : source){
            if (response instanceof HttpResponse<?>) {
                versions.add(((HttpResponse<?>) response).version().name().toLowerCase());
            } else if (response instanceof HttpRequest){
                versions.add(((HttpRequest) response).version().get().name().toLowerCase());
            } else if (response instanceof HttpClient){
                versions.add(((HttpClient) response).version().name().toLowerCase());
            }
        }
        return versions.toArray(String[]::new);
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "version";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        setExpr(exprs[0]);
        return true;
    }
}
