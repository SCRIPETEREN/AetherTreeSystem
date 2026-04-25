package me.yansproject.aethertree.manager;

import me.yansproject.aethertree.AetherTreeSystem;
import me.yansproject.aethertree.util.BlockUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.*;

public class TreeDetectionManager {

    private final AetherTreeSystem plugin;

    // Directions to check for connected logs
    private static final BlockFace[] LOG_SEARCH_FACES = {
        BlockFace.UP, BlockFace.DOWN,
        BlockFace.NORTH, BlockFace.SOUTH,
        BlockFace.EAST, BlockFace.WEST,
        // Diagonals (natural trees)
        BlockFace.NORTH_EAST, BlockFace.NORTH_WEST,
        BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST
    };

    public TreeDetectionManager(AetherTreeSystem plugin) {
        this.plugin = plugin;
    }

    /**
     * Detect all logs connected to the starting block using BFS
     */
    public TreeResult detectTree(Block startBlock) {
        if (!BlockUtil.isLog(startBlock)) return null;

        Material logType = startBlock.getType();
        var config = plugin.getConfigManager();

        int maxSize = config.getMaxTreeSize();
        int minSize = config.getMinTreeSize();
        boolean breakLeaves = config.shouldBreakLeaves();

        Set<Block> visitedLogs = new HashSet<>();
        Set<Block> visitedLeaves = new HashSet<>();

        Queue<Block> logQueue = new LinkedList<>();
        Queue<Block> leavesQueue = new LinkedList<>();

        logQueue.add(startBlock);
        visitedLogs.add(startBlock);

        // === BFS LOG DETECTION ===
        while (!logQueue.isEmpty()) {

            if (maxSize > 0 && visitedLogs.size() >= maxSize) break;

            Block current = logQueue.poll();

            for (BlockFace face : LOG_SEARCH_FACES) {
                Block neighbor = current.getRelative(face);

                // Handle UP diagonals manually (important for tree shapes)
                if (face == BlockFace.UP) {
                    for (BlockFace horizontal : new BlockFace[]{
                            BlockFace.NORTH, BlockFace.SOUTH,
                            BlockFace.EAST, BlockFace.WEST,
                            BlockFace.NORTH_EAST, BlockFace.NORTH_WEST,
                            BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST
                    }) {
                        Block diagonal = current.getRelative(BlockFace.UP).getRelative(horizontal);

                        if (isCompatibleLog(diagonal, logType) && visitedLogs.add(diagonal)) {
                            logQueue.add(diagonal);
                        }
                    }
                }

                if (isCompatibleLog(neighbor, logType) && visitedLogs.add(neighbor)) {
                    logQueue.add(neighbor);
                }

                // Collect leaves
                if (breakLeaves && BlockUtil.isLeaves(neighbor) && visitedLeaves.add(neighbor)) {
                    leavesQueue.add(neighbor);
                }
            }
        }

        // === BFS LEAVES DETECTION ===
        if (breakLeaves) {
            while (!leavesQueue.isEmpty()) {
                Block current = leavesQueue.poll();

                for (BlockFace face : LOG_SEARCH_FACES) {
                    Block neighbor = current.getRelative(face);

                    if (BlockUtil.isLeaves(neighbor) && visitedLeaves.add(neighbor)) {
                        if (isWithinLeavesDistance(neighbor, visitedLogs, 6)) {
                            leavesQueue.add(neighbor);
                        }
                    }
                }
            }
        }

        // === VALIDATION ===
        if (visitedLogs.size() < minSize) {
            plugin.debug("Tree too small: " + visitedLogs.size());
            return null;
        }

        if (!isNaturalTree(startBlock, visitedLogs)) {
            plugin.debug("Not a natural tree");
            return null;
        }

        // === SORTING ===
        List<Block> sortedLogs = new ArrayList<>(visitedLogs);
        sortedLogs.sort((a, b) -> Integer.compare(b.getY(), a.getY()));

        List<Block> sortedLeaves = new ArrayList<>(visitedLeaves);
        sortedLeaves.sort((a, b) -> Integer.compare(b.getY(), a.getY()));

        plugin.debug("Tree detected: " + sortedLogs.size() + " logs, " + sortedLeaves.size() + " leaves");

        return new TreeResult(sortedLogs, sortedLeaves, logType);
    }

    private boolean isCompatibleLog(Block block, Material originalType) {
        if (!BlockUtil.isLog(block)) return false;

        return getWoodType(originalType).equals(getWoodType(block.getType()));
    }

    private String getWoodType(Material material) {
        String name = material.name();

        if (name.startsWith("STRIPPED_")) {
            name = name.substring(9);
        }

        return name.replace("_LOG", "")
                   .replace("_WOOD", "")
                   .replace("_STEM", "")
                   .replace("_HYPHAE", "");
    }

    private boolean isWithinLeavesDistance(Block leaves, Set<Block> logs, int maxDistance) {
        for (Block log : logs) {
            if (leaves.getLocation().distance(log.getLocation()) <= maxDistance) {
                return true;
            }
        }
        return false;
    }

    private boolean isNaturalTree(Block startBlock, Set<Block> logs) {

        Block lowestLog = logs.stream()
                .min(Comparator.comparingInt(Block::getY))
                .orElse(startBlock);

        Block below = lowestLog.getRelative(BlockFace.DOWN);

        boolean hasGround = false;

        for (int i = 0; i < 3; i++) {
            if (BlockUtil.isValidTreeBase(below) || BlockUtil.isLog(below)) {
                hasGround = true;
                break;
            }

            if (below.getType() != Material.AIR) {
                hasGround = true;
                break;
            }

            below = below.getRelative(BlockFace.DOWN);
        }

        // Nether trees bypass
        String woodType = getWoodType(startBlock.getType());
        if (woodType.equals("CRIMSON") || woodType.equals("WARPED")) {
            hasGround = true;
        }

        return hasGround;
    }

    public record TreeResult(List<Block> logs, List<Block> leaves, Material logType) {
        public int totalSize() {
            return logs.size() + leaves.size();
        }
    }
}