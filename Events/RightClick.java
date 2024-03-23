package me.mortezapourramzan.mcplugin.Events;

import me.mortezapourramzan.mcplugin.Games;
import me.mortezapourramzan.mcplugin.TicTacToe;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Objects;

public class RightClick implements Listener {

    @EventHandler
    public void onPlayer(PlayerInteractEvent e) {

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getHand() == EquipmentSlot.HAND) {

            TicTacToe tictactoe = Games.getTheGame(e.getPlayer());

            try {
                Material color = Material.WHITE_WOOL;
                if (tictactoe.getPlayer1().getName().equals(e.getPlayer().getName())) {
                    color = Material.RED_WOOL;
                } else if (tictactoe.getPlayer2().getName().equals(e.getPlayer().getName())) {
                    color = Material.BLUE_WOOL;
                }


                if (tictactoe.isTurn(e.getPlayer())) {

                    Block[][] board = tictactoe.getBoard();

                    for (int i = 0; i < 3; i++) {

                        for (int j = 0; j < 3; j++) {

                            Block theBlock = board[i][j];

                            if (Objects.requireNonNull(e.getClickedBlock()).getLocation().equals(theBlock.getLocation())) {

                                if (e.getClickedBlock().getType() == Material.WHITE_WOOL) {

                                    theBlock.setType(color);
                                    tictactoe.changeTurn();

                                    if (tictactoe.getPlayer1() == e.getPlayer()) {
                                        tictactoe.addPlayer1Positions(theBlock);
                                    } else {
                                        tictactoe.addPlayer2Positions(theBlock);
                                    }

                                    tictactoe.gameStatus();

                                } else {
                                    e.getPlayer().sendMessage(ChatColor.RED + "This Place Is Full!");
                                }
                            }
                        }
                    }
                } else {
                    e.getPlayer().sendMessage(ChatColor.YELLOW + "Its Not Your Turn!");
                }
            } catch (NullPointerException exception) {
                System.out.println("The Player Is Not In Any Game");
            }
        }
    }
}






