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
import com.itsradiix.discordwebhook.embed.Embed;
import com.itsradiix.discordwebhook.embed.models.Author;
import com.itsradiix.discordwebhook.embed.models.Field;
import com.itsradiix.discordwebhook.embed.models.Footer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.List;

@Name("Discord Webhook Embed Builder")
@Description("Builds a discord webhook embed.")
@Examples({
        "discord embed builder:",
        "\ttitle: \"Embed title\"",
        "\tdescription: \"Embed Description\"",
        "\turl: \"https://discord.com/webhook\"",
        "\tcolor: \"##ffffff\"",
        "\ttimestamp: true",
        "\tfooter: footer with text \"hi\"",
        "\tthumbnail: \"https://aabss.lol/assets/bg.jpg\"",
        "\timage: \"https://aabss.lol/assets/aabss.jpg\"",
        "\tauthor: author with text \"aabss\" and url \"https://aabss.lol\"",
        "\tfields: {_field1}, {_field2}, {_field3}",
        "\tvariable: {_var}"
})
@Since("1.0")
public class SecEmbedBuilder extends Section {

    private static final EntryValidator.EntryValidatorBuilder ENTRY_VALIDATOR = EntryValidator.builder();
    private Expression<String> title;
    private Expression<String> description;
    private Expression<String> url;
    private Expression<String> color;
    private Expression<Boolean> timestamp;
    private Expression<Footer> footer;
    private Expression<String> thumbnail;
    private Expression<String> image;
    private Expression<Author> author;
    private Expression<Field> fields;
    private Variable<?> var;

    static {
        Skript.registerSection(SecRequestBuilder.class,
                "discord [webhook] embed [builder]"
        );
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("title", null, true, String.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("description", null, true, String.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("url", null, true, String.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("color", null, true, String.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("timestamp", null, true, Boolean.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("footer", null, true, Footer.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("thumbnail", null, true, String.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("image", null, true, String.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("author", null, true, Author.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("fields", null, true, Field[].class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("variable", null, false, Object.class));
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult, @NotNull SectionNode sectionNode, @NotNull List<TriggerItem> triggerItems) {
        EntryContainer container = ENTRY_VALIDATOR.build().validate(sectionNode);
        if (container == null) return false;
        this.title = (Expression<String>) container.getOptional("title", false);
        this.description = (Expression<String>) container.getOptional("description", false);
        this.url = (Expression<String>) container.getOptional("url", false);
        this.color = (Expression<String>) container.getOptional("color", false);
        this.timestamp = (Expression<Boolean>) container.getOptional("timestamp", false);
        this.footer = (Expression<Footer>) container.getOptional("footer", false);
        this.thumbnail = (Expression<String>) container.getOptional("thumbnail", false);
        this.image = (Expression<String>) container.getOptional("image", false);
        this.author = (Expression<Author>) container.getOptional("author", false);
        this.fields = (Expression<Field>) container.getOptional("fields", false);
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
        Embed.Builder builder;
        builder = new Embed.Builder();
        if (title != null){
            String title = this.title.getSingle(e);
            if (title != null){
                builder = builder.title(title);
            }
        }
        if (description != null){
            String description = this.description.getSingle(e);
            if (description != null){
                builder = builder.description(description);
            }
        }
        if (url != null){
            String url = this.url.getSingle(e);
            if (url != null){
                builder = builder.url(url);
            }
        }
        if (color != null){
            String color = this.color.getSingle(e);
            if (color != null){
                builder = builder.color(color);
            }
        }
        if (timestamp != null){
            if (Boolean.TRUE.equals(this.timestamp.getSingle(e))) {
                builder = builder.timestamp();
            }
        }
        if (footer != null){
            Footer footer = this.footer.getSingle(e);
            if (footer != null){
                if (footer.getIcon_url() != null) {
                    builder = builder.footer(footer.getText(), footer.getIcon_url());
                } else{
                    builder = builder.footer(footer.getText());
                }
            }
        }
        if (thumbnail != null){
            String thumbnail = this.thumbnail.getSingle(e);
            if (thumbnail != null){
                builder = builder.thumbnail(thumbnail);
            }
        }
        if (image != null){
            String image = this.image.getSingle(e);
            if (image != null){
                builder = builder.image(image);
            }
        }
        if (author != null){
            Author author = this.author.getSingle(e);
            if (author != null){
                if (author.getIcon_url() != null) {
                    builder = builder.author(author.getName(), author.getUrl(), author.getIcon_url());
                } else{
                    builder = builder.author(author.getName(), author.getUrl());
                }
            }
        }
        if (fields != null){
            for (Field field : fields.getArray(e)){
                builder = builder.field(field.getName(), field.getValue(), field.isInline());
            }
        }
        var.change(e, new Embed[]{builder.build()}, Changer.ChangeMode.SET);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "discord embed builder";
    }
}
