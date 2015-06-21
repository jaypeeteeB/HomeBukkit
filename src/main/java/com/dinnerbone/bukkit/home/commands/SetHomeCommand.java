
package com.dinnerbone.bukkit.home.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.dinnerbone.bukkit.home.Home;
import com.dinnerbone.bukkit.home.HomeBukkit;

public class SetHomeCommand implements CommandExecutor {
    private final HomeBukkit plugin;

    public SetHomeCommand(HomeBukkit plugin) {
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
                sender.sendMessage(ChatColor.RED + "I don't know who you are!");
                plugin.getLogger().info("SetHome by not instance of player");
                return true;
            } else if ((playerName != sender.getName()) && (!sender.isOp())) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to set other players homes");
                plugin.getLogger().info("SetHome <player> ("+playerName+") by none-op ("+sender.getName()+")");
                return true;
            }         	
        } else {
        	playerName = sender.getName();
        }
        
        String name = args[0];

        Home home = plugin.getDatabase().find(Home.class).where().ieq("name", name).ieq("playerName", playerName).findUnique();

        if (home == null) {
        	plugin.getLogger().info("SetHome - no old home named "+name);
            home = new Home();
            
            if (playerName != sender.getName()) {
            	Home homePlayer = plugin.getDatabase().find(Home.class).where().ieq("playerName", playerName).findUnique();
            	if (homePlayer == null) {
            		// home.setPlayerName(playerName);
            		sender.sendMessage(ChatColor.RED + "I dont know how to handle a PlayerName ["+playerName+"]");
            		plugin.getLogger().info("SetHome did not find old home");
            		return true;
            	} else {
            		home.setPlayer( homePlayer.getPlayer() );
            		plugin.getLogger().info("SetHome found old home for "+homePlayer.getName());
            	}
            } else {
            	home.setPlayer((Player) sender);
            }
            home.setName(name);
            plugin.getLogger().info("SetHome new Home for "+home.getPlayerName()+" called "+home.getName()+" with uuid "+home.getUniqueId());
        }

        home.setLocation(((Player)sender).getLocation());
        plugin.getLogger().info("SetHome called "+home.getName()+" with Loc "+home.getX()+":"+home.getY()+":"+home.getZ()+"!");

        sender.sendMessage(ChatColor.GREEN + "Setting new home '"+home.getName()+"' with at "+(int) home.getX()+":"+(int) home.getY()+":"+ (int) home.getZ()+"!");

        plugin.getDatabase().save(home);

        return true;
    }
}
