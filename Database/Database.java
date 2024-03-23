package me.mortezapourramzan.mcplugin.Database;

import me.mortezapourramzan.mcplugin.ChallengeRequests;
import me.mortezapourramzan.mcplugin.Games;
import me.mortezapourramzan.mcplugin.TicTacToe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.Objects;
import java.util.UUID;

public class Database {

    private static Connection connection;

    public static Connection getConnection() throws SQLException {

        if (connection != null) {
            return connection;
        }

        String url = "jdbc:mysql://localhost/TicTacToe";
        String user = "{user}";
        String password = "{password}";
        connection = DriverManager.getConnection(url, user, password);

        System.out.println("< Connected To The Database >");

        return connection;
    }

    public void initializeDatabase() throws SQLException {

        Statement statement = getConnection().createStatement();
        String sql =
                "CREATE TABLE IF NOT EXISTS games(id MEDIUMINT not null AUTO_INCREMENT, game_name varchar(10), turn int, is_game_over varchar(5) ,p1_uuid varchar(36), p2_uuid varchar(36), B1 varchar(1), B2 varchar(1), B3 varchar(1), B4 varchar(1), B5 varchar(1), B6 varchar(1), B7 varchar(1), B8 varchar(1), B9 varchar(1), primary key (id))";
        statement.execute(sql);

        statement.close();

        System.out.println("< Table Is Ready >");
    }

