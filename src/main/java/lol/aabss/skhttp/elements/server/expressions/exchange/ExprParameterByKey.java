package lol.aabss.skhttp.elements.server.expressions.exchange;

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

@Name("HTTP Parameter by Key")
@Description("Gets a parameter by it's key.")
@Examples({
        "send parameter of {_exchange} by key \"name\""
})
@Since("1.3")
public class ExprParameterByKey extends SimpleExpression<Object> {

    static {
        Skript.registerExpression(ExprExchangeAttribute.class, Object.class, ExpressionType.COMBINED,
                "(parameter|arg[ument]) of %httpexchange% (with|by|from) key %string%"
        );
    }

    private Expression<HttpExchange> exchange;
    private Expression<String> key;
    @Override
    protected Object @NotNull [] get(@NotNull Event e) {
        HttpExchange exchange = this.exchange.getSingle(e);
        if (exchange == null){
            return new Object[]{};
        }
        String key = this.key.getSingle(e);
        if (key == null){
            return new Object[]{};
        }
        return new Object[]{exchange.parameters().get(key)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<?> getReturnType() {
        return Object.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "parameter of exchange by key";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exchange = (Expression<HttpExchange>) exprs[0];
        key = (Expression<String>) exprs[1];
        return true;
    }
}
