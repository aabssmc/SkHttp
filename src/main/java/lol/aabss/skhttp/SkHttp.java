package lol.aabss.skhttp;

import ch.njol.skript.Skript;
import lol.aabss.skhttp.objects.Logger;
import lol.aabss.skhttp.objects.server.HttpContext;
import lol.aabss.skhttp.objects.server.HttpExchange;
import lol.aabss.skhttp.objects.server.HttpServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.http.HttpResponse;

public final class SkHttp extends JavaPlugin {

    public static HttpResponse<?> LAST_RESPONSE;
    public static HttpServer LAST_SERVER;
    public static HttpContext LAST_CONTEXT;
    public static HttpExchange LAST_EXCHANGE;
    public static final Logger LOGGER = new Logger();
    public static SkHttp instance;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("Skript") != null) {
            Metrics metrics = new Metrics(this, 21279);
            try {
                Skript.registerAddon(this)
                        .loadClasses("lol.aabss.skhttp", "elements");
                metrics.addCustomChart(new Metrics.SimplePie("skript_version", () -> Skript.getVersion().toString()));
                instance = this;
                LOGGER.success("SkHttp loaded.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else{
            LOGGER.error("Skript not found! Please add Skript.");
            getPluginLoader().disablePlugin(this);
        }
    }

}
