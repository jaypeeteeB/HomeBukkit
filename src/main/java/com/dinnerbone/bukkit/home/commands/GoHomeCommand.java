
package com.dinnerbone.bukkit.home.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.dinnerbone.bukkit.home.Home;
import com.dinnerbone.bukkit.home.HomeBukkit;

public class GoHomeCommand implements CommandExecutor {
    private final HomeBukkit plugin;

    public GoHomeCommand(HomeBukkit plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                
        String playerName;
        
        if (args.length < 1 ) {
        	return false;
        }
        if (args.length > 1 ) {
        	playerName = args[1];
        	if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "I don't know how to move you!");
                plugin.getLogger().info("ListHome by not-a-player");
                return true;
            } else if ((playerName != sender.getName()) && (!sender.isOp())) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to go to other players homes");
                plugin.getLogger().info("gohome <player> ("+playerName+") by none-op ("+sender.getName()+")");
                return true;
            }         	
        } else {
        	playerName = sender.getName();
        }
         
        String name = args[0];

        Home home = plugin.getDatabase().find(Home.class).where().ieq("name", name).ieq("playerName", playerName).findUnique();

        if (home == null) {
            sender.sendMessage(ChatColor.RED + "I don't know where that is!");
            plugin.getLogger().info("gohome to unknown location: "+name);
        } else {
            ((Player)sender).teleport(home.getLocation());
            plugin.getLogger().info("gohome ("+sender.getName()+") -> [" + playerName + "]:[" + name + "]");
        }

        return true;
    }
}
