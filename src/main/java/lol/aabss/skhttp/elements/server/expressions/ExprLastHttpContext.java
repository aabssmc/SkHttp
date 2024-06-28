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
import lol.aabss.skhttp.objects.server.HttpContext;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import static lol.aabss.skhttp.SkHttp.LAST_CONTEXT;

@Name("Last Http Context")
@Description("Returns the last http context.")
@Examples({
        "set {_r} to last context"
})
@Since("1.3")
public class ExprLastHttpContext extends SimpleExpression<HttpContext> {

    static {
        Skript.registerExpression(ExprLastHttpContext.class, HttpContext.class, ExpressionType.SIMPLE,
                "[the] last [http] context"
        );
    }

    @Override
    protected HttpContext @NotNull [] get(@NotNull Event e) {
        return new HttpContext[]{LAST_CONTEXT};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends HttpContext> getReturnType() {
        return HttpContext.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "last context";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }
}
