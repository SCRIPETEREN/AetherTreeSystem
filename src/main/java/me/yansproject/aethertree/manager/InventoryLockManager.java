package me.yansproject.aethertree.manager;

import me.yansproject.aethertree.AetherTreeSystem;
import me.yansproject.aethertree.util.MessageUtil;
import me.yansproject.aethertree.util.SoundUtil;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class InventoryLockManager {

    private final AetherTreeSystem plugin;
    private final Set<UUID> lockedPlayers = new HashSet<>();

    public InventoryLockManager(AetherTreeSystem plugin) {
        this.plugin = plugin;
    }

    /**
     * Lock a player's inventory
     */
    public void lock(Player player) {
        if (!plugin.getConfigManager().isInventoryLockEnabled()) {
            return;
        }
        
        lockedPlayers.add(player.getUniqueId());
        plugin.debug("Locked inventory for " + player.getName());
    }

    /**
     * Unlock a player's inventory
     */
    public void unlock(Player player) {
        lockedPlayers.remove(player.getUniqueId());
        plugin.debug("Unlocked inventory for " + player.getName());
    }

    /**
     * Unlock by UUID
     */
    public void unlock(UUID playerId) {
        lockedPlayers.remove(playerId);
    }

    /**
     * Check if a player's inventory is locked
     */
    public boolean isLocked(Player player) {
        return isLocked(player.getUniqueId());
    }

    /**
     * Check if a player's inventory is locked by UUID
     */
    public boolean isLocked(UUID playerId) {
        return lockedPlayers.contains(playerId);
    }

    /**
     * Show lock message and play sound
     */
    public void showLockMessage(Player player) {
        if (!plugin.getConfigManager().showInventoryLockMessage()) {
            return;
        }
        
        String message = plugin.getConfigManager().getMessage("inventory-locked");
        MessageUtil.send(player, message);
        SoundUtil.playInventoryLockedSound(player);
    }

    /**
     * Check if hotbar lock is enabled
     */
    public boolean isHotbarLockEnabled() {
        return plugin.getConfigManager().isInventoryLockEnabled() && 
               plugin.getConfigManager().isHotbarLockEnabled();
    }

    /**
     * Check if inventory click lock is enabled
     */
    public boolean isInventoryClickLockEnabled() {
        return plugin.getConfigManager().isInventoryLockEnabled() && 
               plugin.getConfigManager().isInventoryClickLockEnabled();
    }

    /**
     * Check if item drop lock is enabled
     */
    public boolean isItemDropLockEnabled() {
        return plugin.getConfigManager().isInventoryLockEnabled() && 
               plugin.getConfigManager().isItemDropLockEnabled();
    }

    /**
     * Check if offhand swap lock is enabled
     */
    public boolean isOffhandSwapLockEnabled() {
        return plugin.getConfigManager().isInventoryLockEnabled() && 
               plugin.getConfigManager().isOffhandSwapLockEnabled();
    }

    /**
     * Unlock all players (for plugin disable)
     */
    public void unlockAll() {
        lockedPlayers.clear();
    }

    /**
     * Get count of locked players
     */
    public int getLockedCount() {
        return lockedPlayers.size();
    }
}
