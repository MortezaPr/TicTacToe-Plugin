package me.mortezapourramzan.mcplugin.Commands;

import me.mortezapourramzan.mcplugin.ChallengeRequests;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Challenge implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            if (command.getName().equals("challenge")) {

                try {
                    if (sender.getName().equals(args[0])) {
                        sender.sendMessage(ChatColor.YELLOW + "You Cant Challenge Yourself Dummy!");
                    }
                    else if (ChallengeRequests.isAvailable(args[0])) {

                        Player challengedPlayer = Bukkit.getPlayer(args[0]);

                        assert challengedPlayer != null;
                        sender.sendMessage("You Have Challenged " + ChatColor.GOLD + challengedPlayer.getName() + ChatColor.WHITE +" To Play Tic-Tac-Toe!");
                        challengedPlayer.sendMessage(ChatColor.GOLD + sender.getName() + ChatColor.WHITE + " Has Challenged You To Play Tic-Tac-Toe!");

                        ChallengeRequests.challengeAccepted.put(challengedPlayer, false);
                        ChallengeRequests.challengeDenied.put(challengedPlayer, false);
                        ChallengeRequests.inChallenge.put(challengedPlayer, (Player) sender);

                        ChallengeRequests.unAvailablePlayers.add(challengedPlayer);
                        ChallengeRequests.unAvailablePlayers.add((Player) sender);

                        ChallengeRequests.timer(challengedPlayer,(Player) sender);
                    }
                    else {
                        sender.sendMessage(ChatColor.DARK_RED + " This Player Is Not Available!");
                    }
                } catch (ArrayIndexOutOfBoundsException exception) {
                    sender.sendMessage(ChatColor.DARK_RED + "You Must Challenge A Player By Enter The Name!");
                }


            }
        }
        return true;
    }
}
