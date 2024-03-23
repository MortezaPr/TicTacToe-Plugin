package me.mortezapourramzan.mcplugin;
import me.mortezapourramzan.mcplugin.Commands.*;
import me.mortezapourramzan.mcplugin.Database.Database;
import me.mortezapourramzan.mcplugin.Events.LeftClick;
import me.mortezapourramzan.mcplugin.Events.RightClick;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.Objects;

public final class MCPlugin  extends JavaPlugin {

    @Override
    public void onEnable() {

        System.out.println("-----------------------------------------------------------");

        System.out.println("< The Plugin Has Started >");

        Database db = new Database();
        try {
            db.initializeDatabase();
        } catch (SQLException e) {
            System.out.println("< Unable To Connect To The Database >");
            e.printStackTrace();
        }

        System.out.println("-----------------------------------------------------------");

        getServer().getPluginManager().registerEvents(new RightClick(), this);
        getServer().getPluginManager().registerEvents(new LeftClick(), this);
        Objects.requireNonNull(getCommand("challenge")).setExecutor(new Challenge());
        Objects.requireNonNull(getCommand("accept")).setExecutor(new AcceptChallenge());
        Objects.requireNonNull(getCommand("deny")).setExecutor(new DenyChallenge());
        Objects.requireNonNull(getCommand("save")).setExecutor(new SaveGame());
        Objects.requireNonNull(getCommand("load")).setExecutor(new LoadGame());
        Objects.requireNonNull(getCommand("put")).setExecutor(new Put());
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("< The Plugin Has Stopped >");
    }
}
