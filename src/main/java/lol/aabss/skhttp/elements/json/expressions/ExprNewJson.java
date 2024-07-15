package lol.aabss.skhttp.elements.json.expressions;

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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Json - New Json Object/Array")
@Description("Gets a new json object or array")
@Examples({
        "set {_object} to new json object",
        "set {_array} to a json array"
})
@Since("1.4")
public class ExprNewJson extends SimpleExpression<Object> {

    static {
        Skript.registerExpression(ExprNewJson.class, Object.class, ExpressionType.COMBINED,
                "[a] [new] json[ ](object|:array)",
                "json[[ ](object|array)] from string %string%"
        );
    }

    private boolean array;
    private Expression<String> string;

    @Override
    protected Object @NotNull [] get(@NotNull Event e) {
        if (string != null){
            String string = this.string.getSingle(e);
            if (string != null){
                return new Object[]{JsonParser.parseString(string)};
            }
        }
        if (array) {
            return new Object[]{new JsonArray()};
        }
        return new Object[]{new JsonObject()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<?> getReturnType() {
        return (array ? JsonArray.class : JsonObject.class);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "new json";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        array = parseResult.hasTag("array");
        if (matchedPattern == 1) string = (Expression<String>) exprs[0];
        return true;
    }
}
