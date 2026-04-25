package me.yansproject.aethertree;

import me.yansproject.aethertree.command.AetherCommand;
import me.yansproject.aethertree.command.AetherTabCompleter;
import me.yansproject.aethertree.config.ConfigManager;
import me.yansproject.aethertree.listener.InventoryLockListener;
import me.yansproject.aethertree.listener.PlayerInteractListener;
import me.yansproject.aethertree.listener.PlayerQuitListener;
import me.yansproject.aethertree.manager.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public final class AetherTreeSystem extends JavaPlugin {

    private static AetherTreeSystem instance;
    
    private ConfigManager configManager;
    private TreeDetectionManager treeDetectionManager;
    private ChoppingManager choppingManager;
    private AxeManager axeManager;
    private CooldownManager cooldownManager;
    private HungerManager hungerManager;
    private InventoryLockManager inventoryLockManager;
    
    // Player toggle states
    private final Map<UUID, Boolean> playerToggles = new HashMap<>();
    
    // Player statistics
    private final Map<UUID, int[]> playerStats = new HashMap<>(); // [treesChopped, totalLogs]

    @Override
    public void onEnable() {
        instance = this;
        
        // ASCII Art Banner
        getLogger().info("");
        getLogger().info("  ╔═══════════════════════════════════════════╗");
        getLogger().info("  ║         AetherTreeSystem 2.0.0            ║");
        getLogger().info("  ║         Created by YansProject            ║");
        getLogger().info("  ╠═══════════════════════════════════════════╣");
        getLogger().info("  ║            [NEW] New Fitur                ║");
        getLogger().info("  ║            [NEW] Fix Bug                  ║");
        getLogger().info("  ╚═══════════════════════════════════════════╝");
        getLogger().info("");
        
        // Initialize managers
        initializeManagers();
        
        // Register listeners
        registerListeners();
        
        // Register commands
        registerCommands();
        
        getLogger().info("§a✓ AetherTreeSystem has been enabled successfully!");
        getLogger().info("§7Detected Minecraft version: §f" + getServer().getBukkitVersion());
    }

    @Override
    public void onDisable() {
        // Cleanup active sessions
        if (choppingManager != null) {
            choppingManager.cleanupAllSessions();
        }
        
        // Cleanup inventory locks
        if (inventoryLockManager != null) {
            inventoryLockManager.unlockAll();
        }
        
        getLogger().info("§c✗ AetherTreeSystem has been disabled.");
        instance = null;
    }
    
    private void initializeManagers() {
        try {
            configManager = new ConfigManager(this);
            axeManager = new AxeManager(this);
            treeDetectionManager = new TreeDetectionManager(this);
            cooldownManager = new CooldownManager(this);
            hungerManager = new HungerManager(this);
            inventoryLockManager = new InventoryLockManager(this);
            choppingManager = new ChoppingManager(this);
            
            getLogger().info("§a✓ All managers initialized successfully.");
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to initialize managers!", e);
            getServer().getPluginManager().disablePlugin(this);
        }
    }
    
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryLockListener(this), this);
        getLogger().info("§a✓ Event listeners registered.");
    }
    
    private void registerCommands() {
        var command = getCommand("aethertree");
        if (command != null) {
            command.setExecutor(new AetherCommand(this));
            command.setTabCompleter(new AetherTabCompleter());
            getLogger().info("§a✓ Commands registered.");
        }
    }
    
    public void reload() {
        configManager.reload();
        axeManager.reload();
        getLogger().info("§a✓ Configuration reloaded.");
    }
    
    // Toggle methods
    public boolean isPlayerEnabled(UUID uuid) {
        return playerToggles.getOrDefault(uuid, true);
    }
    
    public void setPlayerEnabled(UUID uuid, boolean enabled) {
        playerToggles.put(uuid, enabled);
    }
    
    public boolean togglePlayer(UUID uuid) {
        boolean newState = !isPlayerEnabled(uuid);
        setPlayerEnabled(uuid, newState);
        return newState;
    }
    
    // Stats methods
    public int[] getPlayerStats(UUID uuid) {
        return playerStats.getOrDefault(uuid, new int[]{0, 0});
    }
    
    public void addPlayerStats(UUID uuid, int logs) {
        int[] stats = playerStats.getOrDefault(uuid, new int[]{0, 0});
        stats[0]++; // trees chopped
        stats[1] += logs; // total logs
        playerStats.put(uuid, stats);
    }
    
    // Debug logging
    public void debug(String message) {
        if (configManager != null && configManager.isDebugEnabled()) {
            getLogger().info("§7[DEBUG] " + message);
        }
    }
    
    // Getters
    public static AetherTreeSystem getInstance() {
        return instance;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public TreeDetectionManager getTreeDetectionManager() {
        return treeDetectionManager;
    }
    
    public ChoppingManager getChoppingManager() {
        return choppingManager;
    }
    
    public AxeManager getAxeManager() {
        return axeManager;
    }
    
    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }
    
    public HungerManager getHungerManager() {
        return hungerManager;
    }
    
    public InventoryLockManager getInventoryLockManager() {
        return inventoryLockManager;
    }
}
