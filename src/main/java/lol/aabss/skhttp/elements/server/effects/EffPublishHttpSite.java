package lol.aabss.skhttp.elements.server.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import lol.aabss.skhttp.objects.server.HttpServer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EffPublishHttpSite extends Effect {

    static {
        Skript.registerEffect(EffPublishHttpSite.class,
                "(publish|make|create) [a] [new] [web]site using %httpserver% " +
                        "(from|at) [path] %string% [and] (from|with) [folder|file[s]] %string%"
        );
    }

    private Expression<HttpServer> server;
    private Expression<String> webPath;
    private Expression<String> webFiles;

    @Override
    protected void execute(@NotNull Event e) {
        HttpServer server = this.server.getSingle(e);
        String webPath = this.webPath.getSingle(e);
        String webFiles = this.webFiles.getSingle(e);
        if (server == null || webFiles == null || webPath == null){
            return;
        }
        server.createSite(webPath, webFiles);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "publish http site";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        server = (Expression<HttpServer>) exprs[0];
        webPath = (Expression<String>) exprs[1];
        webFiles = (Expression<String>) exprs[2];
        return true;
    }
}
