package lol.aabss.skhttp.objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Logger{

    public void success(String message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD +"[SkHttp] " + ChatColor.RESET + ChatColor.GREEN + message);
    }

    public void log(String message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD +"[SkHttp] " + ChatColor.RESET + ChatColor.WHITE + message);
    }

    public void warn(String message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD +"[SkHttp] " + ChatColor.RESET + ChatColor.YELLOW + message);
    }

    public void error(String message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD +"[SkHttp] " + ChatColor.RESET + ChatColor.RED + message);
    }
}
