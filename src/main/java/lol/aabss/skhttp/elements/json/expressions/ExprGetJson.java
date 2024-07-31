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
import com.google.gson.JsonElement;
import lol.aabss.skhttp.objects.Json;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static lol.aabss.skhttp.SkHttp.instance;

@Name("Json - Get Object from Json")
@Description("Gets an object from a json object/array")
@Examples({
        "set {_value} to key \"uuid\" from {_object}",
        "set {_value2} to index 1 of {_array}"
})
@Since("1.4")
public class ExprGetJson extends SimpleExpression<Object> {

    static {
        if (Bukkit.getPluginManager().getPlugin("SkJson") == null) {
            Skript.registerExpression(ExprGetJson.class, Object.class, ExpressionType.COMBINED,
                    "key %string% (from|of) [json[[ ](object|array)]] %jsonobjects/jsonarrays%",
                    "index %integer% (from|of) [json[[ ](object|array)]] %jsonobjects/jsonarrays%"
            );
        }
    }

    private Expression<String> key;
    private Expression<Integer> index;
    private Expression<JsonElement> json;

    @Override
    protected Object @NotNull [] get(@NotNull Event e) {
        List<Object> values = new ArrayList<>();
        for (JsonElement jsonElement : this.json.getArray(e)){
            Json json = new Json(jsonElement.isJsonObject() ? jsonElement.getAsJsonObject() : jsonElement.getAsJsonArray(), e);
            if (key != null) {
                String key = this.key.getSingle(e);
                if (key == null){
                    return new Object[]{};
                }
                values.add(json.get(key));
            } else if (index != null){
                Integer index = this.index.getSingle(e);
                if (index == null){
                    return new Object[]{};
                }
                index = index+(instance.getConfig().getBoolean("use-skript-index", false) ? 1 : 0);
                values.add(json.get(index));
            }
        }
        return values.toArray(Object[]::new);
    }

    @Override
    public boolean isSingle() {
        return json.isSingle();
    }

    @Override
    public @NotNull Class<?> getReturnType() {
        return Object.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "get object from json";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (matchedPattern == 0){
            key = (Expression<String>) exprs[0];
        } else {
            index = (Expression<Integer>) exprs[0];
        }
        json = (Expression<JsonElement>) exprs[1];
        return true;
    }
}
