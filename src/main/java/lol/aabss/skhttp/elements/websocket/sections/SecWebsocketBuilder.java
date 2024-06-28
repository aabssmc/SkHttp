package lol.aabss.skhttp.elements.websocket.sections;

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
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import lol.aabss.skhttp.objects.websocket.WebsocketBukkitListener;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Name("Send Websocket Builder")
@Description({
        "Creates a new websocket.",
        "# Note:",
        "Use WebsocketOpenEvent in order to do actions when opened."
})
@Examples({
        "make websocket with url \"wss://something.com/websocket\" and store it in {_websocket}",
        "\theaders:",
        "\t\tkey: value",
        "\t\tanotherkey: anothervalue",
        "send websocket with url \"ws://example.com\" and store it in {_example}"
})
@Since("1.3")
public class SecWebsocketBuilder extends EffectSection {

    private static final EntryValidator.EntryValidatorBuilder ENTRY_VALIDATOR = EntryValidator.builder();
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{(.*?)}");
    private static EntryContainer ENTRY_CONTAINER;
    private final HashMap<String, String> headers = new HashMap<>();
    private Expression<String> url;
    private Variable<?> var;

    static {
        Skript.registerSection(SecWebsocketBuilder.class,
                "(register|make) [a] [new] websocket with [ur(l|i)] %string% and store it in %object%"
        );
        ENTRY_VALIDATOR.addSection("headers", true);
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
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult, @Nullable SectionNode sectionNode, @Nullable List<TriggerItem> triggerItems) {
        if (sectionNode != null) {
            ENTRY_CONTAINER = ENTRY_VALIDATOR.build().validate(sectionNode);
            if (ENTRY_CONTAINER == null) return false;
        }
        this.url = (Expression<String>) exprs[0];
        if (this.url == null) return false;
        if (exprs[1] instanceof Variable<?>){
            this.var = (Variable<?>) exprs[1];
            return true;
        } else {
            Skript.error("The object expression must be a variable.");
            return false;
        }
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event e) {
        ENTRY_CONTAINER.getSource().convertToEntries(-1, ":");
        loadHeaders(ENTRY_CONTAINER.getSource(), e);
        execute(e);
        return super.walk(e, false);
    }

    public void execute(Event e){
        String url = this.url.getSingle(e);
        if (this.url == null){
            return;
        }
        if (url == null){
            return;
        }
        Variable<?> var = this.var;
        if (this.var == null){
            return;
        }
        WebSocket.Builder builder = HttpClient.newHttpClient().newWebSocketBuilder();
        if (headers != null) {
            for (String key : headers.keySet()) {
                builder = builder.header(key, headers.get(key));
            }
        }
        builder.buildAsync(URI.create(url), new WebsocketBukkitListener()).whenCompleteAsync((webSocket, throwable) -> {
            var.change(e, new Object[]{webSocket}, Changer.ChangeMode.SET);
        });
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "websocket builder";
    }
}
