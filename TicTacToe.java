package me.mortezapourramzan.mcplugin;

import me.mortezapourramzan.mcplugin.Database.Database;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TicTacToe implements Serializable {

    private int turn = 1;
    private boolean inGame;

    private boolean isGameOver = false;

    private static Player player1;
    private static Player player2;

    private Block[][] board = new Block[3][3];

    private final ArrayList<Integer> player1Positions = new ArrayList<>();
    private final ArrayList<Integer> player2Positions = new ArrayList<>();
    private final ArrayList<Integer> fullPlaces = new ArrayList<>();

    public TicTacToe(Player p1, Player p2) {
        player1 = p1;
        player2 = p2;
        inGame = true;
    }

    // getter

    public boolean isGameOver() {
        return isGameOver;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getTurn() {
        return turn;
    }

    public Block[][] getBoard() {
        return board;
    }

    public boolean isInGame() {
        return inGame;
    }

    public ArrayList<Integer> getPlayer1Positions() {
        return player1Positions;
    }

    public ArrayList<Integer> getPlayer2Positions() {
        return player2Positions;
    }

    public ArrayList<Integer> getFullPlaces() {
        return fullPlaces;
    }


    // setter

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public void setBoard(Block[][] board) {
        this.board = board;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public static void setPlayer1(Player player1) {
        TicTacToe.player1 = player1;
    }

    public static void setPlayer2(Player player2) {
        TicTacToe.player2 = player2;
    }


    // methods

    public void addPlayer1Positions(Block block) {
        convertLocationToNumber(player1Positions, block);
        convertLocationToNumber(fullPlaces, block);
    }

    public void addPlayer2Positions(Block block) {
        convertLocationToNumber(player2Positions, block);
        convertLocationToNumber(fullPlaces, block);
    }

    public void put(Player player, int pos, int k) {

        if ( ( (player.getName() == player1.getName()) && (k == 1) ) ||
                (player.getName() == player2.getName()) && (k == -1) ) {

            boolean full = false;

            for (int num : fullPlaces) {
                if (num == pos) {
                    player.sendMessage("This Place Is Full!");
                    full = true;
                    break;
                }
            }

            if (!full) {

                fullPlaces.add(pos);
                if (k == 1) {
                    player1Positions.add(pos);
                } else {
                    player2Positions.add(pos);
                }

                Material material;
                assert false;
                if (k==1) {
                    material = Material.RED_WOOL;
                } else {
                    material = Material.BLUE_WOOL;
                }

                switch (pos) {
                    case 1 -> board[2][0].setType(material);
                    case 2 -> board[2][1].setType(material);
                    case 3 -> board[2][2].setType(material);
                    case 4 -> board[1][0].setType(material);
                    case 5 -> board[1][1].setType(material);
                    case 6 -> board[1][2].setType(material);
                    case 7 -> board[0][0].setType(material);
                    case 8 -> board[0][1].setType(material);
                    case 9 -> board[0][2].setType(material);
                }

                changeTurn();
                gameStatus();
            } else {
                if (k == 1) {
                    player1.sendMessage(ChatColor.YELLOW + "This Place Is Full!");
                } else {
                    player2.sendMessage(ChatColor.YELLOW + "This Place Is Full!");
                }
            }
        } else {
            player.sendMessage(ChatColor.YELLOW + "Its Not Your Turn!");
        }
    }

    public String gameResult() {
        List<Integer> topRow = Arrays.asList(1, 2, 3);
        List<Integer> midRow = Arrays.asList(4, 5, 6);
        List<Integer> botRow = Arrays.asList(7, 8, 9);

        List<Integer> leftCol = Arrays.asList(1, 4, 7);
        List<Integer> midCol = Arrays.asList(2, 5, 8);
        List<Integer> rightCol = Arrays.asList(3, 6, 9);

        List<Integer> cross1 = Arrays.asList(1, 5, 9);
        List<Integer> cross2 = Arrays.asList(7, 5, 3);

        List<List<Integer>> winning = new ArrayList<>();
        winning.add(topRow);
        winning.add(midRow);
        winning.add(botRow);
        winning.add(leftCol);
        winning.add(midCol);
        winning.add(rightCol);
        winning.add(cross1);
        winning.add(cross2);

        for (List<Integer> l : winning) {
            if (player1Positions.containsAll(l)) {
                isGameOver = true;
                return player1.getName();
            } else if (player2Positions.containsAll(l)) {
                isGameOver = true;
                return player2.getName();
            } else if (player1Positions.size() + player2Positions.size() == 9){
                isGameOver = true;
                return "Draw";
            }
        }
        return null;
    }

    public void gameStatus() {

        String result = gameResult();

        if (result != null && !Objects.equals(result, "Draw")) {

            try {
                Database.save(this);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Color color;
            Player winner;

            if (result.equals(player1.getName())) {
                winner = player1;
                color = Color.RED;
            } else {
                winner = player2;
                color = Color.BLUE;
            }

            Location currentLoc = winner.getLocation();
            Vector vec = winner.getEyeLocation().getDirection().multiply(6).setY(0);
            Location loc = currentLoc.add(vec);

            Firework fw = (Firework) Objects.requireNonNull(loc.getWorld()).spawnEntity(loc, EntityType.FIREWORK);
            FireworkMeta fwm = fw.getFireworkMeta();

            fwm.setPower(3);
            fwm.addEffect(FireworkEffect.builder().withColor(color).flicker(true).build());

            fw.setFireworkMeta(fwm);
            fw.detonate();

            for(int i = 0;i<10; i++){
                Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
                fw2.setFireworkMeta(fwm);
            }

            if (winner == player1) {
                player1.sendMessage(ChatColor.DARK_RED + result +
                        ChatColor.WHITE + " Is The Winner!");
            } else {
                player2.sendMessage(ChatColor.DARK_BLUE + result +
                        ChatColor.WHITE + " Is The Winner!");
            }

        } else if (Objects.equals(result, "Draw")) {

            try {
                Database.save(this);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            player1.sendMessage(ChatColor.LIGHT_PURPLE + result + "!");
            player2.sendMessage(ChatColor.LIGHT_PURPLE + result + "!");
        }
    }

    public void createGame(Player sender) {
        if (!isGameOver) {
            Location teleport = sender.getLocation().add(1,0,0);
            if (sender == player1) {
                player2.teleport(teleport);
            } else {
                player1.teleport(teleport);
            }
        }
        Location currentLoc = sender.getLocation();
        Vector vec = sender.getEyeLocation().getDirection().multiply(6).setY(0);
        Location boardLocation = currentLoc.add(vec);

        for(int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                boardLocation.getBlock().setType(Material.WHITE_WOOL);
                Games.addBlockGame(this);
                board[i][j] = boardLocation.getBlock();
                boardLocation = boardLocation.add(1,0,0);
            }
            boardLocation.subtract(3,0,0);
            boardLocation.add(0,1,0);
        }
    }

    public void changeTurn() {
        turn *= -1;
    }

    public boolean isTurn(Player player) {
        if (player1.getName().equals(player.getName())) {
            return turn == 1;
        } else if (player2.getName().equals(player.getName())) {
            return turn == -1;
        }
        return true;
    }

    public void destroyBlocks() {
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                board[i][j].getLocation().getBlock().setType(Material.AIR);
            }
        }
    }

    // local methods

    private void convertLocationToNumber(ArrayList<Integer> positions, Block block) {

        Location location = block.getLocation();
        if (board[2][0].getLocation().equals(location)) {
            positions.add(1);
        } else if (board[2][1].getLocation().equals(location)) {
            positions.add(2);
        } else if (board[2][2].getLocation().equals(location)) {
            positions.add(3);
        } else if (board[1][0].getLocation().equals(location)) {
            positions.add(4);
        } else if (board[1][1].getLocation().equals(location)) {
            positions.add(5);
        } else if (board[1][2].getLocation().equals(location)) {
            positions.add(6);
        } else if (board[0][0].getLocation().equals(location)) {
            positions.add(7);
        } else if (board[0][1].getLocation().equals(location)) {
            positions.add(8);
        } else if (board[0][2].getLocation().equals(location)) {
            positions.add(9);
        }
    }
}
