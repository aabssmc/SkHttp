package lol.aabss.skhttp.elements.http.sections;

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
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.Timespan;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import lol.aabss.skhttp.objects.RequestObject;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static lol.aabss.skhttp.SkHttp.SKRIPT_REFLECT_SUPPORTED;

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
        "\tbody: \"some body\"",
        "\ttimeout: 30 seconds",
        "\theaders:",
        "\t\tsomekey: somevalue",
        "\t\tContent-Type: application/json"
})
@Since("1.0")
public class SecRequestBuilder extends Section {

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{(.*?)}");

    private static final EntryValidator.EntryValidatorBuilder ENTRY_VALIDATOR = EntryValidator.builder();
    private static EntryContainer ENTRY_CONTAINER;
    private Expression<String> url;
    private Expression<String> method;
    private Expression<Object> body;
    private Expression<Timespan> timeout;
    private final HashMap<String, String> headers = new HashMap<>();
    private Variable<?> var;

    static {
        Skript.registerSection(SecRequestBuilder.class,
                "http request [builder] stored in %object%"
        );
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("url", null, false, String.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("method", null, false, String.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("body", null, true, Object.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("timeout", null, true, Timespan.class));
        ENTRY_VALIDATOR.addSection("headers", true);
        // (create|make) [a] [new] http request using [url] %string% and [with] [method] %string% [and [with] [header[s]] %-strings%] [and [body] %-string%] (then|and) store it in %object%
        // (create|make) [a] [new] http request using [url] %string% and [with] [method] %string% [and [body] %-string%] [and [with] [header[s]] %-strings%] (then|and) store it in %object%
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult, @NotNull SectionNode sectionNode, @NotNull List<TriggerItem> triggerItems) {
        ENTRY_CONTAINER = ENTRY_VALIDATOR.build().validate(sectionNode);
        if (ENTRY_CONTAINER == null) return false;
        this.url = (Expression<String>) ENTRY_CONTAINER.getOptional("url", false);
        if (this.url == null) return false;
        this.method = (Expression<String>) ENTRY_CONTAINER.getOptional("method", false);
        if (this.method == null) return false;
        this.body = (Expression<Object>) ENTRY_CONTAINER.getOptional("body", false);
        this.timeout = (Expression<Timespan>) ENTRY_CONTAINER.getOptional("timeout", false);

        if (exprs[0] instanceof Variable<?>){
            this.var = (Variable<?>) exprs[0];
            return true;
        } else {
            Skript.error("The object expression must be a variable.");
            return false;
        }
    }

    public void loadHeaders(SectionNode sectionNode, Event event) {
        for (Node node : sectionNode) {
            if (node instanceof SectionNode) {
                ((SectionNode) node).convertToEntries(-1);
                for (Node node1 : (SectionNode) node) {
                    if (node1 instanceof EntryNode) {
                        String key = replaceVariables(node1.getKey(), event);
                        String value = replaceVariables(((EntryNode) node1).getValue(), event);
                        headers.put(key, value);
                    } else {
                        Skript.error("Invalid line in headers");
                    }
                }
            }
        }
    }

    private String replaceVariables(String input, Event event) {
        Matcher matcher = VARIABLE_PATTERN.matcher(input);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String variableName = matcher.group(1);
            boolean isLocal = variableName.contains("_");
            Object variableValue = Variables.getVariable(variableName, event, isLocal);
            String replacement = Classes.toString(variableValue);
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);
        return result.toString();
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event e) {
        ENTRY_CONTAINER.getSource().convertToEntries(-1, ":");
        loadHeaders(ENTRY_CONTAINER.getSource(), e);
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
        Timespan timeout = null;
        if (this.timeout != null) {
            timeout = this.timeout.getSingle(e);
            if (timeout == null) {
                return;
            }
        }
        Variable<?> var = this.var;
        if (var == null) {
            return;
        }
        HttpRequest.Builder request;
        HttpRequest.BodyPublisher publisher;
        Path pathRequest = null;
        // body ------
        if (body == null) {
            publisher = HttpRequest.BodyPublishers.noBody();
            request = HttpRequest.newBuilder()
                    .uri(URI.create(uri));
            switch (method.toUpperCase()){
                case "POST": request.POST(publisher);
                case "GET": request.GET();
                case "PUT": request.PUT(publisher);
                case "DELETE": request.DELETE();
                default: request.method(method, publisher);
            }
        } else {
            Object body = this.body.getSingle(e);
            if (body != null) {
                request = HttpRequest.newBuilder()
                        .uri(URI.create(uri));
                if (SKRIPT_REFLECT_SUPPORTED && body instanceof com.btk5h.skriptmirror.ObjectWrapper){
                    body = ((com.btk5h.skriptmirror.ObjectWrapper) body).get();
                }
                if (body instanceof File || body instanceof Path) {
                    try {
                        Path path;
                        if (body instanceof File file) {
                            path = file.toPath();
                        } else {
                            path = (Path) body;
                        }
                        String boundary = UUID.randomUUID().toString();
                        byte[] fileContent = Files.readAllBytes(path);
                        String bodyBuilder = "--" + boundary + "\r\n" +
                                "Content-Disposition: form-data; name=\"file\"; filename=\"" + path.getFileName() + "\"\r\n" +
                                "Content-Type: application/octet-stream\r\n\r\n";
                        byte[] bodyStart = bodyBuilder.getBytes();
                        byte[] boundaryEnd = ("\r\n--" + boundary + "--\r\n").getBytes();
                        byte[] bodyString = new byte[bodyStart.length + fileContent.length + boundaryEnd.length];
                        System.arraycopy(bodyStart, 0, bodyString, 0, bodyStart.length);
                        System.arraycopy(fileContent, 0, bodyString, bodyStart.length, fileContent.length);
                        System.arraycopy(boundaryEnd, 0, bodyString, bodyStart.length + fileContent.length, boundaryEnd.length);
                        request = request.header("Content-Type", "multipart/form-data; boundary=" + boundary);
                        pathRequest = path;
                        publisher = HttpRequest.BodyPublishers.ofByteArray(bodyString);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    publisher = HttpRequest.BodyPublishers.ofString(String.valueOf(body));
                }
                request.method(method.toUpperCase(), publisher);
            } else {
                return;
            }
        }
        // -----------
        // --- headers -----
        HttpRequest http;
        if (headers != null) {
            for (String key : headers.keySet()) {
                request = request.header(key, headers.get(key));
            }
        }
        if (timeout != null) {
            request = request.timeout(Duration.ofMillis(timeout.getMilliSeconds()));
        } else {
            request = request.timeout(Duration.ofMinutes(1));
        }
        // -----------
        http = request.build();
        var.change(e, new RequestObject(http, pathRequest).array(), Changer.ChangeMode.SET);
    }


    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "http request builder";
    }
}
