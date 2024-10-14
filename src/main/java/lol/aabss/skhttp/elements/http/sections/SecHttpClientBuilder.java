package lol.aabss.skhttp.elements.http.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.skript.util.Timespan;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Name("HTTP Client Builder")
@Description("Builds a HTTP client.")
@Examples({
        "http client builder stored in {_var}:",
        "\ttimeout: 10 minutes",
        "",
        "http client builder stored in {_client}:",
        "\ttimeout: 10 minutes",
        "\tfollow redirects: \"always\" # always, normal, never",
        "\tpriority: 256 # 1 through 256",
        "\tversion: 1 # 1 or 2",
        "\texecutor:",
        "\t\tbroadcast \"this happens multiple times every time a request is sent\""
})
@Since("1.0")
public class SecHttpClientBuilder extends Section {

    private static final EntryValidator.EntryValidatorBuilder ENTRY_VALIDATOR = EntryValidator.builder();
    private Expression<Timespan> timeout;
    private Expression<String> followRedirects;
    private Expression<Integer> priority;
    private Expression<Integer> version;
    private Variable<?> var;
    private Trigger executor;

    static {
        Skript.registerSection(SecHttpClientBuilder.class,
                "http client [builder] stored in %object%"
        );
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("timeout", null, true, Timespan.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("follow redirects", null, true, String.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("priority", null, true, Integer.class));
        ENTRY_VALIDATOR.addEntryData(new ExpressionEntryData<>("version", null, true, Integer.class));
        ENTRY_VALIDATOR.addSection("executor", true);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult, @NotNull SectionNode sectionNode, @NotNull List<TriggerItem> triggerItems) {
        EntryContainer ENTRY_CONTAINER = ENTRY_VALIDATOR.build().validate(sectionNode);
        if (ENTRY_CONTAINER == null) return false;
        this.timeout = (Expression<Timespan>) ENTRY_CONTAINER.getOptional("timeout", false);
        this.followRedirects = (Expression<String>) ENTRY_CONTAINER.getOptional("follow redirects", false);
        this.priority = (Expression<Integer>) ENTRY_CONTAINER.getOptional("priority", false);
        this.version = (Expression<Integer>) ENTRY_CONTAINER.getOptional("version", false);
        AtomicBoolean delayed = new AtomicBoolean(false);
        Runnable afterLoading = () -> delayed.set(!getParser().getHasDelayBefore().isFalse());
        executor = loadCode(getTriggerNode(sectionNode), "http client builder", afterLoading);
        if (delayed.get()) {
            Skript.error("Delays can't be used within a Http Client Section");
            return false;
        }
        if (exprs[0] instanceof Variable<?>){
            this.var = (Variable<?>) exprs[0];
            return true;
        } else {
            Skript.error("The object expression must be a variable.");
            return false;
        }

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
        Runnable runnable;
        if (executor != null) {
            runnable = () -> {
                Variables.setLocalVariables(event, Variables.copyLocalVariables(event));
                executor.execute(event);
            };
        } else {
            runnable = null;
        }
        execute(event, runnable);
        return super.walk(event, false);
    }

    private void execute(Event event, Runnable runnable) {
        HttpClient.Builder builder = HttpClient.newBuilder();
        if (timeout != null) {
            Timespan timeout = this.timeout.getSingle(event);
            if (timeout != null) {
                builder.connectTimeout(Duration.ofMillis(timeout.getMilliSeconds()));
            }
        }
        if (followRedirects != null) {
            String followRedirects = this.followRedirects.getSingle(event);
            if (followRedirects == null) {
                followRedirects = "normal";
            }
            switch (followRedirects) {
                case "always": builder.followRedirects(HttpClient.Redirect.ALWAYS);
                case "never": builder.followRedirects(HttpClient.Redirect.NEVER);
                default: builder.followRedirects(HttpClient.Redirect.NORMAL);
            }
        }
        if (priority != null) {
            Integer priority = this.priority.getSingle(event);
            if (priority != null) {
                builder.priority(priority > 256 ? 256 : (priority < 1 ? 1 : priority));
            }
        }
        if (priority != null) {
            Integer version = this.version.getSingle(event);
            if (version != null) {
                builder.version(version == 1 ? HttpClient.Version.HTTP_1_1 : HttpClient.Version.HTTP_2);
            }
        }
        if (runnable != null) {
            builder.executor((command) -> {
                command.run();
                runnable.run();
            });
        }
        var.change(event, new Object[]{builder.build()}, Changer.ChangeMode.SET);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "http client builder";
    }
}
