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
import lol.aabss.skhttp.SkHttp;
import lol.aabss.skhttp.objects.server.HttpServer;
import lol.aabss.skhttp.objects.server.HttpsServer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Random;

@Name("New Http Server")
@Description("Returns a new http server with an optional specified port.")
@Examples({
        "set {_server} to new http server"
})
@Since("1.3")
public class ExprNewHttpServer extends SimpleExpression<HttpServer> {

    static {
        Skript.registerExpression(ExprNewHttpServer.class, HttpServer.class, ExpressionType.COMBINED,
                "[a] [new] http[s] server [with port %-integer%]"
        );
    }

    private Expression<Integer> port;

    @Override
    protected HttpServer @NotNull [] get(@NotNull Event e) {
        int defaultPort = SkHttp.instance.getConfig().getInt("default-server-port", -1);
        if (port != null) {
            Integer port = this.port.getSingle(e);
            if (port != null) {
                return server(port);
            }
        }
        if (defaultPort != -1) {
            return server(defaultPort);
        }
        return server(new Random().nextInt(1024, 99999));
    }

    private HttpServer[] server(int port){
        return new HttpServer[]{new HttpServer(port)};
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
        return "new http server";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        port = (Expression<Integer>) exprs[0];
        return true;
    }
}
