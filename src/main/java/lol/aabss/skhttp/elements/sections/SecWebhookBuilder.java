package lol.aabss.skhttp.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.util.Kleenean;
import com.itsradiix.discordwebhook.DiscordWebHook;
import com.itsradiix.discordwebhook.embed.Embed;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.List;

@Name("Discord Webhook Builder")
@Description("Builds a discord webhook.")
@Examples({
        "discord webhook builder:",
        "\tcontent: \"SkHttp is a new skript addon for http things!\"",
        "\tusername: \"SkHttp\"",
        "\tavatar: \"https://aabss.lol/assets/aabss.jpg\"",
        "\tembed: {_embed}",
        "\tvariable: {_webhook}"
})
@Since("1.1")
public class SecWebhookBuilder extends Section {

    private static final EntryValidator.EntryValidatorBuilder ENTRY_VALIDATOR = EntryValidator.builder();
    private Expression<String> content;
    private Expression<String> username;
    private Expression<String> avatar;
    private Expression<Embed> embed;
    private Variable<?> var;

    static {
        Skript.registerSection(SecWebhookBuilder.class,
                "discord webhook [builder]"
        );
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("content", null, false, String.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("username", null, true, String.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("avatar", null, true, String.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("embed", null, true, Embed.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("variable", null, false, Object.class));
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult, @NotNull SectionNode sectionNode, @NotNull List<TriggerItem> triggerItems) {
        EntryContainer container = ENTRY_VALIDATOR.build().validate(sectionNode);
        if (container == null) return false;
        this.content = (Expression<String>) container.getOptional("content", false);
        if (content == null) return false;
        this.username = (Expression<String>) container.getOptional("username", false);
        this.avatar = (Expression<String>) container.getOptional("avatar", false);
        this.embed = (Expression<Embed>) container.getOptional("embed", false);
        this.var = (Variable<?>) container.getOptional("variable", false);
        return var != null;
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event e) {
        execute(e);
        return super.walk(e, false);
    }

    private void execute(Event e) {
        Variable<?> var = this.var;
        if (var == null) {
            return;
        }
        DiscordWebHook.Builder builder = new DiscordWebHook.Builder();
        String content = this.content.getSingle(e);
        if (content != null){
            builder = builder.content(content);
        }
        if (username != null){
            String username = this.username.getSingle(e);
            if (username != null){
                builder = builder.username(username);
            }
        }
        if (avatar != null){
            String avatar = this.avatar.getSingle(e);
            if (avatar != null){
                builder = builder.avatar_url(avatar);
            }
        }
        if (embed != null){
            Embed embed = this.embed.getSingle(e);
            if (embed != null){
                builder = builder.embed(embed);
            }
        }
        var.change(e, new DiscordWebHook[]{builder.build()}, Changer.ChangeMode.SET);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "discord webhook builder";
    }
}
