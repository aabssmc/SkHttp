package lol.aabss.skhttp.elements.http.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import lol.aabss.skhttp.objects.server.HttpExchange;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Name("HTTP Header By Key")
@Description("Gets a header by it's key.")
@Examples({
        "send header of {_request} by key \"Content-Type\""
})
@Since("1.3")
public class ExprHeadersByKey extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprHeadersByKey.class, String.class, ExpressionType.COMBINED,
                "header of %httpresponses/httprequests/httpexchanges% (with|by|from) key %string%"
        );
    }

    private boolean request;

    private Expression<Object> object;
    private Expression<String> key;
    @Override
    protected String @NotNull [] get(@NotNull Event e) {
        Object obj = this.object.getSingle(e);
        if (obj == null){
            return new String[]{};
        }
        String key = this.key.getSingle(e);
        if (key == null){
            return new String[]{};
        }
        if (obj instanceof HttpResponse<?>){
            return ((HttpResponse<?>) obj).headers().map().get(key).toArray(String[]::new);
        } else if (obj instanceof HttpRequest){
            return ((HttpRequest) obj).headers().map().get(key).toArray(String[]::new);
        } else if (object instanceof HttpExchange){
            if (request) {
                return ((HttpExchange) object).requestHeaders().get(key).toArray(String[]::new);
            }
            return ((HttpExchange) object).responseHeaders().get(key).toArray(String[]::new);
        }
        return new String[]{};
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "header by key";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        object = (Expression<Object>) exprs[0];
        key = (Expression<String>) exprs[1];
        request = parseResult.hasTag("request");
        return true;
    }
}
