package me.mortezapourramzan.mcplugin.Commands;

import me.mortezapourramzan.mcplugin.Database.Database;
import me.mortezapourramzan.mcplugin.Games;
import me.mortezapourramzan.mcplugin.TicTacToe;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class SaveGame implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            if (command.getName().equals("save")) {

                Player Sender = (Player) sender;

                if ((Games.getTheGame(Sender)) != null && (Games.getTheGame(Sender)).isInGame() ) {

                    TicTacToe ticTacToe = Games.getTheGame((Sender));
                    try {
                        Database.save(ticTacToe);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    sender.sendMessage(ChatColor.DARK_RED + "You Are Not In The Game!");
                }
            }
        }
        return true;
    }
}
