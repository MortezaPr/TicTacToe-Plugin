package me.mortezapourramzan.mcplugin.Commands;

import me.mortezapourramzan.mcplugin.ChallengeRequests;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DenyChallenge implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            if (command.getName().equals("deny")) {

                if (ChallengeRequests.isChallenged((Player) sender)) {
                    ChallengeRequests.challengeDenied.replace((Player) sender, true);
                    sender.sendMessage(ChatColor.RED + "Challenge Denied!");

                    Player challenger = ChallengeRequests.getChallenger((Player) sender);
                    challenger.sendMessage(ChatColor.GOLD + sender.getName() + ChatColor.RED + " Denied The Challenge!");

                    ChallengeRequests.inChallenge.remove(ChallengeRequests.inChallenge.get((Player) sender));
                    ChallengeRequests.unAvailablePlayers.remove(challenger);
                    ChallengeRequests.unAvailablePlayers.remove((Player) sender);
                }
                else {
                    sender.sendMessage(ChatColor.DARK_AQUA + "You Haven't Been Challenged Yet!");
                }
            }
        }
        return true;
    }
}
