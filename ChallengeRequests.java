package me.mortezapourramzan.mcplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class ChallengeRequests {

    public static List<Player> unAvailablePlayers = new ArrayList<>();

    public static boolean isAvailable(String name) {
        Player ThePlayer = Bukkit.getPlayer(name);
        if (ThePlayer == null) {
            return false;
        } else {
            boolean bool = unAvailablePlayers.stream().anyMatch(player -> player.getName().equals(name));
            return !bool;
        }
    }

    public static Map<Player, Player> inChallenge = new HashMap<>();

    public static Map<Player, Boolean> challengeAccepted = new HashMap<>();

    public static Map<Player, Boolean> challengeDenied = new HashMap<>();


    public static void removeChallenge(Player challenged) {
        inChallenge.remove(challenged);
    }

    public static Boolean isChallenged(Player challengedPlayer) {
        return inChallenge.containsKey(challengedPlayer);
    }

    public static Player getChallenger(Player challenged) {
        return inChallenge.get(challenged);
    }

    public static void timer(Player challenged, Player challenger) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            int secondPassed = 11;
            @Override
            public void run() {
                secondPassed--;
                if (challengeAccepted.get(challenged) || challengeDenied.get(challenged)) {
                    timer.cancel();
                    challengeAccepted.remove(challenged);
                    challengeDenied.remove(challenged);
                    removeChallenge(challenged);
                }
                else if (secondPassed == 1) {
                    challenged.sendMessage(ChatColor.DARK_RED + String.valueOf(secondPassed));
                    challenged.sendMessage(ChatColor.DARK_RED + "Challenge Denied!");
                    timer.cancel();
                    challenger.sendMessage(ChatColor.GOLD + challenged.getName() + ChatColor.DARK_RED +" Denied The Challenge");
                    challengeAccepted.remove(challenged);
                    challengeDenied.remove(challenged);
                    removeChallenge(challenged);
                } else {
                    if (secondPassed < 4 ) {
                        challenged.sendMessage(ChatColor.DARK_RED + String.valueOf(secondPassed));
                    } else {
                        challenged.sendMessage(String.valueOf(secondPassed));
                    }
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }
}
