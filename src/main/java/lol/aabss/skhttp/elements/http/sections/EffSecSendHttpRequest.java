package lol.aabss.skhttp.elements.http.sections;

import ch.njol.skript.Skript;
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
import lol.aabss.skhttp.objects.RequestObject;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;

@Name("Send Http Request")
@Description({
        "Sends an, optionally async, http request.",
        "# Note:",
        "If you are sending an async request, you will have to use the section in order to get the response on time."
})
@Examples({
        "send async request using {_client} and {_request}:",
        "\tbroadcast body of the response",
        "send request using {_request}"
})
@Since("1.0, 1.2 (effect section)")
public class EffSecSendHttpRequest extends EffectSection {

    static {
        Skript.registerSection(EffSecSendHttpRequest.class,
                "(send|post) [[:a]sync[hronous]] [http] request using [[client] %-httpclient% and] [request] %httprequest%",
                "(send|post) [http] request using [[client] %-httpclient% and] [request] %httprequest% [:a]synchronously"
        );
        EventValues.registerEventValue(SendHttpRequestEvent.class, HttpResponse.class, new Getter<>() {
            @Override
            public HttpResponse<?> get(SendHttpRequestEvent event) {
                return event.getResponse();
            }
        }, EventValues.TIME_NOW);
    }

    public static class SendHttpRequestEvent extends Event {
        private static final HandlerList handlers = new HandlerList();
        private final HttpResponse<?> response;

        public SendHttpRequestEvent(HttpResponse<?> response) {
            this.response = response;
        }

        public HttpResponse<?> getResponse() {
            return response;
        }

        @Override
        public @NotNull HandlerList getHandlers() {
            return handlers;
        }

        public static HandlerList getHandlerList() {
            return handlers;
        }
    }

    @Nullable
    private Trigger trigger;
    @Nullable
    private Expression<HttpClient> client;
    private Expression<RequestObject> request;
    private boolean async;

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult, @Nullable SectionNode sectionNode, @Nullable List<TriggerItem> triggerItems) {
        if (hasSection()) {
            assert sectionNode != null;
            trigger = loadCode(sectionNode, "http request event", SendHttpRequestEvent.class);
        }
        client = (Expression<HttpClient>) exprs[0];
        request = (Expression<RequestObject>) exprs[1];
        async = parseResult.hasTag("a");
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event event) {
        RequestObject request = this.request.getSingle(event);
        if (request == null){
            return null;
        }
        try {
            if (client != null) {
                call(event, request, this.client.getSingle(event));
            } else {
                call(event, request, null);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return super.walk(event, false);
    }

    private void call(@NotNull Event event, RequestObject request, HttpClient client) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        if (client != null){
            httpClient = client;
        }
        if (async) {
            httpClient.sendAsync(request.request, HttpResponse.BodyHandlers.ofString())
                    .whenCompleteAsync((stringHttpResponse, throwable) -> {
                        SkHttp.LAST_RESPONSE = stringHttpResponse;
                        if (trigger != null) {
                            Object variables = Variables.copyLocalVariables(event);
                            if (variables != null) {
                                Variables.setLocalVariables(event, variables);
                            }
                            trigger.execute(event);
                        }
                    });
        } else {
            SkHttp.LAST_RESPONSE = httpClient.send(request.request, HttpResponse.BodyHandlers.ofString());
            if (trigger != null) {
                Object variables = Variables.copyLocalVariables(event);
                if (variables != null) {
                    Variables.setLocalVariables(event, variables);
                }
                trigger.execute(event);
            }
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "send "+(async ? "a" : "")+ "sync http request";
    }
}
