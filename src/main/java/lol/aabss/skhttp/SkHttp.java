package lol.aabss.skhttp;

import ch.njol.skript.Skript;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class SkHttp extends JavaPlugin {

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("Skript") != null) {
            Metrics metrics = new Metrics(this, 21279);
            try {
                Skript.registerAddon(this)
                        .loadClasses("lol.aabss.skhttp", "elements");
                metrics.addCustomChart(new Metrics.SimplePie("skript_version", () -> Skript.getVersion().toString()));
                getLogger().info("SkHttp load success");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else{
            getLogger().severe("Skript not found! Please add skript");
            getPluginLoader().disablePlugin(this);
        }
    }
}
