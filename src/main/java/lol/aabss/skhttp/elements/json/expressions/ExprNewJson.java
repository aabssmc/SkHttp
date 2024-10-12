package lol.aabss.skhttp.elements.json.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.UnparsedLiteral;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.LiteralUtils;
import ch.njol.util.Kleenean;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lol.aabss.skhttp.SkHttp;
import lol.aabss.skhttp.objects.Json;
import org.bukkit.Bukkit;
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
public class ExprNewJson extends SimpleExpression<JsonElement> {

    static {
        if (SkHttp.instance.getConfig().getBoolean("json-elements", true)) {
            Skript.registerExpression(ExprNewJson.class, JsonElement.class, ExpressionType.COMBINED,
                    "[a] [new] json[ ](object|:array)",
                    "json[[ ](object|array)] from [string|object] %objects%"
            );
        }
    }

    private boolean array;
    private Expression<Object> object;

    @Override
    protected JsonElement @NotNull [] get(@NotNull Event e) {
        if (object != null){
            Object object;
            if (this.object.isSingle()) {
                object = this.object.getSingle(e);
            } else {
                object = this.object.getArray(e);
            }
            if (object != null){
                return new JsonElement[]{Json.toJsonElement(object, e)};
            }
        }
        if (array) {
            return new JsonElement[]{new JsonArray()};
        }
        return new JsonElement[]{new JsonObject()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends JsonElement> getReturnType() {
        return (array ? JsonArray.class : JsonObject.class);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "new json";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        array = parseResult.hasTag("array");
        if (matchedPattern == 1) object = (Expression<Object>) exprs[0];
        if (this.object instanceof UnparsedLiteral) {
            object = LiteralUtils.defendExpression(object);
        }
        if (this.object != null) {
            return LiteralUtils.canInitSafely(object);
        }
        return true;
    }
}
