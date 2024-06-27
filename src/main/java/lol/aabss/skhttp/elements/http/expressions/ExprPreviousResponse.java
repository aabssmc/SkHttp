package lol.aabss.skhttp.elements.http.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


@Name("Previous HTTP Response")
@Description("Returns the previous HTTP response of another httpresponse.")
@Examples({
        "set {_r} to previous response of last response"
})
@Since("1.0")
public class ExprPreviousResponse extends PropertyExpression<HttpResponse<String>, HttpResponse> {

    static {
        register(ExprPreviousResponse.class, HttpResponse.class, "previous [http] response", "httpresponses");
    }

    @Override
    protected HttpResponse<?> @NotNull [] get(@NotNull Event event, HttpResponse<String> @NotNull [] source) {
        List<HttpResponse<?>> responses = new ArrayList<>();
        for (HttpResponse<?> response : source){
            responses.add(response.previousResponse().get());
        }
        return responses.toArray(HttpResponse<?>[]::new);
    }

    @Override
    public @NotNull Class<? extends HttpResponse> getReturnType() {
        return HttpResponse.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "previous responses";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        setExpr((Expression<? extends HttpResponse<String>>) exprs[0]);
        return true;
    }
}
