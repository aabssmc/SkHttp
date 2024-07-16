package lol.aabss.skhttp.elements.discord.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.util.Kleenean;
import com.itsradiix.discordwebhook.models.embeds.*;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.Arrays;
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
        "\tthumbnail: \"https://aabss.cc/assets/bg.jpg\"",
        "\timage: \"https://aabss.cc/assets/aabss.jpg\"",
        "\tauthor: author with text \"aabss\" and url \"https://aabss.cc\"",
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
    private Expression<String> timestamp;
    private Expression<Footer> footer;
    private Expression<String> thumbnail;
    private Expression<String> image;
    private Expression<Author> author;
    private Expression<Field> fields;
    private Variable<?> var;

    static {
        Skript.registerSection(SecEmbedBuilder.class,
                "discord [webhook] embed [builder]"
        );
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("title", null, true, String.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("description", null, true, String.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("url", null, true, String.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("color", null, true, String.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("timestamp", null, true, String.class));
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
        this.timestamp = (Expression<String>) container.getOptional("timestamp", false);
        this.footer = (Expression<Footer>) container.getOptional("footer", false);
        this.thumbnail = (Expression<String>) container.getOptional("thumbnail", false);
        this.image = (Expression<String>) container.getOptional("image", false);
        this.author = (Expression<Author>) container.getOptional("author", false);
        this.fields = (Expression<Field>) container.getOptional("fields", false);
        if (container.getOptional("variable", false) instanceof Variable<?>){
            this.var = (Variable<?>) container.getOptional("variable", false);
            return var != null;
        } else {
            Skript.error("The object expression must be a variable.");
            return false;
        }
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
        Embed builder;
        builder = new Embed();
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
                builder = builder.setUrl(url);
            }
        }
        if (color != null){
            String color = this.color.getSingle(e);
            if (color != null){
                builder = builder.setColor(color);
            }
        }
        if (timestamp != null){
            String timestamp = this.timestamp.getSingle(e);
            if (timestamp != null) {
                builder = builder.setTimestamp(timestamp);
            }
        }
        if (footer != null){
            Footer footer = this.footer.getSingle(e);
            if (footer != null){
                builder = builder.setFooter(footer);
            }
        }
        if (thumbnail != null){
            String thumbnail = this.thumbnail.getSingle(e);
            if (thumbnail != null){
                builder = builder.setThumbnail(new Thumbnail(thumbnail, null));
            }
        }
        if (image != null){
            String image = this.image.getSingle(e);
            if (image != null){
                builder = builder.setImage(new Image(image, null));
            }
        }
        if (author != null){
            Author author = this.author.getSingle(e);
            if (author != null){
                builder = builder.setAuthor(author);
            }
        }
        if (fields != null){
            builder = builder.setFields(Arrays.stream(fields.getArray(e)).toList());
        }
        var.change(e, new Embed[]{builder}, Changer.ChangeMode.SET);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "discord embed builder";
    }
}
