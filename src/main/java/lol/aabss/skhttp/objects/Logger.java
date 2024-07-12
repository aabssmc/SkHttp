package lol.aabss.skhttp.objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Logger{

    public void success(Object message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD +"[SkHttp] "
                + ChatColor.translateAlternateColorCodes('&', "&r&a"+ message));
    }

    public void log(Object message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD +"[SkHttp] "
                + ChatColor.translateAlternateColorCodes('&', "&r&f"+ message));
    }

    public void warn(Object message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD +"[SkHttp] "
                + ChatColor.translateAlternateColorCodes('&', "&r&e"+ message));
    }

    public void error(Object message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD +"[SkHttp] "
                + ChatColor.translateAlternateColorCodes('&', "&r&c"+ message));
    }
}
