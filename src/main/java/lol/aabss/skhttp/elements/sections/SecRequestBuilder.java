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
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

@Name("HTTP Request Builder")
@Description("Builds a HTTP request.")
@Examples({
        "http request builder:",
        "\turl: \"https://www.someurl.com\"",
        "\tmethod: \"GET\"",
        "\tvariable: {_var}",
        "",
        "http request builder:",
        "\turl: \"https://www.someurl.com\"",
        "\tmethod: \"GET\"",
        "\tbody: \"some body text\"",
        "\tvariable: {_var}"
})
@Since("1.0")
public class SecRequestBuilder extends Section {

    private static final EntryValidator.EntryValidatorBuilder ENTRY_VALIDATOR = EntryValidator.builder();
    private Expression<String> url;
    private Expression<String> method;
    private Expression<String> body;
    private Expression<String> headers;
    private Variable<?> var;

    static {
        Skript.registerSection(SecRequestBuilder.class,
                "[a] [new] http request [builder]"
        );
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("url", null, false, String.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("method", null, false, String.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("body", null, true, String.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("headers", null, true, String[].class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("variable", null, false, Object.class));
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult, @NotNull SectionNode sectionNode, @NotNull List<TriggerItem> triggerItems) {
        EntryContainer container = ENTRY_VALIDATOR.build().validate(sectionNode);
        if (container == null) return false;
        this.url = (Expression<String>) container.getOptional("url", false);
        if (this.url == null) return false;
        this.method = (Expression<String>) container.getOptional("method", false);
        if (this.method == null) return false;
        this.body = (Expression<String>) container.getOptional("body", false);
        if (this.body == null) return false;
        this.headers = (Expression<String>) container.getOptional("headers", false);
        if (this.headers == null) return false;
        var = (Variable<?>) container.getOptional("variable", false);
        return var != null;
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event e) {
        execute(e);
        return super.walk(e, false);
    }

    private void execute(Event e) {
        String uri = this.url.getSingle(e);
        if (uri == null) {
            return;
        }
        String method = this.method.getSingle(e);
        if (method == null) {
            return;
        }
        Variable<?> var = this.var;
        if (var == null) {
            return;
        }
        HttpRequest.Builder request;
        if (body == null){
            request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .method(method, HttpRequest.BodyPublishers.noBody());
        } else{
            String body = this.body.getSingle(e);
            if (body != null) {
                request = HttpRequest.newBuilder()
                        .uri(URI.create(uri))
                        .method(method, HttpRequest.BodyPublishers.ofString(body));
            } else{
                return;
            }
        }
        HttpRequest[] http;
        if (headers == null){
            http = new HttpRequest[]{request.build()};
        } else{
            String[] headers = this.headers.getArray(e);
            http = new HttpRequest[]{request.headers(headers).build()};
        }
        var.change(e, http, Changer.ChangeMode.SET);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "http request builder";
    }
}
