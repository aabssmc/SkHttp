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

@Name("Response Body")
@Description("Returns the body of a response.")
@Examples({
        "send body of {_r}"
})
@Since("1.0")
public class ExprBody extends PropertyExpression<HttpResponse<String>, String> {

    static {
        register(ExprBody.class, String.class, "[response] body", "httpresponses");
    }

    @Override
    protected String @NotNull [] get(@NotNull Event event, HttpResponse<String> @NotNull [] source) {
        List<String> bodys = new ArrayList<>();
        for (HttpResponse<String> response : source){
            bodys.add(response.body());
        }
        return bodys.toArray(String[]::new);
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "body";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        setExpr((Expression<? extends HttpResponse<String>>) exprs[0]);
        return true;
    }
}
