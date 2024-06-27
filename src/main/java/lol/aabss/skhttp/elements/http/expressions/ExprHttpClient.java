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

import java.net.http.HttpClient;

@Name("New HTTP Client")
@Description("Returns a new HTTP client.")
@Examples({
        "set {_c} to a new http client."
})
@Since("1.0")
public class ExprHttpClient extends SimpleExpression<HttpClient> {

    static {
        Skript.registerExpression(ExprHttpClient.class, HttpClient.class, ExpressionType.SIMPLE,
                "[a] [new] [empty] http client"
        );
    }

    @Override
    protected HttpClient @NotNull [] get(@NotNull Event e) {
        return new HttpClient[]{HttpClient.newHttpClient()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends HttpClient> getReturnType() {
        return HttpClient.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "new http client";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }
}
