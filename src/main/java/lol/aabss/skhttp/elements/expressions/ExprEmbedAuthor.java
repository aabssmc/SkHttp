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
import com.itsradiix.embed.models.Author;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Embed Author")
@Description("Makes a new embed author with a text, url and an icon.")
@Examples({
        "set {_author} to author with text \"Hi\" and with url \"https://aabss.lol\" and with icon \"https://aabss.lol/assets/aabss.jpg\""
})
@Since("1.1")
public class ExprEmbedAuthor extends SimpleExpression<Author> {

    static {
        Skript.registerExpression(ExprEmbedAuthor.class, Author.class, ExpressionType.COMBINED,
                "[a] [new] [embed] author with [text] %string% [and] with url %string% [[and] with icon [url] %-string%]"
        );
    }

    private Expression<String> text;
    private Expression<String> url;
    private Expression<String> icon;

    @Override
    protected Author @NotNull [] get(@NotNull Event e) {
        String text = this.text.getSingle(e);
        String url = this.url.getSingle(e);
        if (text != null && url != null){
            if (icon != null){
                String icon = this.icon.getSingle(e);
                if (icon != null){
                    return new Author[]{new Author(text, url, icon)};
                }
            } else{
                return new Author[]{new Author(text, url)};
            }
        }
        return new Author[]{null};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Author> getReturnType() {
        return Author.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "new embed author";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        text = (Expression<String>) exprs[0];
        url = (Expression<String>) exprs[1];
        icon = (Expression<String>) exprs[2];
        return true;
    }
}
