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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
public class SecSendAsyncHttpRequest extends EffectSection {

    public static class SendAsyncHttpEvent extends Event {
        private final HttpResponse<?> response;

        public SendAsyncHttpEvent(HttpResponse<?> response) {
            this.response = response;
        }

        public HttpResponse<?> getResponse() {
            return response;
        }

        @Override
        public @NotNull HandlerList getHandlers() {
            throw new IllegalStateException();
        }
    }

    static {
        Skript.registerSection(SecSendAsyncHttpRequest.class,
                "(send|post) [[:a]sync[hronous]] [http] request using [[client] %-httpclient% and] [request] %httprequest%",
                "(send|post) [http] request using [[client] %-httpclient% and] [request] %httprequest% [:a]synchronously"
        );
        EventValues.registerEventValue(SendAsyncHttpEvent.class, HttpResponse.class, new Getter<>() {
            @Override
            public HttpResponse<?> get(SendAsyncHttpEvent event) {
                return event.getResponse();
            }
        }, EventValues.TIME_NOW);
    }

    @Nullable
    private Trigger trigger;
    @Nullable
    private Expression<HttpClient> client;
    private Expression<RequestObject> request;
    private boolean async;

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult, @Nullable SectionNode sectionNode, @Nullable List<TriggerItem> triggerItems) {
        AtomicBoolean delayed = new AtomicBoolean(false);
        Runnable afterLoading = () -> delayed.set(!getParser().getHasDelayBefore().isFalse());
        if (sectionNode != null) {
            trigger = loadCode(sectionNode, "send async request", afterLoading, SendAsyncHttpEvent.class);
            if (delayed.get()) {
                Skript.error("Delays can't be used within a Send Async Http Request Section");
                return false;
            }
        }
        client = (Expression<HttpClient>) exprs[0];
        request = (Expression<RequestObject>) exprs[1];
        async = parseResult.hasTag("a");
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event event) {
        Consumer<HttpResponse<String>> consumer;
        if (trigger != null) {
            consumer = o -> {
                SkHttp.LAST_RESPONSE = o;
                SendAsyncHttpEvent asyncHttp = new SendAsyncHttpEvent(o);
                Variables.setLocalVariables(asyncHttp, Variables.copyLocalVariables(event));
                TriggerItem.walk(trigger, asyncHttp);
                Variables.setLocalVariables(event, Variables.copyLocalVariables(asyncHttp));
                Variables.removeLocals(asyncHttp);
            };
        } else {
            consumer = null;
        }
        execute(event, consumer);
        return super.walk(event, false);
    }

    private void execute(Event event, Consumer<HttpResponse<String>> consumer){
        RequestObject request = this.request.getSingle(event);
        if (request == null){
            return;
        }
        try {
            if (client != null) {
                HttpClient client = this.client.getSingle(event);
                if (client != null) {
                    if (async) {
                        client.sendAsync(request.request, HttpResponse.BodyHandlers.ofString()).whenCompleteAsync(getWhenComplete(consumer));
                    } else {
                        consumer.accept(client.send(request.request, HttpResponse.BodyHandlers.ofString()));
                    }
                } else {
                    if (async) {
                        HttpClient.newHttpClient().sendAsync(request.request, HttpResponse.BodyHandlers.ofString()).whenCompleteAsync(getWhenComplete(consumer));
                    } else {
                        consumer.accept(HttpClient.newHttpClient().send(request.request, HttpResponse.BodyHandlers.ofString()));
                    }
                }
            } else {
                if (async) {
                    HttpClient.newHttpClient().sendAsync(request.request, HttpResponse.BodyHandlers.ofString()).whenCompleteAsync(getWhenComplete(consumer));
                } else {
                    consumer.accept(HttpClient.newHttpClient().send(request.request, HttpResponse.BodyHandlers.ofString()));
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public BiConsumer<HttpResponse<String>, Throwable> getWhenComplete(Consumer<HttpResponse<String>> consumer){
        return (stringHttpResponse, throwable) -> consumer.accept(stringHttpResponse);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "send "+(async ? "a" : "")+ "sync http request";
    }
}