    public static void save(TicTacToe ticTacToe) throws SQLException {

        ChallengeRequests.removeChallenge(ticTacToe.getPlayer2());
        ChallengeRequests.unAvailablePlayers.remove(ticTacToe.getPlayer1());
        ChallengeRequests.unAvailablePlayers.remove(ticTacToe.getPlayer2());

        PreparedStatement statement = getConnection()
                .prepareStatement("INSERT INTO games(game_name, turn, is_game_over ,p1_uuid, p2_uuid, B1, B2, B3, B4, B5, B6, B7, B8, B9) VALUES(?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        Block[][] board = ticTacToe.getBoard();

        PreparedStatement statement1 = getConnection()
                .prepareStatement("SELECT * FROM games");

        ResultSet resultSet = statement1.executeQuery();

        int last_id = 0;

        while (resultSet.next()) {
            last_id = resultSet.getInt(1);
        }

        int num = last_id + 1;

        String name = "game" + num;

        statement.setString(1, name);
        statement.setInt(2, ticTacToe.getTurn());
        statement.setString(3, String.valueOf(ticTacToe.isGameOver()));
        statement.setString(4, String.valueOf(ticTacToe.getPlayer1().getUniqueId()));
        statement.setString(5, String.valueOf(ticTacToe.getPlayer2().getUniqueId()));
        statement.setString(6, getColor(board[2][0]));
        statement.setString(7, getColor(board[2][1]));
        statement.setString(8, getColor(board[2][2]));
        statement.setString(9, getColor(board[1][0]));
        statement.setString(10, getColor(board[1][1]));
        statement.setString(11, getColor(board[1][2]));
        statement.setString(12, getColor(board[0][0]));
        statement.setString(13, getColor(board[0][1]));
        statement.setString(14, getColor(board[0][2]));

        ticTacToe.getPlayer1().sendMessage(ChatColor.DARK_PURPLE + "Game Saved!");
        ticTacToe.getPlayer2().sendMessage(ChatColor.DARK_PURPLE + "Game Saved!");

        ticTacToe.getPlayer1().sendMessage(ChatColor.YELLOW + "Save's Name:  " + ChatColor.WHITE + "< "  + name + " >");
        ticTacToe.getPlayer2().sendMessage(ChatColor.YELLOW + "Save's Name:  " + ChatColor.WHITE + "< "  + name + " >");

        ticTacToe.destroyBlocks();
        Games.destroyBlocks(ticTacToe);
        Games.removeTheGame(ticTacToe.getPlayer1(), ticTacToe.getPlayer2());

        statement.executeUpdate();

        statement.close();
    }

    public static void load(String name, Player sender) throws SQLException {

        PreparedStatement statement = getConnection()
                .prepareStatement("SELECT * FROM games WHERE game_name = ?");

        statement.setString(1, name);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {

            String p1 = resultSet.getString("p1_uuid");
            String p2 = resultSet.getString("p2_uuid");

            String bool = resultSet.getString("is_game_over");

            boolean isGameOver = Boolean.parseBoolean(bool);

            int turn = resultSet.getInt("turn");

            Player player1 = Bukkit.getPlayer(UUID.fromString(p1));
            Player player2 = Bukkit.getPlayer(UUID.fromString(p2));

            if (player1 == null || player2 == null) {

                sender.sendMessage(ChatColor.DARK_RED + "The Other Player Is Not Online Right Now!");

            } else if (ChallengeRequests.isAvailable(player1.getName()) && ChallengeRequests.isAvailable(player2.getName())) {

                if (player1 == sender || player2 == sender) {

                    TicTacToe ticTacToe = new TicTacToe(player1, player2);

                    ticTacToe.setGameOver(isGameOver);

                    ticTacToe.setTurn(turn);

                    ticTacToe.createGame(sender);

                    Block[][] board = ticTacToe.getBoard();

                    String B1 = resultSet.getString("B1");
                    board[2][0].getLocation().getBlock().setType(loadColor(B1));
                    addPositions(ticTacToe, board[2][0]);

                    String B2 = resultSet.getString("B2");
                    board[2][1].getLocation().getBlock().setType(loadColor(B2));
                    addPositions(ticTacToe, board[2][1]);

                    String B3 = resultSet.getString("B3");
                    board[2][2].getLocation().getBlock().setType(loadColor(B3));
                    addPositions(ticTacToe, board[2][2]);

                    String B4 = resultSet.getString("B4");
                    board[1][0].getLocation().getBlock().setType(loadColor(B4));
                    addPositions(ticTacToe, board[1][0]);

                    String B5 = resultSet.getString("B5");
                    board[1][1].getLocation().getBlock().setType(loadColor(B5));
                    addPositions(ticTacToe, board[1][1]);

                    String B6 = resultSet.getString("B6");
                    board[1][2].getLocation().getBlock().setType(loadColor(B6));
                    addPositions(ticTacToe, board[1][2]);

                    String B7 = resultSet.getString("B7");
                    board[0][0].getLocation().getBlock().setType(loadColor(B7));
                    addPositions(ticTacToe, board[0][0]);

                    String B8 = resultSet.getString("B8");
                    board[0][1].getLocation().getBlock().setType(loadColor(B8));
                    addPositions(ticTacToe, board[0][1]);

                    String B9 = resultSet.getString("B9");
                    board[0][2].getLocation().getBlock().setType(loadColor(B9));
                    addPositions(ticTacToe, board[0][2]);

                    ticTacToe.setBoard(board);

                    String result = ticTacToe.gameResult();

                    if (result == null) {
                        Games.addGame(player1, player2, ticTacToe);
                        Games.addBlockGame(ticTacToe);
                        ChallengeRequests.inChallenge.put(player1, player2);
                        ChallengeRequests.unAvailablePlayers.add(player1);
                        ChallengeRequests.unAvailablePlayers.add(player2);
                    } else {
                        ticTacToe.destroyBlocks();
                        if (result.equals(player1.getName())) {
                            showWinner(ticTacToe, player1.getName());
                        } else if (result.equals(player2.getName())) {
                            showWinner(ticTacToe, player2.getName());
                        } else {
                            ticTacToe.getPlayer1().sendMessage(ChatColor.YELLOW + "The Game Is Over!");
                            ticTacToe.getPlayer2().sendMessage(ChatColor.YELLOW + "The Game Is Over!");
                            ticTacToe.getPlayer1().sendMessage(ChatColor.GREEN + "Final Result: " + ChatColor.WHITE + "Draw");
                            ticTacToe.getPlayer2().sendMessage(ChatColor.GREEN + "Final Result: " + ChatColor.WHITE + "Draw");
                        }
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "You Don't Have Permission To Load This Game.");
                }
            } else {
                sender.sendMessage(ChatColor.DARK_RED + "You Cant Load This Game Right Now.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "There Is No Save With This Name!");
        }
    }

    private static String getColor(Block block) {
        if (block.getType() == Material.RED_WOOL) {
            return "R";
        } else if (block.getType() == Material.BLUE_WOOL) {
            return "B";
        } else {
            return "W";
        }
    }

    private static Material loadColor(String color) {

        if (Objects.equals(color, "W")) {
            return Material.WHITE_WOOL;
        } else if (Objects.equals(color, "B")) {
            return Material.BLUE_WOOL;
        } else {
            return Material.RED_WOOL;
        }
    }

    private static void showWinner(TicTacToe ticTacToe, String winner) {
        ticTacToe.getPlayer1().sendMessage(ChatColor.YELLOW + "The Game Is Over!");
        ticTacToe.getPlayer2().sendMessage(ChatColor.YELLOW + "The Game Is Over!");
        ticTacToe.getPlayer1().sendMessage(ChatColor.GREEN + "Final Result: " + ChatColor.WHITE + winner +
                " Is The Winner!");
        ticTacToe.getPlayer2().sendMessage(ChatColor.GREEN + "Final Result: " + ChatColor.WHITE + winner +
                " Is The Winner!");
    }

    private static void addPositions(TicTacToe ticTacToe, Block block) {

        if (block.getType() == Material.RED_WOOL) {
            ticTacToe.addPlayer1Positions(block);
        } else if (block.getType() == Material.BLUE_WOOL) {
            ticTacToe.addPlayer2Positions(block);
        }
    }
}
