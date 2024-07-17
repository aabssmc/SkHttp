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
import lol.aabss.skhttp.objects.server.HttpServer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import static lol.aabss.skhttp.SkHttp.LAST_SERVER;

@Name("Last Http Server")
@Description("Returns the last http server.")
@Examples({
        "set {_r} to last server"
})
@Since("1.3")
public class ExprLastHttpServer extends SimpleExpression<HttpServer> {

    static {
        Skript.registerExpression(ExprLastHttpServer.class, HttpServer.class, ExpressionType.SIMPLE,
                "[the] last [http[s]] server"
        );
    }

    @Override
    protected HttpServer @NotNull [] get(@NotNull Event e) {
        return new HttpServer[]{LAST_SERVER};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends HttpServer> getReturnType() {
        return HttpServer.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "last server";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        return true;
    }
}
