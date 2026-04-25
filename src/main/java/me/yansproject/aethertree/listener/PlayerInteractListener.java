package me.yansproject.aethertree.listener;

import me.yansproject.aethertree.AetherTreeSystem;
import me.yansproject.aethertree.util.BlockUtil;
import me.yansproject.aethertree.util.MessageUtil;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    private final AetherTreeSystem plugin;

    public PlayerInteractListener(AetherTreeSystem plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Only handle main hand
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        Action action = event.getAction();
        Block block = event.getClickedBlock();
        
        var config = plugin.getConfigManager();

        // Check if plugin is enabled
        if (!config.isEnabled()) return;

        // Check interaction type
        String interactionType = config.getInteractionType();
        boolean validAction = false;

        if (interactionType.equalsIgnoreCase("RIGHT_CLICK")) {
            validAction = (action == Action.RIGHT_CLICK_BLOCK);
        } else if (interactionType.equalsIgnoreCase("SNEAK_RIGHT_CLICK")) {
            validAction = (action == Action.RIGHT_CLICK_BLOCK && player.isSneaking());
        }

        if (!validAction) return;

        // Check if clicking a log
        if (block == null || !BlockUtil.isLog(block)) return;

        // Check permission
        if (!player.hasPermission("aethertree.use")) {
            MessageUtil.send(player, config.getMessage("no-permission"));
            return;
        }

        // Check player toggle
        if (!plugin.isPlayerEnabled(player.getUniqueId())) {
            return; // Silently ignore if player disabled
        }

        // Check world
        if (!config.isWorldAllowed(player.getWorld().getName())) {
            return;
        }

        // Check axe
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (!plugin.getAxeManager().isAxe(mainHand)) {
            return;
        }

        // Handle the chopping
        plugin.getChoppingManager().handleInteraction(player, block, mainHand);
        
        // Don't cancel the event to allow normal interaction sounds
    }
}
