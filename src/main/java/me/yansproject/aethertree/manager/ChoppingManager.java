package me.yansproject.aethertree.manager;

import me.yansproject.aethertree.AetherTreeSystem;
import me.yansproject.aethertree.model.ChoppingSession;
import me.yansproject.aethertree.util.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class ChoppingManager {

    private final AetherTreeSystem plugin;
    private final Map<UUID, ChoppingSession> activeSessions = new HashMap<>();
    private final Map<UUID, BukkitTask> sessionTasks = new HashMap<>();

    public ChoppingManager(AetherTreeSystem plugin) {
        this.plugin = plugin;
    }

    /**
     * Start or continue a chopping session
     */
    public void handleInteraction(Player player, Block block, ItemStack axe) {
        UUID playerId = player.getUniqueId();
        var config = plugin.getConfigManager();

        // Check if already in a session
        if (activeSessions.containsKey(playerId)) {
            ChoppingSession existing = activeSessions.get(playerId);
            
            // Check if it's the same tree
            if (existing.getOriginLocation().equals(block.getLocation())) {
                // Continue existing session (handled by task)
                return;
            } else {
                // Different tree, cancel old session
                cancelSession(playerId, false);
            }
        }

        // Check cooldown
        if (plugin.getCooldownManager().isOnCooldown(playerId)) {
            if (!player.hasPermission("aethertree.bypass.cooldown")) {
                if (config.showCooldownMessage()) {
                    double remaining = plugin.getCooldownManager().getRemainingCooldown(playerId);
                    String msg = config.getMessage("cooldown-active")
                        .replace("{time}", String.format("%.1f", remaining));
                    MessageUtil.send(player, msg);
                }
                return;
            }
        }

        // [NEW v2.0] Check hunger
        if (!plugin.getHungerManager().canStartChopping(player)) {
            plugin.getHungerManager().showHungryMessage(player);
            return;
        }

        // Detect tree
        var treeResult = plugin.getTreeDetectionManager().detectTree(block);
        
        if (treeResult == null) {
            MessageUtil.send(player, config.getMessage("not-a-tree"));
            SoundUtil.playCancelSound(player);
            return;
        }

        // Check tree size limit
        int maxSize = config.getMaxTreeSize();
        if (maxSize > 0 && treeResult.logs().size() > maxSize) {
            if (!player.hasPermission("aethertree.unlimited")) {
                String msg = config.getMessage("tree-too-large")
                    .replace("{max}", String.valueOf(maxSize));
                MessageUtil.send(player, msg);
                SoundUtil.playCancelSound(player);
                return;
            }
        }

        // Calculate progress per tick
        double progressPerTick = plugin.getAxeManager().calculateProgressPerTick(
            axe, treeResult.logs().size()
        );

        // Create new session
        ChoppingSession session = new ChoppingSession(
            player, block, treeResult.logs(), treeResult.leaves(), progressPerTick
        );

        activeSessions.put(playerId, session);

        // [NEW v2.0] Lock inventory
        plugin.getInventoryLockManager().lock(player);

        // Start the chopping task
        startChoppingTask(session);
        
        plugin.debug("Started chopping session for " + player.getName() + 
            " - " + treeResult.logs().size() + " logs");
    }

    /**
     * Start the repeating task for chopping progress
     */
    private void startChoppingTask(ChoppingSession session) {
        UUID playerId = session.getPlayerId();
        var config = plugin.getConfigManager();
        int interval = config.getProgressInterval();

        BukkitTask task = new BukkitRunnable() {
            int soundTick = 0;

            @Override
            public void run() {
                Player player = session.getPlayer();
                
                // Validate session
                if (player == null || !player.isOnline() || session.isCancelled()) {
                    cancelSession(playerId, false);
                    return;
                }

                // Check if player is still holding right click (has axe in hand)
                ItemStack mainHand = player.getInventory().getItemInMainHand();
                if (!plugin.getAxeManager().isAxe(mainHand)) {
                    cancelSession(playerId, true);
                    return;
                }

                // Check if still looking at the tree (origin block still exists)
                if (!session.isStillValid()) {
                    cancelSession(playerId, true);
                    return;
                }

                // [NEW v2.0] Check if player is starving and cancel-on-starving is enabled
                if (config.cancelOnStarving() && plugin.getHungerManager().isStarving(player)) {
                    cancelSession(playerId, true);
                    plugin.getHungerManager().showHungryMessage(player);
                    return;
                }

                // Add progress
                boolean complete = session.addProgress();

                // Update action bar
                updateActionBar(session);

                // Play sounds periodically
                soundTick++;
                if (soundTick >= config.getChoppingSoundInterval()) {
                    SoundUtil.playChoppingSound(player);
                    
                    // Spawn particles
                    if (config.isParticleEnabled()) {
                        SoundUtil.spawnParticles(
                            session.getOriginLocation(),
                            config.getChoppingParticle(),
                            config.getChoppingParticleCount()
                        );
                    }
                    
                    soundTick = 0;
                }

                // Check completion
                if (complete) {
                    completeSession(session);
                }
            }
        }.runTaskTimer(plugin, 0L, interval);

        sessionTasks.put(playerId, task);
        session.setTaskId(task.getTaskId());
    }

    /**
     * Update the action bar display
     */
    private void updateActionBar(ChoppingSession session) {
        var config = plugin.getConfigManager();
        if (!config.isActionBarEnabled()) return;

        Player player = session.getPlayer();
        double progress = session.getProgressFraction();
        int percent = (int) Math.round(session.getProgressPercent());

        // Build progress bar
        String bar = ActionBarUtil.buildStyledBar(
            progress,
            config.getActionBarLength(),
            config.getActionBarStyle(),
            config.getFilledChar(),
            config.getEmptyChar(),
            config.getProgressColor(),
            config.getRemainingColor()
        );

        // pesan nya gw taro sini
        String format = config.getActionBarFormat()
            .replace("{bar}", bar)
            .replace("{percent}", String.valueOf(percent))
            .replace("{logs}", String.valueOf(session.getLogCount()))
            .replace("{axe}", player.getInventory().getItemInMainHand().getType().name());

        ActionBarUtil.send(player, format);
    }

   
    private void completeSession(ChoppingSession session) {
        Player player = session.getPlayer();
        UUID playerId = session.getPlayerId();
        var config = plugin.getConfigManager();

        // berhenti ngerjain
        cancelTask(playerId);

        // [NEW v2.0] Unlock inventory
        plugin.getInventoryLockManager().unlock(player);

        // Get the axe before breaking
        ItemStack axe = player.getInventory().getItemInMainHand();

        // Break all logs
        List<Block> logs = session.getLogs();
        boolean axeBroke = false;

        for (Block log : logs) {
            if (!BlockUtil.isLog(log)) continue; // Skip if already broken
            
            Location loc = log.getLocation();
            
            // Drop items
            if (config.useNaturalDrops()) {
                log.breakNaturally(axe);
            } else {
                // Add to inventory
                for (ItemStack drop : log.getDrops(axe)) {
                    HashMap<Integer, ItemStack> overflow = player.getInventory().addItem(drop);
                    // Drop overflow naturally
                    for (ItemStack item : overflow.values()) {
                        player.getWorld().dropItemNaturally(loc, item);
                    }
                }
                log.setType(Material.AIR);
            }

            // Consume durability
            if (!axeBroke && plugin.getAxeManager().isAxe(axe)) {
                axeBroke = plugin.getAxeManager().consumeDurability(player, axe, 1);
                if (axeBroke) {
                    MessageUtil.send(player, config.getMessage("axe-broke"));
                    break; // Stop breaking if axe broke
                }
            }
        }

        // Break leaves with delay
        if (config.shouldBreakLeaves() && !session.getLeaves().isEmpty()) {
            breakLeavesDelayed(session.getLeaves(), config.getLeavesBreakDelay());
        }

        // Auto replant
        if (config.shouldAutoReplant()) {
            Location replantLoc = findReplantLocation(session);
            if (replantLoc != null) {
                Material sapling = BlockUtil.getSaplingFor(session.getLogType());
                if (sapling != null) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Block block = replantLoc.getBlock();
                            if (block.getType() == Material.AIR) {
                                block.setType(sapling);
                            }
                        }
                    }.runTaskLater(plugin, config.getReplantDelay());
                }
            }
        }

        // [NEW v2.0] Consume hunger
        plugin.getHungerManager().consumeHunger(player, logs.size());

        // Play completion effects
        SoundUtil.playCompleteSound(player);
        
        if (config.isParticleEnabled()) {
            SoundUtil.spawnParticles(
                session.getOriginLocation(),
                config.getCompleteParticle(),
                config.getCompleteParticleCount()
            );
        }

        // Show title
        if (config.isTitleEnabled()) {
            String title = config.getTitleText();
            String subtitle = config.getSubtitleText()
                .replace("{logs}", String.valueOf(logs.size()));
            
            MessageUtil.sendTitle(player, title, subtitle,
                config.getTitleFadeIn(),
                config.getTitleStay(),
                config.getTitleFadeOut()
            );
        }

        // Clear action bar
        ActionBarUtil.clear(player);

        // Update stats
        plugin.addPlayerStats(playerId, logs.size());

        // Set cooldown
        plugin.getCooldownManager().setCooldown(playerId);

        // Remove session
        activeSessions.remove(playerId);
        
        plugin.debug("Completed chopping for " + player.getName() + 
            " - " + logs.size() + " logs broken");
    }

    /**
     * Break leaves with a delay
     */
    private void breakLeavesDelayed(List<Block> leaves, int delayTicks) {
        new BukkitRunnable() {
            int index = 0;
            
            @Override
            public void run() {
                // Break a few leaves per tick for effect
                for (int i = 0; i < 5 && index < leaves.size(); i++, index++) {
                    Block leaf = leaves.get(index);
                    if (BlockUtil.isLeaves(leaf)) {
                        leaf.breakNaturally();
                    }
                }
                
                if (index >= leaves.size()) {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, delayTicks, 1L);
    }

    /**
     * Find a valid location to replant the sapling
     */
    private Location findReplantLocation(ChoppingSession session) {
        // Find the lowest log position
        Block lowest = session.getLogs().stream()
            .min(Comparator.comparingInt(Block::getY))
            .orElse(null);
        
        if (lowest == null) return null;
        
        Location loc = lowest.getLocation();
        Block below = loc.clone().subtract(0, 1, 0).getBlock();
        
        if (BlockUtil.isValidTreeBase(below)) {
            return loc;
        }
        
        return null;
    }

    /**
     * Cancel a session
     */
    public void cancelSession(UUID playerId, boolean showMessage) {
        ChoppingSession session = activeSessions.remove(playerId);
        cancelTask(playerId);
        
        // [NEW v2.0] Unlock inventory
        plugin.getInventoryLockManager().unlock(playerId);
        
        if (session != null) {
            session.cancel();
            Player player = session.getPlayer();
            
            if (player != null && player.isOnline()) {
                ActionBarUtil.clear(player);
                
                if (showMessage) {
                    var config = plugin.getConfigManager();
                    MessageUtil.send(player, config.getMessage("chopping-cancelled"));
                    SoundUtil.playCancelSound(player);
                }
            }
            
            plugin.debug("Cancelled session for " + playerId);
        }
    }

    /**
     * Cancel the task for a player
     */
    private void cancelTask(UUID playerId) {
        BukkitTask task = sessionTasks.remove(playerId);
        if (task != null) {
            task.cancel();
        }
    }

    /**
     * Check if a player has an active session
     */
    public boolean hasActiveSession(UUID playerId) {
        return activeSessions.containsKey(playerId);
    }

    /**
     * Get active session for a player
     */
    public ChoppingSession getSession(UUID playerId) {
        return activeSessions.get(playerId);
    }

    /**
     * Cleanup all sessions (for plugin disable)
     */
    public void cleanupAllSessions() {
        for (UUID playerId : new HashSet<>(activeSessions.keySet())) {
            cancelSession(playerId, false);
        }
        activeSessions.clear();
        sessionTasks.clear();
    }
}
