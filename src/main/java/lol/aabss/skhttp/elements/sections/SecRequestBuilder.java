package lol.aabss.skhttp.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.config.EntryNode;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.util.Kleenean;
import lol.aabss.skhttp.objects.RequestObject;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

@Name("HTTP Request Builder")
@Description("Builds a HTTP request.")
@Examples({
        "http request builder stored in {_var}:",
        "\turl: \"https://www.someurl.com\"",
        "\tmethod: \"GET\"",
        "",
        "http request builder stored in {_request}:",
        "\turl: \"https://www.someurl.com\"",
        "\tmethod: \"GET\"",
        "\tbody: \"some body text\"",
        "\theaders:",
        "\t\tsomekey: somevalue",
        "\t\tContent-Type: application/json"
})
@Since("1.0")
public class SecRequestBuilder extends Section {

    private static final EntryValidator.EntryValidatorBuilder ENTRY_VALIDATOR = EntryValidator.builder();
    private Expression<String> url;
    private Expression<String> method;
    private Expression<Object> body;
    private HashMap<String, String> headers = new HashMap<>();
    private Variable<?> var;

    static {
        Skript.registerSection(SecRequestBuilder.class,
                "http request [builder] stored in %object%"
        );
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("url", null, false, String.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("method", null, false, String.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("body", null, true, Object.class));
        ENTRY_VALIDATOR.addSection("headers", true);
        // (create|make) [a] [new] http request using [url] %string% and [with] [method] %string% [and [with] [header[s]] %-strings%] [and [body] %-string%] (then|and) store it in %object%
        // (create|make) [a] [new] http request using [url] %string% and [with] [method] %string% [and [body] %-string%] [and [with] [header[s]] %-strings%] (then|and) store it in %object%
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult, @NotNull SectionNode sectionNode, @NotNull List<TriggerItem> triggerItems) {
        EntryContainer container = ENTRY_VALIDATOR.build().validate(sectionNode);
        if (container == null) return false;
        this.url = (Expression<String>) container.getOptional("url", false);
        if (this.url == null) return false;
        this.method = (Expression<String>) container.getOptional("method", false);
        if (this.method == null) return false;
        this.body = (Expression<Object>) container.getOptional("body", false);
        container.getSource().convertToEntries(-1, ":");
        loadHeaders(container.getSource());
        if (exprs[0] instanceof Variable<?>){
            this.var = (Variable<?>) exprs[0];
            return true;
        } else {
            Skript.error("The object expression must be a variable.");
            return false;
        }
    }

    private void loadHeaders(SectionNode sectionNode) {
        for (Node node : sectionNode) {
            if (node instanceof SectionNode) {
                ((SectionNode) node).convertToEntries(-1);
                for (Node node1 : ((SectionNode) node)){
                    if (node1 instanceof EntryNode){
                        headers.put(node1.getKey(), ((EntryNode) node1).getValue());
                    } else {
                        Skript.error("Invalid line in headers");
                    }
                }
            }
        }
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
        HttpRequest.BodyPublisher publisher;
        String type = null;
        Path pathRequest = null;
        if (body == null) {
            publisher = HttpRequest.BodyPublishers.noBody();
            request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .method(method, publisher);
        } else {
            Object body = this.body.getSingle(e);
            if (body != null) {
                request = HttpRequest.newBuilder()
                        .uri(URI.create(uri));
                if (body instanceof String string) {
                    type = "string";
                    publisher = HttpRequest.BodyPublishers.ofString(string);
                } else if (body instanceof byte[] bytes) {
                    type = "bytes";
                    publisher = HttpRequest.BodyPublishers.ofByteArray(bytes);
                } else if (body instanceof File file) {
                    try {
                        type = "file";
                        pathRequest = file.toPath();
                        publisher = HttpRequest.BodyPublishers.ofFile(pathRequest);
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (body instanceof Path path) {
                    try {
                        type = "path";
                        pathRequest = path;
                        publisher = HttpRequest.BodyPublishers.ofFile(pathRequest);
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (body instanceof InputStream stream) {
                    type = "inputStream";
                    publisher = HttpRequest.BodyPublishers.ofInputStream(() -> stream);
                } else if (body instanceof Supplier<?> supplier) {
                    if (supplier.get() instanceof InputStream) {
                        type = "inputStreamSupplier";
                        publisher = HttpRequest.BodyPublishers.ofInputStream((Supplier<? extends InputStream>) supplier);
                    } else {
                        type = "none";
                        publisher = HttpRequest.BodyPublishers.noBody();
                    }
                } else {
                    type = "object";
                    publisher = HttpRequest.BodyPublishers.ofString(String.valueOf(body));
                }
                request = request.method(method, publisher);
            } else {
                return;
            }
        }
        HttpRequest http;
        if (headers != null) {
            for (String key : headers.keySet()) {
                request = request.header(key, headers.get(key));
            }
        }
        http = request.build();
        var.change(e, new RequestObject(http, type, pathRequest).array(), Changer.ChangeMode.SET);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "http request builder";
    }
}
