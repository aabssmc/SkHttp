package lol.aabss.skhttp.elements.server.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import lol.aabss.skhttp.objects.server.HttpExchange;
import lol.aabss.skhttp.objects.server.HttpServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Http Address")
@Description("Returns the inetaddress of either a http server or http exchange.")
@Examples({
        "send remote address of {_exchange}"
})
@Since("1.3")
public class ExprHttpAddress extends SimplePropertyExpression<Object, String> {

    static {
        register(ExprHttpAddress.class, String.class, "[:local|remote] address", "httpexchanges/httpservers");
    }

    private boolean local;

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        local = parseResult.hasTag("local");
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "address";
    }

    @Override
    public @Nullable String convert(Object object) {
        if (object instanceof HttpServer){
            return ((HttpServer) object).address().getAddress().getHostAddress();
        } else if (object instanceof HttpExchange){
            if (local){
                return ((HttpExchange) object).localAddress().getAddress().getHostAddress();
            } else {
                return ((HttpExchange) object).remoteAddress().getAddress().getHostAddress();
            }
        }
        return null;
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }
}
