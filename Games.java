package me.mortezapourramzan.mcplugin;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class Games {

    private static final Map<Player, TicTacToe> games = new HashMap<>();

    private static final Map<Player, Block[][]> blocks = new HashMap<>();

    // all blocks that are being used as board

    public static void addBlockGame(TicTacToe ticTacToe) {
        blocks.put(ticTacToe.getPlayer1(), ticTacToe.getBoard());
    }

    public static void destroyBlocks(TicTacToe ticTacToe) {
        blocks.remove(ticTacToe.getPlayer1());
    }

    public static boolean isGameBlock(Block theBlock) {
        if (!blocks.isEmpty()) {
            List<Block> gamesBlocks = collectionToList((blocks.values()));
            return gamesBlocks.stream().
                    anyMatch(block -> block.getLocation().equals(theBlock.getLocation()));
        }
        return false;
    }

    // all games

    public static void addGame(Player player1, Player player2, TicTacToe ticTacToe) {
        games.put(player1, ticTacToe);
        games.put(player2, ticTacToe);
    }

    public static void removeTheGame(Player player1, Player player2 ) {
        games.remove(player1);
        games.remove(player2);
    }

    public static TicTacToe getTheGame(Player player) {
        return games.getOrDefault(player, null);
    }


    // local methods

    private static List<Block> collectionToList(Collection collection) {
        List<Block> list = null;
        for (Object twoD : collection) {
            Block[][] twoDArray = (Block[][]) twoD;
            list = new ArrayList<>();
            for (Block[] array : twoDArray) {
                list.addAll(Arrays.asList(array));
            }
        }
        return list;
    }
}
