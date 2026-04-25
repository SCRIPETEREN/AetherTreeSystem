package me.yansproject.aethertree.manager;

import me.yansproject.aethertree.AetherTreeSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final AetherTreeSystem plugin;
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public CooldownManager(AetherTreeSystem plugin) {
        this.plugin = plugin;
    }

    /**
     * Check if a player is on cooldown
     */
    public boolean isOnCooldown(UUID playerId) {
        if (!plugin.getConfigManager().isCooldownEnabled()) {
            return false;
        }
        
        Long expiry = cooldowns.get(playerId);
        if (expiry == null) {
            return false;
        }
        
        if (System.currentTimeMillis() >= expiry) {
            cooldowns.remove(playerId);
            return false;
        }
        
        return true;
    }

    /**
     * Get remaining cooldown time in seconds
     */
    public double getRemainingCooldown(UUID playerId) {
        Long expiry = cooldowns.get(playerId);
        if (expiry == null) {
            return 0;
        }
        
        long remaining = expiry - System.currentTimeMillis();
        if (remaining <= 0) {
            cooldowns.remove(playerId);
            return 0;
        }
        
        return remaining / 1000.0;
    }

    /**
     * Set cooldown for a player
     */
    public void setCooldown(UUID playerId) {
        if (!plugin.getConfigManager().isCooldownEnabled()) {
            return;
        }
        
        int durationSeconds = plugin.getConfigManager().getCooldownDuration();
        long expiryTime = System.currentTimeMillis() + (durationSeconds * 1000L);
        cooldowns.put(playerId, expiryTime);
    }

    /**
     * Remove cooldown for a player
     */
    public void removeCooldown(UUID playerId) {
        cooldowns.remove(playerId);
    }

    /**
     * Clear all cooldowns
     */
    public void clearAll() {
        cooldowns.clear();
    }
}
