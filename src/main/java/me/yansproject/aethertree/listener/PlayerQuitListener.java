package me.yansproject.aethertree.listener;

import me.yansproject.aethertree.AetherTreeSystem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final AetherTreeSystem plugin;

    public PlayerQuitListener(AetherTreeSystem plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Cancel any active chopping session
        plugin.getChoppingManager().cancelSession(event.getPlayer().getUniqueId(), false);
        
        // [NEW v2.0] Unlock inventory
        plugin.getInventoryLockManager().unlock(event.getPlayer());
        
        // Remove cooldown
        plugin.getCooldownManager().removeCooldown(event.getPlayer().getUniqueId());
    }
}
