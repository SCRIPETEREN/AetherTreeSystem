package me.yansproject.aethertree.util;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.EnumSet;
import java.util.Set;

public final class BlockUtil {

    // All log types - dynamically supports any _LOG or _STEM material
    private static final Set<Material> LOG_MATERIALS = EnumSet.noneOf(Material.class);
    private static final Set<Material> LEAVES_MATERIALS = EnumSet.noneOf(Material.class);
    private static final Set<Material> SAPLING_MATERIALS = EnumSet.noneOf(Material.class);
    
    static {
        // Dynamically populate materials for future-proofing
        for (Material material : Material.values()) {
            String name = material.name();
            
            // Logs (includes stems for nether trees)
            if ((name.endsWith("_LOG") || name.endsWith("_STEM")) && !name.contains("STRIPPED")) {
                LOG_MATERIALS.add(material);
            }
            // Stripped logs
            if ((name.startsWith("STRIPPED_") && (name.endsWith("_LOG") || name.endsWith("_STEM")))) {
                LOG_MATERIALS.add(material);
            }
            // Wood blocks (full bark logs)
            if (name.endsWith("_WOOD") || name.endsWith("_HYPHAE")) {
                LOG_MATERIALS.add(material);
            }
            
            // Leaves
            if (name.endsWith("_LEAVES")) {
                LEAVES_MATERIALS.add(material);
            }
            // Nether "leaves" equivalents
            if (name.equals("NETHER_WART_BLOCK") || name.equals("WARPED_WART_BLOCK") || 
                name.equals("SHROOMLIGHT")) {
                LEAVES_MATERIALS.add(material);
            }
            
            // Saplings
            if (name.endsWith("_SAPLING") || name.equals("CRIMSON_FUNGUS") || 
                name.equals("WARPED_FUNGUS")) {
                SAPLING_MATERIALS.add(material);
            }
        }
    }

    private BlockUtil() {
        // Utility class
    }

    /**
     * Check if a block is any type of log
     */
    public static boolean isLog(Block block) {
        return block != null && LOG_MATERIALS.contains(block.getType());
    }

    /**
     * Check if a material is any type of log
     */
    public static boolean isLog(Material material) {
        return material != null && LOG_MATERIALS.contains(material);
    }

    /**
     * Check if a block is any type of leaves
     */
    public static boolean isLeaves(Block block) {
        return block != null && LEAVES_MATERIALS.contains(block.getType());
    }

    /**
     * Check if a material is any type of leaves
     */
    public static boolean isLeaves(Material material) {
        return material != null && LEAVES_MATERIALS.contains(material);
    }

    /**
     * Get the corresponding sapling for a log type
     */
    public static Material getSaplingFor(Material logType) {
        if (logType == null) return null;
        
        String name = logType.name();
        
        // Handle stripped logs
        if (name.startsWith("STRIPPED_")) {
            name = name.substring(9); // Remove "STRIPPED_"
        }
        
        // Handle wood blocks
        if (name.endsWith("_WOOD")) {
            name = name.replace("_WOOD", "_LOG");
        }
        if (name.endsWith("_HYPHAE")) {
            name = name.replace("_HYPHAE", "_STEM");
        }
        
        // Map log types to saplings
        return switch (name) {
            case "OAK_LOG" -> Material.OAK_SAPLING;
            case "SPRUCE_LOG" -> Material.SPRUCE_SAPLING;
            case "BIRCH_LOG" -> Material.BIRCH_SAPLING;
            case "JUNGLE_LOG" -> Material.JUNGLE_SAPLING;
            case "ACACIA_LOG" -> Material.ACACIA_SAPLING;
            case "DARK_OAK_LOG" -> Material.DARK_OAK_SAPLING;
            case "MANGROVE_LOG" -> Material.MANGROVE_PROPAGULE;
            case "CHERRY_LOG" -> Material.CHERRY_SAPLING;
            case "CRIMSON_STEM" -> Material.CRIMSON_FUNGUS;
            case "WARPED_STEM" -> Material.WARPED_FUNGUS;
            default -> {
                // Try to find matching sapling dynamically
                String saplingName = name.replace("_LOG", "_SAPLING")
                                        .replace("_STEM", "_FUNGUS");
                try {
                    yield Material.valueOf(saplingName);
                } catch (IllegalArgumentException e) {
                    yield null;
                }
            }
        };
    }

    /**
     * Check if the block below is a valid tree base (dirt, grass, etc.)
     */
    public static boolean isValidTreeBase(Block block) {
        if (block == null) return false;
        
        Material type = block.getType();
        return type == Material.DIRT ||
               type == Material.GRASS_BLOCK ||
               type == Material.PODZOL ||
               type == Material.MYCELIUM ||
               type == Material.ROOTED_DIRT ||
               type == Material.MOSS_BLOCK ||
               type == Material.MUD ||
               type == Material.MUDDY_MANGROVE_ROOTS ||
               type == Material.CRIMSON_NYLIUM ||
               type == Material.WARPED_NYLIUM ||
               type == Material.SOUL_SOIL ||
               type == Material.NETHERRACK;
    }

    /**
     * Get all registered log materials
     */
    public static Set<Material> getAllLogMaterials() {
        return EnumSet.copyOf(LOG_MATERIALS);
    }

    /**
     * Get all registered leaves materials
     */
    public static Set<Material> getAllLeavesMaterials() {
        return EnumSet.copyOf(LEAVES_MATERIALS);
    }
}
