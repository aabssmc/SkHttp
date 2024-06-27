package lol.aabss.skhttp.elements.discord.expressions;

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
import com.itsradiix.discordwebhook.embed.models.Footer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Embed Footer")
@Description("Makes a new embed footer with a text and an icon.")
@Examples({
        "set {_footer} to footer with text \"Hi\" and with icon \"https://aabss.lol/assets/aabss.jpg\""
})
@Since("1.1")
public class ExprEmbedFooter extends SimpleExpression<Footer> {

    static {
        Skript.registerExpression(ExprEmbedFooter.class, Footer.class, ExpressionType.COMBINED,
                "[a] [new] [embed] footer with [text] %string% [[and] with icon [url] %-string%]"
        );
    }

    private Expression<String> text;
    private Expression<String> url;

    @Override
    protected Footer @NotNull [] get(@NotNull Event e) {
        String text = this.text.getSingle(e);
        if (text != null){
            if (url != null){
                String url = this.url.getSingle(e);
                if (url != null){
                    return new Footer[]{new Footer(text, url)};
                }
            } else{
                return new Footer[]{new Footer(text)};
            }
        }
        return new Footer[]{};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Footer> getReturnType() {
        return Footer.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "new embed footer";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        text = (Expression<String>) exprs[0];
        url = (Expression<String>) exprs[1];
        return true;
    }
}
