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
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

import static lol.aabss.skhttp.SkHttp.LAST_RESPONSE;

@Name("Send Http Request")
@Description("Sends a optionally async request.")
@Examples({
        "send async request using {_client} and {_request} and store the response in {_response}",
        "send request using {_client} and {_request} and store the response in {_response}"
})
@Since("1.0")
public class EffSendHttpRequest extends Effect {

    static {
        Skript.registerEffect(EffSendHttpRequest.class,
                "send [[:a]sync] [http] request using [client] %httpclient% and [request] %httprequest% " +
                        "(and|then) store (the response|it) in %object%"
        );
    }

    private Expression<HttpClient> client;
    private Expression<HttpRequest> request;
    private Variable<?> var;
    private boolean async;


    @Override
    protected void execute(@NotNull Event e) {
        try {
            Variable<?> var = this.var;
            if (var != null) {
                HttpClient client = this.client.getSingle(e);
                if (client != null) {
                    HttpRequest request = this.request.getSingle(e);
                    if (request != null) {
                        HttpResponse<?> response;
                        if (async) {
                            response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).get();
                        } else {
                            response = client.send(request, HttpResponse.BodyHandlers.ofString());
                        }
                        var.change(e,
                                new HttpResponse[]{response},
                                Changer.ChangeMode.SET
                        );
                        LAST_RESPONSE = response;
                    }
                }
            }
        } catch (IOException | InterruptedException | ExecutionException ex) {
            throw new RuntimeException(ex);
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
        request = (Expression<HttpRequest>) exprs[1];
        var = (Variable<?>) exprs[2];
        return true;
    }
}
