package lol.aabss.skhttp;

import ch.njol.skript.Skript;
import lol.aabss.skhttp.objects.Logger;
import lol.aabss.skhttp.objects.server.HttpContext;
import lol.aabss.skhttp.objects.server.HttpExchange;
import lol.aabss.skhttp.objects.server.HttpServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

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
            saveDefaultConfig();
            Metrics metrics = new Metrics(this, 21279);
            try {
                getCommand("skhttp").setExecutor(this);
                getCommand("skhttp").setTabCompleter(this);
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

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0){
            sender.sendMessage(ChatColor.RED + "/skhttp reload");
        } else {
            if (args[0].equalsIgnoreCase("reload")){
                saveConfig();
                reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "Reloaded!");
            } else {
                sender.sendMessage(ChatColor.RED + "/skhttp reload");
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            if ("reload".startsWith(args[0].toLowerCase())) completions.add("reload");
            return completions;
        }
        return null;
    }

}
