package lol.aabss.skhttp.elements.json.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.UnparsedLiteral;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.LiteralUtils;
import ch.njol.util.Kleenean;
import com.google.gson.JsonElement;
import lol.aabss.skhttp.objects.Json;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Json - Has Element")
@Description("Returns true if the json has the specified value or key")
@Examples({
        "if {_json} has value \"aabss\":",
        "if {_json} has key \"name\":",
        "\treturn true"
})
@Since("1.4")
public class CondJsonHas extends Condition {

    static {
        if (Bukkit.getPluginManager().getPlugin("SkJson") == null) {
            Skript.registerCondition(CondJsonHas.class,
                    "%jsonarrays/jsonobjects% (has|contains) (value|:key) %object%"
            );
        }
    }

    private Expression<JsonElement> json;
    private Expression<Object> object;
    private boolean key;

    @Override
    public boolean check(@NotNull Event e) {
        Object object = this.object.getSingle(e);
        if (object == null){
            return false;
        }
        for (JsonElement element : json.getArray(e)){
            if (key) {
                if (!new Json(element, e).hasKey(Classes.toString(object), e)) {
                    return false;
                }
            } else {
                if (!new Json(element, e).hasValue(object, e)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "json has value";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        json = (Expression<JsonElement>) exprs[0];
        object = (Expression<Object>) exprs[1];
        key = parseResult.hasTag("key");
        if (this.object instanceof UnparsedLiteral) {
            object = LiteralUtils.defendExpression(object);
        }
        return LiteralUtils.canInitSafely(object);
    }
}
