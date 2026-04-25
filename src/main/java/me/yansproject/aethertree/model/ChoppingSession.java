package me.yansproject.aethertree.model;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class ChoppingSession {

    private final UUID playerId;
    private final Player player;
    private final Block originBlock;
    private final Location originLocation;
    private final Material logType;
    private final List<Block> logs;
    private final List<Block> leaves;
    
    private double progress;
    private final double maxProgress;
    private final double progressPerTick;
    
    private int taskId = -1;
    private long startTime;
    private int tickCount;
    private boolean cancelled;
    private boolean completed;

    public ChoppingSession(Player player, Block originBlock, List<Block> logs, 
            List<Block> leaves, double progressPerTick) {
        this.playerId = player.getUniqueId();
        this.player = player;
        this.originBlock = originBlock;
        this.originLocation = originBlock.getLocation().clone();
        this.logType = originBlock.getType();
        this.logs = logs;
        this.leaves = leaves;
        this.progress = 0;
        this.maxProgress = 100.0;
        this.progressPerTick = progressPerTick;
        this.startTime = System.currentTimeMillis();
        this.tickCount = 0;
        this.cancelled = false;
        this.completed = false;
    }

    /**
     * Add progress and return true if complete
     */
    public boolean addProgress() {
        if (cancelled || completed) return false;
        
        tickCount++;
        progress += progressPerTick;
        
        if (progress >= maxProgress) {
            progress = maxProgress;
            completed = true;
            return true;
        }
        
        return false;
    }

    /**
     * Get progress as percentage (0-100)
     */
    public double getProgressPercent() {
        return Math.min(100, (progress / maxProgress) * 100);
    }

    /**
     * Get progress as fraction (0-1)
     */
    public double getProgressFraction() {
        return Math.min(1, progress / maxProgress);
    }

    /**
     * Get elapsed time in milliseconds
     */
    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

    /**
     * Get log count
     */
    public int getLogCount() {
        return logs.size();
    }

    /**
     * Get leaves count
     */
    public int getLeavesCount() {
        return leaves.size();
    }

    /**
     * Get total block count
     */
    public int getTotalBlockCount() {
        return logs.size() + leaves.size();
    }

    /**
     * Check if player is still looking at the origin block
     */
    public boolean isStillValid() {
        if (cancelled || player == null || !player.isOnline()) return false;
        
        // Check if origin block still exists
        Block current = originLocation.getBlock();
        return current.getType() == logType;
    }

    // Cancel session
    public void cancel() {
        this.cancelled = true;
    }

    // Getters
    public UUID getPlayerId() {
        return playerId;
    }

    public Player getPlayer() {
        return player;
    }

    public Block getOriginBlock() {
        return originBlock;
    }

    public Location getOriginLocation() {
        return originLocation;
    }

    public Material getLogType() {
        return logType;
    }

    public List<Block> getLogs() {
        return logs;
    }

    public List<Block> getLeaves() {
        return leaves;
    }

    public double getProgress() {
        return progress;
    }

    public double getMaxProgress() {
        return maxProgress;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getTickCount() {
        return tickCount;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public boolean isCompleted() {
        return completed;
    }
}
