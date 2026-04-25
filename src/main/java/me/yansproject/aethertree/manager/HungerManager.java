package me.yansproject.aethertree.manager;

import me.yansproject.aethertree.AetherTreeSystem;
import me.yansproject.aethertree.util.MessageUtil;
import me.yansproject.aethertree.util.SoundUtil;
import org.bukkit.entity.Player;

public class HungerManager {

    private final AetherTreeSystem plugin;

    public HungerManager(AetherTreeSystem plugin) {
        this.plugin = plugin;
    }

    /**
     * Check if player has enough food to start chopping
     */
    public boolean canStartChopping(Player player) {
        if (!plugin.getConfigManager().isHungerEnabled()) {
            return true;
        }
        
        if (player.hasPermission("aethertree.bypass.hunger")) {
            return true;
        }
        
        int minFood = plugin.getConfigManager().getMinFoodLevel();
        if (minFood <= 0) {
            return true;
        }
        
        return player.getFoodLevel() >= minFood;
    }

    /**
     * Show "too hungry" message
     */
    public void showHungryMessage(Player player) {
        if (!plugin.getConfigManager().showHungryMessage()) {
            return;
        }
        
        String message = plugin.getConfigManager().getMessage("too-hungry");
        MessageUtil.send(player, message);
        SoundUtil.playHungrySound(player);
    }

    /**
     * Calculate hunger cost based on logs chopped
     */
    public int calculateHungerCost(int logCount) {
        var config = plugin.getConfigManager();
        
        int baseCost = config.getHungerBaseCost();
        double perLogCost = config.getHungerPerLogCost();
        int maxCost = config.getHungerMaxCost();
        
        // Formula: base + (logs * per-log)
        int totalCost = (int) Math.ceil(baseCost + (logCount * perLogCost));
        
        // Clamp to max
        return Math.min(totalCost, maxCost);
    }

    /**
     * Consume hunger from player after chopping
     */
    public void consumeHunger(Player player, int logCount) {
        if (!plugin.getConfigManager().isHungerEnabled()) {
            return;
        }
        
        if (player.hasPermission("aethertree.bypass.hunger")) {
            return;
        }
        
        var config = plugin.getConfigManager();
        
        int hungerCost = calculateHungerCost(logCount);
        
        // Reduce food level
        int currentFood = player.getFoodLevel();
        int newFood = Math.max(0, currentFood - hungerCost);
        player.setFoodLevel(newFood);
        
        // Reduce saturation
        if (config.shouldReduceSaturation()) {
            float saturationCost = (float) (hungerCost * config.getSaturationMultiplier());
            float currentSaturation = player.getSaturation();
            float newSaturation = Math.max(0, currentSaturation - saturationCost);
            player.setSaturation(newSaturation);
        }
        
        plugin.debug("Consumed " + hungerCost + " hunger from " + player.getName() + 
            " (logs: " + logCount + ")");
    }

    /**
     * Check if player is starving (food level = 0)
     */
    public boolean isStarving(Player player) {
        return player.getFoodLevel() <= 0;
    }
}
