package me.mortezapourramzan.mcplugin.Commands;

import me.mortezapourramzan.mcplugin.Games;
import me.mortezapourramzan.mcplugin.PlaceNum;
import me.mortezapourramzan.mcplugin.TicTacToe;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Put implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            if (command.getName().equals("put")) {
                Player Sender = (Player) sender;

                if (Games.getTheGame((Sender)) != null) {
                    try {
                        int pos = Integer.parseInt(args[0]);
                        if (pos > 9 || pos < 1) {
                            throw new PlaceNum();
                        }
                        TicTacToe ticTacToe = Games.getTheGame(Sender);
                        ticTacToe.put(Sender, pos, ticTacToe.getTurn());
                    } catch (NumberFormatException | PlaceNum | ArrayIndexOutOfBoundsException e) {
                        sender.sendMessage( ChatColor.DARK_RED + "You Must Choose Block's Number (1-9)");
                    }
                } else {
                    sender.sendMessage( ChatColor.DARK_RED + "You Are Not In A Game!");
                }
            }
        }
        return true;
    }
}
