package lol.aabss.skhttp.elements.server.expressions;

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


import static lol.aabss.skhttp.SkHttp.LAST_EXCHANGE;

@Name("Last Http Exchange")
@Description("Returns the last http exchange.")
@Examples({
        "set {_r} to last exchange"
})
@Since("1.3")
public class ExprLastHttpExchange extends SimpleExpression<HttpExchange> {

    static {
        Skript.registerExpression(ExprLastHttpExchange.class, HttpExchange.class, ExpressionType.SIMPLE,
                "[the] last [http] exchange"
        );
    }

    @Override
    protected HttpExchange @NotNull [] get(@NotNull Event e) {
        return new HttpExchange[]{LAST_EXCHANGE};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends HttpExchange> getReturnType() {
        return HttpExchange.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "last exchange";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }
}
