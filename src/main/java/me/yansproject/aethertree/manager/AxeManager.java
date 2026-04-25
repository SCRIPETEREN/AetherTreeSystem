package me.yansproject.aethertree.manager;

import me.yansproject.aethertree.AetherTreeSystem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

public class AxeManager {

    private final AetherTreeSystem plugin;
    private final Random random = new Random();
    
    // All axe materials
    private static final Set<Material> AXE_MATERIALS = EnumSet.of(
        Material.WOODEN_AXE,
        Material.STONE_AXE,
        Material.IRON_AXE,
        Material.GOLDEN_AXE,
        Material.DIAMOND_AXE,
        Material.NETHERITE_AXE
    );

    public AxeManager(AetherTreeSystem plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        // Reload any cached values if needed
    }

    /**
     * Check if an item is an axe
     */
    public boolean isAxe(ItemStack item) {
        if (item == null) return false;
        
        Material type = item.getType();
        
        // Check vanilla axes
        if (AXE_MATERIALS.contains(type)) {
            return true;
        }
        
        // Check for modded/future axes by name pattern
        return type.name().endsWith("_AXE");
    }

    /**
     * Get the speed multiplier for an axe
     */
    public double getSpeedMultiplier(ItemStack axe) {
        if (axe == null) return 1.0;
        
        var config = plugin.getConfigManager();
        
        // Get base multiplier from config
        double baseMultiplier = config.getAxeSpeedMultiplier(axe.getType().name());
        
        // Add efficiency bonus
        int efficiencyLevel = axe.getEnchantmentLevel(Enchantment.EFFICIENCY);
        double efficiencyBonus = efficiencyLevel * config.getEfficiencyBonusPerLevel();
        
        return baseMultiplier + efficiencyBonus;
    }

    /**
     * Calculate progress per tick for a given axe and tree size
     */
    public double calculateProgressPerTick(ItemStack axe, int treeSize) {
    var config = plugin.getConfigManager();

    // Base progress dari config
    double baseProgress = config.getBaseProgressPerTick();

    // Multiplier dari axe + enchant
    double speedMultiplier = getSpeedMultiplier(axe);

    // Scaling berdasarkan ukuran tree (biar ga OP di tree besar)
    double sizeScaling = 1.0 + (Math.log(treeSize + 1) * 0.1);

    // Final progress per tick
    double finalProgress = (baseProgress * speedMultiplier) / sizeScaling;

    return finalProgress;
}

    /**
     * Consume durability from the axe
     * Returns true if the axe broke
     */
    public boolean consumeDurability(Player player, ItemStack axe, int amount) {
        if (axe == null || !isAxe(axe)) return false;
        
        var config = plugin.getConfigManager();
        
        if (!config.shouldConsumeDurability()) return false;
        if (player.hasPermission("aethertree.bypass.durability")) return false;
        
        if (!(axe.getItemMeta() instanceof Damageable damageable)) return false;
        
        int durabilityToConsume = amount * config.getDurabilityPerLog();
        
        // Apply Unbreaking enchantment
        if (config.respectUnbreaking()) {
            int unbreakingLevel = axe.getEnchantmentLevel(Enchantment.UNBREAKING);
            if (unbreakingLevel > 0) {
                // Unbreaking has (100 / (level + 1))% chance to consume durability
                int actualConsume = 0;
                for (int i = 0; i < durabilityToConsume; i++) {
                    if (random.nextInt(unbreakingLevel + 1) == 0) {
                        actualConsume++;
                    }
                }
                durabilityToConsume = actualConsume;
            }
        }
        
        if (durabilityToConsume <= 0) return false;
        
        int currentDamage = damageable.getDamage();
        int maxDurability = axe.getType().getMaxDurability();
        int newDamage = currentDamage + durabilityToConsume;
        
        if (newDamage >= maxDurability) {
            // Axe breaks
            player.getInventory().setItemInMainHand(null);
            return true;
        }
        
        damageable.setDamage(newDamage);
        axe.setItemMeta(damageable);
        
        return false;
    }

    /**
     * Get all axe materials
     */
    public Set<Material> getAllAxeMaterials() {
        return EnumSet.copyOf(AXE_MATERIALS);
    }
}
