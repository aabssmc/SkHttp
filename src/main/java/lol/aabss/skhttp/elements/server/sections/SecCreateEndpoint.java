package lol.aabss.skhttp.elements.server.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import lol.aabss.skhttp.SkHttp;
import lol.aabss.skhttp.objects.server.HttpExchange;
import lol.aabss.skhttp.objects.server.HttpServer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@Name("Create Endpoint")
@Description("Creates a new endpoint.")
@Examples({
        "set {server} to new http server",
        "make endpoint using {server}:",
        "\tmethod: \"GET\"",
        "\tpath: \"getTest\"",
        "\ttrigger:",
        "\t\tadd 1 to {amount}",
        "\t\trespond with code 200 and message \"{\"\"amount\"\": %{amount}%}\"",
        "start http {server}"
})
@Since("1.3")
public class SecCreateEndpoint extends Section {

    private static final EntryValidator.EntryValidatorBuilder ENTRY_VALIDATOR = EntryValidator.builder();
    private Expression<String> method;
    private Expression<String> path;
    private Expression<HttpServer> server;
    @Nullable
    private Trigger trigger;

    public static class CreateEndpointEvent extends Event {
        private final HttpExchange exchange;

        public CreateEndpointEvent(HttpExchange exchange) {
            this.exchange = exchange;
        }

        public HttpExchange getExchange() {
            return exchange;
        }

        @Override
        public @NotNull HandlerList getHandlers() {
            throw new IllegalStateException();
        }
    }

    static {
        Skript.registerSection(SecCreateEndpoint.class,
                "(create|make) [a] [new] (endpoint|context) using %httpserver%"
        );
        EventValues.registerEventValue(CreateEndpointEvent.class, HttpExchange.class, new Getter<>() {
            @Override
            public HttpExchange get(CreateEndpointEvent event) {
                return event.getExchange();
            }
        }, EventValues.TIME_NOW);
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("method", null, false, String.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("path", null, false, String.class));
        ENTRY_VALIDATOR.addSection("trigger", false);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult, @NotNull SectionNode sectionNode, @NotNull List<TriggerItem> triggerItems) {
        EntryContainer ENTRY_CONTAINER = ENTRY_VALIDATOR.build().validate(sectionNode);
        server = (Expression<HttpServer>) exprs[0];
        AtomicBoolean delayed = new AtomicBoolean(false);
        Runnable afterLoading = () -> delayed.set(!getParser().getHasDelayBefore().isFalse());
        trigger = loadCode(getTriggerNode(sectionNode), "create endpoint", afterLoading, CreateEndpointEvent.class);
        if (delayed.get()) {
            Skript.error("Delays can't be used within a Create Endpoint Section");
            return false;
        }
        if (ENTRY_CONTAINER == null) return false;
        this.method = (Expression<String>) ENTRY_CONTAINER.getOptional("method", false);
        if (this.method == null) return false;
        this.path = (Expression<String>) ENTRY_CONTAINER.getOptional("path", false);
        return this.path != null;
    }

    public SectionNode getTriggerNode(SectionNode sectionNode){
        for (Node node : sectionNode) {
            if (node instanceof SectionNode nodeSection) {
                return nodeSection;
            }
        }
        return null;
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event event) {
        Consumer<HttpExchange> consumer;
        if (trigger != null) {
            consumer = o -> {
                SkHttp.LAST_EXCHANGE = o;
                CreateEndpointEvent endpoint = new CreateEndpointEvent(o);
                Variables.setLocalVariables(endpoint, Variables.copyLocalVariables(event));
                TriggerItem.walk(trigger, endpoint);
                Variables.setLocalVariables(event, Variables.copyLocalVariables(endpoint));
                Variables.removeLocals(endpoint);
            };
        } else {
            consumer = null;
        }
        execute(event, consumer);
        return super.walk(event, false);
    }

    public void execute(Event event, Consumer<HttpExchange> consumer) {
        HttpServer server = this.server.getSingle(event);
        if (server == null){
            return;
        }
        String method = this.method.getSingle(event);
        if (method == null){
            return;
        }
        String path = this.path.getSingle(event);
        if (path == null){
            return;
        }
        SkHttp.LAST_CONTEXT = server.createEndpoint(method, path, consumer);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "";
    }
}
