package lol.aabss.skhttp.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.itsradiix.discordwebhook.DiscordWebHook;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Send Discord Webhook")
@Description("Sends strings to a discord webhook.")
@Examples({
        "send \"ok 123\" to discord webhook with url \"https://discord.com/something\""
})
@Since("1.1")
public class EffSendDiscordWebhook extends Effect {

    static {
        Skript.registerEffect(EffSendDiscordWebhook.class,
                "(send|post) %discordwebhook/string% to [discord] webhook with url %string%"
        );
    }

    private Expression<Object> text;
    private Expression<String> url;

    @Override
    protected void execute(@NotNull Event e) {
        if (url != null && text != null) {
            String url = this.url.getSingle(e);
            if (url != null) {
                Object text = this.text.getSingle(e);
                if (text instanceof String t) {
                    DiscordWebHook message = new DiscordWebHook.Builder()
                            .content(t)
                            .build();
                    DiscordWebHook.sendMessage(url, message);
                } else if (text instanceof DiscordWebHook t){
                    DiscordWebHook.sendMessage(url, t);
                }
            }
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "send discord webhook";
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        text = (Expression<Object>) exprs[0];
        url = (Expression<String>) exprs[1];
        return true;
    }
}
