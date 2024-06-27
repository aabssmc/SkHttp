package lol.aabss.skhttp.elements.server.expressions.context;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import lol.aabss.skhttp.objects.server.HttpContext;
import lol.aabss.skhttp.objects.server.HttpServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Http Server from Http Context")
@Description("Gets thee http server from a context.")
@Examples({
        "send full path of {_exchange}"
})
@Since("1.3")
public class ExprHttpContextServer extends SimplePropertyExpression<HttpContext, HttpServer> {

    static {
        register(ExprHttpContextServer.class, HttpServer.class, "http server", "httpcontexts");
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "http server of context";
    }

    @Override
    public @Nullable HttpServer convert(HttpContext context) {
        return context.server();
    }

    @Override
    public @NotNull Class<? extends HttpServer> getReturnType() {
        return HttpServer.class;
    }
}
