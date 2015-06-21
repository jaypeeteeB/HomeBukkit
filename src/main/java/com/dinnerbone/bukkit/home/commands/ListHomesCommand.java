
package com.dinnerbone.bukkit.home.commands;

import com.dinnerbone.bukkit.home.Home;
import com.dinnerbone.bukkit.home.HomeBukkit;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListHomesCommand implements CommandExecutor {
    private final HomeBukkit plugin;

    public ListHomesCommand(HomeBukkit plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	String playerName;
        if (args.length > 0 ) {
        	playerName = args[0];
        	
        	if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "I don't know how to trust you!");
                plugin.getLogger().info("ListHome by not-a-player");
                return true;
            } else if ((playerName != sender.getName()) && (!sender.isOp())) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to see other players homes");
                plugin.getLogger().info("listhome <player> ("+playerName+") by none-op ("+sender.getName()+")");
                return true;
            }         	
        } else {
        	playerName = sender.getName();
        }
                
        List<Home> homes = plugin.getDatabase().find(Home.class).where().ieq("playerName", playerName).findList();

        if (homes.isEmpty()) {
            if (sender.getName() == playerName) {
                sender.sendMessage("You have no homes!");
            } else {
                sender.sendMessage("That player has no homes!");
            }
            plugin.getLogger().info("listhome <player> ("+playerName+") has no homes");
        } else {
            String result = "";

            for (Home home : homes) {
                if (result.length() > 0) {
                    result += ", ";
                }

                result += home.getName();
            }

            sender.sendMessage("All your homes are: " + result);
        }

        return true;
    }
}
