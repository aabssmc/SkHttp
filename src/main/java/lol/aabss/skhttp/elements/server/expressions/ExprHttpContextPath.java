package lol.aabss.skhttp.elements.server.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import lol.aabss.skhttp.objects.server.HttpContext;
import lol.aabss.skhttp.objects.server.HttpExchange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Http Path")
@Description("Returns the path of either a http context or http exchange.")
@Examples({
        "send full path of {_exchange}"
})
@Since("1.3")
public class ExprHttpContextPath extends SimplePropertyExpression<Object, String> {

    static {
        register(ExprHttpContextPath.class, String.class,
                "[:full] [ur(l|i)] path", "httpcontexts/httpexchanges"
        );
    }

    private boolean full;

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        full = parseResult.hasTag("full");
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "url path";
    }

    @Override
    public @Nullable String convert(Object object) {
        if (object instanceof HttpContext){
            return ((HttpContext) object).path();
        } else if (object instanceof HttpExchange){
            if (full) return ((HttpExchange) object).fullPath();
            return ((HttpExchange) object).path();
        }
        return null;
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }
}
