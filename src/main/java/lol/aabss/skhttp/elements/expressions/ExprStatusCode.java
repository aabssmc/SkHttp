package lol.aabss.skhttp.elements.expressions;

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


@Name("Response Status Code")
@Description("Returns the status code of a response.")
@Examples({
        "if status code of {_r} = 404:",
        "\tsend \"bad response\" to console"
})
@Since("1.0")
public class ExprStatusCode extends PropertyExpression<HttpResponse<String>, Integer> {

    static {
        register(ExprStatusCode.class, Integer.class, "(status|response) code", "httpresponses");
    }

    @Override
    protected Integer @NotNull [] get(@NotNull Event event, HttpResponse<String> @NotNull [] source) {
        List<Integer> ints = new ArrayList<>();
        for (HttpResponse<?> response : source){
            ints.add(response.statusCode());
        }
        return ints.toArray(Integer[]::new);
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "status code";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        setExpr((Expression<? extends HttpResponse<String>>) exprs[0]);
        return true;
    }
}
