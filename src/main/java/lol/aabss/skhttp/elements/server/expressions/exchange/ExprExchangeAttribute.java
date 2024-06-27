package lol.aabss.skhttp.elements.server.expressions.exchange;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import lol.aabss.skhttp.objects.server.HttpExchange;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("HTTP Attribute by Key")
@Description("Gets a attribute by it's key.")
@Examples({
        "set attribute of {_exchange} by key \"named\" to 100",
        "send attribute of {_exchange} by key \"name\""
})
@Since("1.3")
public class ExprExchangeAttribute extends SimpleExpression<Object> {

    static {
        Skript.registerExpression(ExprExchangeAttribute.class, Object.class, ExpressionType.COMBINED,
                "attribute of %httpexchange% (with|by|from) key %string%"
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
        return new Object[]{exchange.attribute(key)};
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode.equals(Changer.ChangeMode.SET)){
            return CollectionUtils.array(Object.class);
        }
        return null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.ChangeMode mode) {
        if (mode.equals(Changer.ChangeMode.SET)){
            HttpExchange exchange = this.exchange.getSingle(e);
            if (exchange == null){
                return;
            }
            String key = this.key.getSingle(e);
            if (key == null){
                return;
            }
            if (delta[0] == null){
                return;
            }
            exchange.attribute(key, delta[0]);
        }
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
        return "attribute of exchange by key";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exchange = (Expression<HttpExchange>) exprs[0];
        key = (Expression<String>) exprs[1];
        return true;
    }
}
