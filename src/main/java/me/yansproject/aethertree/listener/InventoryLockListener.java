package me.yansproject.aethertree.listener;

import me.yansproject.aethertree.AetherTreeSystem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class InventoryLockListener implements Listener {

    private final AetherTreeSystem plugin;
    private long lastMessageTime = 0;
    private static final long MESSAGE_COOLDOWN = 1000; // 1 second cooldown between messages

    public InventoryLockListener(AetherTreeSystem plugin) {
        this.plugin = plugin;
    }

    /**
     * Prevent hotbar slot switching
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onHotbarSwitch(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        
        if (!plugin.getInventoryLockManager().isLocked(player)) {
            return;
        }
        
        if (!plugin.getInventoryLockManager().isHotbarLockEnabled()) {
            return;
        }
        
        event.setCancelled(true);
        showMessageWithCooldown(player);
    }

    /**
     * Prevent inventory clicking
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        
        if (!plugin.getInventoryLockManager().isLocked(player)) {
            return;
        }
        
        if (!plugin.getInventoryLockManager().isInventoryClickLockEnabled()) {
            return;
        }
        
        event.setCancelled(true);
        showMessageWithCooldown(player);
    }

    /**
     * Prevent inventory dragging
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        
        if (!plugin.getInventoryLockManager().isLocked(player)) {
            return;
        }
        
        if (!plugin.getInventoryLockManager().isInventoryClickLockEnabled()) {
            return;
        }
        
        event.setCancelled(true);
        showMessageWithCooldown(player);
    }

    /**
     * Prevent item dropping
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        
        if (!plugin.getInventoryLockManager().isLocked(player)) {
            return;
        }
        
        if (!plugin.getInventoryLockManager().isItemDropLockEnabled()) {
            return;
        }
        
        event.setCancelled(true);
        showMessageWithCooldown(player);
    }

    /**
     * Prevent offhand swap (F key)
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onOffhandSwap(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        
        if (!plugin.getInventoryLockManager().isLocked(player)) {
            return;
        }
        
        if (!plugin.getInventoryLockManager().isOffhandSwapLockEnabled()) {
            return;
        }
        
        event.setCancelled(true);
        showMessageWithCooldown(player);
    }

    /**
     * Show message with cooldown to prevent spam
     */
    private void showMessageWithCooldown(Player player) {
        long now = System.currentTimeMillis();
        if (now - lastMessageTime > MESSAGE_COOLDOWN) {
            plugin.getInventoryLockManager().showLockMessage(player);
            lastMessageTime = now;
        }
    }
}
