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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.List;

public final class SkHttp extends JavaPlugin {

    public static HttpResponse<?> LAST_RESPONSE;
    public static HttpServer LAST_SERVER;
    public static HttpContext LAST_CONTEXT;
    public static HttpExchange LAST_EXCHANGE;
    public static WebSocket LAST_WEBSOCKET;
    public static final Logger LOGGER = new Logger();
    public static SkHttp instance;
    public static final boolean SKRIPT_REFLECT_SUPPORTED = Skript.classExists("com.btk5h.skriptmirror.ObjectWrapper");
    public File WEBSITE_FOLDER = new File(this.getDataFolder(), "sites");

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
                if (WEBSITE_FOLDER.mkdirs()) {
                    generateExampleSite();
                }
                LOGGER.success("SkHttp loaded.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else{
            LOGGER.error("Skript not found! Please add Skript.");
            getPluginLoader().disablePlugin(this);
        }
    }

    public void generateExampleSite() throws IOException {
        File example = new File(WEBSITE_FOLDER, "skhttp-example");
        if (example.mkdirs()) {
            File index = new File(example, "index.html");
            if (index.createNewFile()) {
                FileWriter fw = new FileWriter(index);
                fw.write("""
                        <!DOCTYPE html>
                        	<head>
                        		<link rel="stylesheet" href="style.css">
                        	</head>
                        	<body>
                        		<header><h1>SkHttp Example Site</h1></header>
                        	</body>
                        </html>
                        """);
                fw.close();
            }
            File css = new File(example, "style.css");
            if (css.createNewFile()){
                FileWriter fw = new FileWriter(css);
                fw.write("""
                        body {
                        	font-family: Arial, sans-serif;
                        	margin: 0;
                        	padding: 0;
                        }
                        header {
                        	background-color: #333;
                        	color: #fff;
                        	text-align: center;
                        	padding: 10px;
                        }
                        """);
                fw.close();
            }
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
