
package com.dinnerbone.bukkit.home;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.dinnerbone.bukkit.home.commands.DelHomeCommand;
import com.dinnerbone.bukkit.home.commands.GoHomeCommand;
import com.dinnerbone.bukkit.home.commands.ListHomesCommand;
import com.dinnerbone.bukkit.home.commands.SetHomeCommand;

public class HomeBukkit extends JavaPlugin {
    public void onDisable() {
    }

    public void onEnable() {
        PluginDescriptionFile desc = getDescription();

        System.out.println(desc.getFullName() + " has been enabled");

        getCommand("listhomes").setExecutor(new ListHomesCommand(this));
        getCommand("sethome").setExecutor(new SetHomeCommand(this));
        getCommand("gohome").setExecutor(new GoHomeCommand(this));
        getCommand("delhome").setExecutor(new DelHomeCommand(this));

        setupDatabase();
    }

    private void setupDatabase() {
        try {
        	this.getLogger().info("Home: Starting DB ...");
            getDatabase().find(Home.class).findRowCount();
        	this.getLogger().info(".... done");
        } catch (PersistenceException ex) {
            System.out.println("Installing database for " + getDescription().getName() + " due to first time usage");
            installDDL();
        }
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        this.getLogger().info("Home: retrieving DB ...");
        list.add(Home.class);
        this.getLogger().info("Home: ... done");
        return list;
    }

    public static boolean anonymousCheck(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cannot execute that command, I don't know who you are!");
            return true;
        } else {
            return false;
        }
    }

    public static Player getPlayer(CommandSender sender, String[] args, int index) {
        if (args.length > index) {
            // List<Player> players = sender.getServer().matchPlayer(args[index]);
        	return null;
        	
        } else {
            if (anonymousCheck(sender)) {
                return null;
            } else {
                return (Player)sender;
            }
        }
    }
    
    
}
