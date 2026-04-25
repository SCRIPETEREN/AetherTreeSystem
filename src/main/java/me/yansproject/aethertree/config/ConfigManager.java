package me.yansproject.aethertree.config;

import me.yansproject.aethertree.AetherTreeSystem;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final AetherTreeSystem plugin;
    private FileConfiguration config;

    public ConfigManager(AetherTreeSystem plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
    }

    public void reload() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    // ═══════════════════════════════════════════════════════════
    // GENERAL
    // ═══════════════════════════════════════════════════════════
    
    public boolean isEnabled() {
        return config.getBoolean("general.enabled", true);
    }

    public boolean isDebugEnabled() {
        return config.getBoolean("general.debug", false);
    }

    // ═══════════════════════════════════════════════════════════
    // CHOPPING
    // ═══════════════════════════════════════════════════════════
    
    public String getInteractionType() {
        return config.getString("chopping.interaction-type", "RIGHT_CLICK");
    }

    public boolean requireContinuousHold() {
        return config.getBoolean("chopping.require-continuous-hold", true);
    }

    public int getProgressInterval() {
        return config.getInt("chopping.progress-interval", 2);
    }

    public double getBaseProgressPerTick() {
        return config.getDouble("chopping.base-progress-per-tick", 1.0);
    }

    public int getMaxTreeSize() {
        return config.getInt("chopping.max-tree-size", 256);
    }

    public int getMinTreeSize() {
        return config.getInt("chopping.min-tree-size", 3);
    }

    public boolean shouldBreakLeaves() {
        return config.getBoolean("chopping.break-leaves", true);
    }

    public int getLeavesBreakDelay() {
        return config.getInt("chopping.leaves-break-delay", 5);
    }

    public boolean useNaturalDrops() {
        return config.getBoolean("chopping.natural-drops", true);
    }

    public boolean shouldAutoReplant() {
        return config.getBoolean("chopping.auto-replant", true);
    }

    public int getReplantDelay() {
        return config.getInt("chopping.replant-delay", 10);
    }

    // ═══════════════════════════════════════════════════════════
    // INVENTORY LOCK [NEW v2.0]
    // ═══════════════════════════════════════════════════════════
    
    public boolean isInventoryLockEnabled() {
        return config.getBoolean("inventory-lock.enabled", true);
    }
    
    public boolean isHotbarLockEnabled() {
        return config.getBoolean("inventory-lock.lock-hotbar", true);
    }
    
    public boolean isInventoryClickLockEnabled() {
        return config.getBoolean("inventory-lock.lock-inventory-click", true);
    }
    
    public boolean isItemDropLockEnabled() {
        return config.getBoolean("inventory-lock.lock-item-drop", true);
    }
    
    public boolean isOffhandSwapLockEnabled() {
        return config.getBoolean("inventory-lock.lock-offhand-swap", true);
    }
    
    public boolean showInventoryLockMessage() {
        return config.getBoolean("inventory-lock.show-lock-message", true);
    }

    // ═══════════════════════════════════════════════════════════
    // HUNGER [NEW v2.0]
    // ═══════════════════════════════════════════════════════════
    
    public boolean isHungerEnabled() {
        return config.getBoolean("hunger.enabled", true);
    }
    
    public int getHungerBaseCost() {
        return config.getInt("hunger.base-cost", 2);
    }
    
    public double getHungerPerLogCost() {
        return config.getDouble("hunger.per-log-cost", 0.1);
    }
    
    public int getHungerMaxCost() {
        return config.getInt("hunger.max-cost", 8);
    }
    
    public boolean shouldReduceSaturation() {
        return config.getBoolean("hunger.reduce-saturation", true);
    }
    
    public double getSaturationMultiplier() {
        return config.getDouble("hunger.saturation-multiplier", 0.5);
    }
    
    public int getMinFoodLevel() {
        return config.getInt("hunger.min-food-level", 2);
    }
    
    public boolean showHungryMessage() {
        return config.getBoolean("hunger.show-hungry-message", true);
    }
    
    public boolean cancelOnStarving() {
        return config.getBoolean("hunger.cancel-on-starving", false);
    }

    // ═══════════════════════════════════════════════════════════
    // AXES
    // ═══════════════════════════════════════════════════════════
    
    public double getAxeSpeedMultiplier(String axeType) {
        return config.getDouble("axes.speed-multiplier." + axeType, 1.0);
    }

    public double getEfficiencyBonusPerLevel() {
        return config.getDouble("axes.efficiency-bonus-per-level", 0.15);
    }

    public boolean shouldConsumeDurability() {
        return config.getBoolean("axes.consume-durability", true);
    }

    public int getDurabilityPerLog() {
        return config.getInt("axes.durability-per-log", 1);
    }

    public boolean respectUnbreaking() {
        return config.getBoolean("axes.respect-unbreaking", true);
    }

    // ═══════════════════════════════════════════════════════════
    // COOLDOWN
    // ═══════════════════════════════════════════════════════════
    
    public boolean isCooldownEnabled() {
        return config.getBoolean("cooldown.enabled", true);
    }

    public int getCooldownDuration() {
        return config.getInt("cooldown.duration", 3);
    }

    public boolean showCooldownMessage() {
        return config.getBoolean("cooldown.show-message", true);
    }

    // ═══════════════════════════════════════════════════════════
    // DISPLAY - ACTION BAR
    // ═══════════════════════════════════════════════════════════
    
    public boolean isActionBarEnabled() {
        return config.getBoolean("display.action-bar.enabled", true);
    }

    public String getActionBarStyle() {
        return config.getString("display.action-bar.style", "MODERN");
    }

    public int getActionBarLength() {
        return config.getInt("display.action-bar.length", 20);
    }

    public String getFilledChar() {
        return config.getString("display.action-bar.filled-char", "█");
    }

    public String getEmptyChar() {
        return config.getString("display.action-bar.empty-char", "░");
    }

    public String getProgressColor() {
        return config.getString("display.action-bar.progress-color", "&a");
    }

    public String getRemainingColor() {
        return config.getString("display.action-bar.remaining-color", "&7");
    }

    public String getTextColor() {
        return config.getString("display.action-bar.text-color", "&f");
    }

    public boolean showPercentage() {
        return config.getBoolean("display.action-bar.show-percentage", true);
    }

    public boolean showTreeInfo() {
        return config.getBoolean("display.action-bar.show-tree-info", true);
    }

    public String getActionBarFormat() {
        return config.getString("display.action-bar.format", 
            "&8⟨ &a{bar} &8⟩ &f{percent}% &8| &7{logs} logs");
    }

    // ═══════════════════════════════════════════════════════════
    // DISPLAY - TITLE
    // ═══════════════════════════════════════════════════════════
    
    public boolean isTitleEnabled() {
        return config.getBoolean("display.title.enabled", true);
    }

    public String getTitleText() {
        return config.getString("display.title.title", "&a&l✓ TIMBER!");
    }

    public String getSubtitleText() {
        return config.getString("display.title.subtitle", "&7Chopped &f{logs} &7logs");
    }

    public int getTitleFadeIn() {
        return config.getInt("display.title.fade-in", 5);
    }

    public int getTitleStay() {
        return config.getInt("display.title.stay", 20);
    }

    public int getTitleFadeOut() {
        return config.getInt("display.title.fade-out", 10);
    }

    // ═══════════════════════════════════════════════════════════
    // SOUNDS
    // ═══════════════════════════════════════════════════════════
    
    public boolean isSoundEnabled() {
        return config.getBoolean("sounds.enabled", true);
    }

    public String getChoppingSound() {
        return config.getString("sounds.chopping.sound", "BLOCK_WOOD_HIT");
    }

    public float getChoppingSoundVolume() {
        return (float) config.getDouble("sounds.chopping.volume", 0.5);
    }

    public float getChoppingSoundPitch() {
        return (float) config.getDouble("sounds.chopping.pitch", 1.0);
    }

    public int getChoppingSoundInterval() {
        return config.getInt("sounds.chopping.interval", 4);
    }

    public String getCompleteSound() {
        return config.getString("sounds.complete.sound", "ENTITY_PLAYER_LEVELUP");
    }

    public float getCompleteSoundVolume() {
        return (float) config.getDouble("sounds.complete.volume", 1.0);
    }

    public float getCompleteSoundPitch() {
        return (float) config.getDouble("sounds.complete.pitch", 1.5);
    }

    public String getCancelSound() {
        return config.getString("sounds.cancel.sound", "ENTITY_VILLAGER_NO");
    }

    public float getCancelSoundVolume() {
        return (float) config.getDouble("sounds.cancel.volume", 0.7);
    }

    public float getCancelSoundPitch() {
        return (float) config.getDouble("sounds.cancel.pitch", 1.0);
    }
    
    // [NEW v2.0] Inventory Locked Sound
    public String getInventoryLockedSound() {
        return config.getString("sounds.inventory-locked.sound", "BLOCK_NOTE_BLOCK_BASS");
    }
    
    public float getInventoryLockedSoundVolume() {
        return (float) config.getDouble("sounds.inventory-locked.volume", 0.5);
    }
    
    public float getInventoryLockedSoundPitch() {
        return (float) config.getDouble("sounds.inventory-locked.pitch", 0.5);
    }
    
    // [NEW v2.0] Hungry Sound
    public String getHungrySound() {
        return config.getString("sounds.hungry.sound", "ENTITY_PLAYER_BURP");
    }
    
    public float getHungrySoundVolume() {
        return (float) config.getDouble("sounds.hungry.volume", 0.7);
    }
    
    public float getHungrySoundPitch() {
        return (float) config.getDouble("sounds.hungry.pitch", 0.8);
    }

    // ═══════════════════════════════════════════════════════════
    // PARTICLES
    // ═══════════════════════════════════════════════════════════
    
    public boolean isParticleEnabled() {
        return config.getBoolean("particles.enabled", true);
    }

    public String getChoppingParticle() {
        return config.getString("particles.chopping.type", "CRIT");
    }

    public int getChoppingParticleCount() {
        return config.getInt("particles.chopping.count", 3);
    }

    public String getCompleteParticle() {
        return config.getString("particles.complete.type", "VILLAGER_HAPPY");
    }

    public int getCompleteParticleCount() {
        return config.getInt("particles.complete.count", 20);
    }

    // ═══════════════════════════════════════════════════════════
    // WORLDS
    // ═══════════════════════════════════════════════════════════
    
    public String getWorldMode() {
        return config.getString("worlds.mode", "DISABLED");
    }

    public java.util.List<String> getWorldList() {
        return config.getStringList("worlds.list");
    }

    public boolean isWorldAllowed(String worldName) {
        String mode = getWorldMode();
        if (mode.equalsIgnoreCase("DISABLED")) {
            return true;
        }
        
        java.util.List<String> list = getWorldList();
        boolean inList = list.contains(worldName);
        
        if (mode.equalsIgnoreCase("WHITELIST")) {
            return inList;
        } else if (mode.equalsIgnoreCase("BLACKLIST")) {
            return !inList;
        }
        
        return true;
    }

    // ═══════════════════════════════════════════════════════════
    // MESSAGES
    // ═══════════════════════════════════════════════════════════
    
    public String getPrefix() {
        return config.getString("messages.prefix", "&8[&aAetherTree&8] ");
    }

    public String getMessage(String key) {
        return config.getString("messages." + key, "&cMessage not found: " + key);
    }

    public String getMessage(String key, String defaultValue) {
        return config.getString("messages." + key, defaultValue);
    }
}
