package lol.aabss.skhttp.elements.json.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.google.gson.JsonElement;
import lol.aabss.skhttp.objects.Json;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class CondJsonHas extends Condition {

    static {
        Skript.registerCondition(CondJsonHas.class,
                "%jsonarrays/jsonobjects% (has|contains) [value] %object%"
        );
    }

    private Expression<JsonElement> json;
    private Expression<Object> object;

    @Override
    public boolean check(@NotNull Event e) {
        Object object = this.object.getSingle(e);
        if (object == null){
            return false;
        }
        for (JsonElement element : json.getArray(e)){
            if (!new Json(element, e).has(object, e)){
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        json = (Expression<JsonElement>) exprs[0];
        object = (Expression<Object>) exprs[1];
        return true;
    }
}
