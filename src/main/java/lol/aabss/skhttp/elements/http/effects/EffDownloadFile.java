package lol.aabss.skhttp.elements.http.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import lol.aabss.skhttp.objects.RequestObject;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.http.HttpResponse;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.CompletableFuture;

@Name("Download file from URL")
@Description({
        "Downloads all data from a website and will be stored as a file with any given extension",
        "Will be stored in the data folder."
})
@Examples({
        "download file from \"https://i.imgur.com/h8iRx75.png\" to \"image.png\""
})
@Since("1.2")
public class EffDownloadFile extends Effect {

    static {
        Skript.registerEffect(EffDownloadFile.class,
                "download file from %httpresponse/httprequest/string% to [file] %string%"
        );
    }

    private Expression<Object> url;
    private Expression<String> path;

    @Override
    protected void execute(Event e) {
        if (path == null || url == null){
            return;
        }
        String path = this.path.getSingle(e);
        Object urlString = this.url.getSingle(e);
        if (path != null && urlString != null) {
            CompletableFuture.runAsync(() -> {
                URL url;
                InputStream in;
                try {
                    if (urlString instanceof HttpResponse<?>) {
                        url = ((HttpResponse<?>) urlString).uri().toURL();
                    } else if (urlString instanceof RequestObject) {
                        url = ((RequestObject) urlString).request.uri().toURL();
                    } else if (urlString instanceof String) {
                        url = new URL((String) urlString);
                    } else {
                        return;
                    }
                    ReadableByteChannel rbc = Channels.newChannel(url.openStream());
                    FileOutputStream fos = new FileOutputStream(path);
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "download file from url";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        url = (Expression<Object>) exprs[0];
        path = (Expression<String>) exprs[1];
        return true;
    }
}
