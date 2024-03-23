package me.mortezapourramzan.mcplugin.Commands;

import me.mortezapourramzan.mcplugin.Database.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;


public class LoadGame implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            if (command.getName().equals("load")) {
                try {
                    String name = args[0];
                    try {
                        Database.load(name, (Player) sender);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (NullPointerException exception) {
                    sender.sendMessage(ChatColor.DARK_RED + "You Must Enter Game's Name!");
                }
            }
        }
        return true;
    }
}
