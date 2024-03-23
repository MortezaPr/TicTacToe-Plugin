package me.mortezapourramzan.mcplugin.Events;

import me.mortezapourramzan.mcplugin.Games;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class LeftClick implements Listener {

    @EventHandler
    public void onPlayer(PlayerInteractEvent e) {

        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {

            if (Games.isGameBlock(e.getClickedBlock())) {
                e.setCancelled(true);
            }
        }
    }
}
