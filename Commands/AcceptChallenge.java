package me.mortezapourramzan.mcplugin.Commands;

import me.mortezapourramzan.mcplugin.ChallengeRequests;
import me.mortezapourramzan.mcplugin.Games;
import me.mortezapourramzan.mcplugin.TicTacToe;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AcceptChallenge implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            if (command.getName().equals("accept")) {

                if (ChallengeRequests.isChallenged((Player) sender)) {
                    Player challenged = (Player) sender;
                    ChallengeRequests.challengeAccepted.replace(challenged, true);
                    sender.sendMessage(ChatColor.GREEN + "You Accepted The Challenge!");

                    Player challenger = ChallengeRequests.getChallenger(challenged);
                    challenger.sendMessage(ChatColor.GOLD + challenged.getName() + ChatColor.GREEN + " Accepted The Challenge!");

                    TicTacToe ticTacToe = new TicTacToe(challenger, challenged);
                    Games.addGame(challenger, challenged, ticTacToe);
                    ticTacToe.createGame(ticTacToe.getPlayer1());
                }
                else {
                    sender.sendMessage(ChatColor.DARK_AQUA + "You Haven't Been Challenged Yet!");
                }
            }
        }
        return true;
    }
}
