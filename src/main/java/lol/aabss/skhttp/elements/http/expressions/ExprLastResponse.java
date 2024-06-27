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
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.http.HttpResponse;

import static lol.aabss.skhttp.SkHttp.LAST_RESPONSE;

@Name("Last Http Response")
@Description("Returns the last http response.")
@Examples({
        "set {_r} to last response"
})
@Since("1.0")
public class ExprLastResponse extends SimpleExpression<HttpResponse> {

    static {
        Skript.registerExpression(ExprLastResponse.class, HttpResponse.class, ExpressionType.SIMPLE,
                "[the] last [http] response"
        );
    }

    @Override
    protected HttpResponse<?> @NotNull [] get(@NotNull Event e) {
        return new HttpResponse[]{LAST_RESPONSE};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends HttpResponse> getReturnType() {
        return HttpResponse.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "last response";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }
}
