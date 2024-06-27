package lol.aabss.skhttp.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import lol.aabss.skhttp.objects.RequestObject;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static lol.aabss.skhttp.SkHttp.LAST_RESPONSE;

@Name("Send Http Request")
@Description("Sends a optionally async request.")
@Examples({
        "send async request using {_client} and {_request} and store the response in {_response}",
        "send request {_request} and store the response in {_response}"
})
@Since("1.0")
public class EffSendHttpRequest extends Effect {

    static {
        Skript.registerEffect(EffSendHttpRequest.class,
                "(send|post) [http] request [using [client] %-httpclient% and] [request] %httprequest% " +
                        "(and|then) store (the response|it) in %object%"
        );
    }

    private Expression<HttpClient> client;
    private Expression<RequestObject> request;
    private Variable<?> var;
    private boolean async;


    @Override
    protected void execute(@NotNull Event e) {
        Variable<?> var = this.var;
        if (var != null) {
            HttpClient client;
            if (this.client == null){
                client = HttpClient.newHttpClient();
            } else {
                client = this.client.getSingle(e);
            }
            if (client != null) {
                RequestObject requestObject = this.request.getSingle(e);
                if (requestObject != null) {
                    HttpRequest request = requestObject.request;
                    if (async) {
                        client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).whenCompleteAsync((httpResponse, throwable) -> {
                            var.change(e,
                                    new HttpResponse[]{httpResponse},
                                    Changer.ChangeMode.SET
                            );
                            LAST_RESPONSE = httpResponse;
                        });
                    } else {
                        try {
                            HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                            var.change(e,
                                    new HttpResponse[]{response},
                                    Changer.ChangeMode.SET
                            );
                            LAST_RESPONSE = response;
                        } catch (IOException | InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "send " + (async ? "async" : "") + " request.";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        async = parseResult.hasTag("a");
        client = (Expression<HttpClient>) exprs[0];
        request = (Expression<RequestObject>) exprs[1];
        var = (Variable<?>) exprs[2];
        return true;
    }
}
