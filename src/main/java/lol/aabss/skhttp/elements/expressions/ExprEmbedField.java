package lol.aabss.skhttp.elements.expressions;

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
import com.itsradiix.discordwebhook.embed.models.Field;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Embed Field")
@Description("Makes a new optionally inlined embed field with a name and a value")
@Examples({
        "set {_field} to field with text \"Hi\" and with url \"https://aabss.lol\" and with icon \"https://aabss.lol/assets/aabss.jpg\""
})
@Since("1.1")
public class ExprEmbedField extends SimpleExpression<Field> {

    static {
        Skript.registerExpression(ExprEmbedField.class, Field.class, ExpressionType.COMBINED,
                "[a] [new] [inline:inline[d]] [embed] field named %string% [and] with value %string%"
        );
    }

    private boolean inline;
    private Expression<String> name;
    private Expression<String> value;

    @Override
    protected Field @NotNull [] get(@NotNull Event e) {
        String name = this.name.getSingle(e);
        String value = this.value.getSingle(e);
        if (name != null && value != null){
            return new Field[]{new Field(name, value, inline)};
        }
        return new Field[]{null};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Field> getReturnType() {
        return Field.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "new embed field";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        name = (Expression<String>) exprs[0];
        value = (Expression<String>) exprs[1];
        inline = parseResult.hasTag("inline");
        return true;
    }
}
