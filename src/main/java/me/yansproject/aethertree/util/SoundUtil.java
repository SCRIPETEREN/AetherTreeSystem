package me.yansproject.aethertree.util;

import me.yansproject.aethertree.AetherTreeSystem;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public final class SoundUtil {

    private SoundUtil() {
        // Utility class
    }

    /**
     * Play a sound to a player
     */
    public static void playSound(Player player, String soundName, float volume, float pitch) {
        if (player == null || soundName == null) return;
        
        AetherTreeSystem plugin = AetherTreeSystem.getInstance();
        if (!plugin.getConfigManager().isSoundEnabled()) return;
        
        try {
            Sound sound = Sound.valueOf(soundName.toUpperCase().replace(".", "_"));
            player.playSound(player.getLocation(), sound, volume, pitch);
        } catch (IllegalArgumentException e) {
            plugin.debug("Invalid sound: " + soundName);
        }
    }

    /**
     * Play a sound at a location
     */
    public static void playSoundAt(Location location, String soundName, float volume, float pitch) {
        if (location == null || location.getWorld() == null || soundName == null) return;
        
        AetherTreeSystem plugin = AetherTreeSystem.getInstance();
        if (!plugin.getConfigManager().isSoundEnabled()) return;
        
        try {
            Sound sound = Sound.valueOf(soundName.toUpperCase().replace(".", "_"));
            location.getWorld().playSound(location, sound, volume, pitch);
        } catch (IllegalArgumentException e) {
            plugin.debug("Invalid sound: " + soundName);
        }
    }

    /**
     * Spawn particles at a location
     */
    public static void spawnParticles(Location location, String particleName, int count) {
        if (location == null || location.getWorld() == null || particleName == null) return;
        
        AetherTreeSystem plugin = AetherTreeSystem.getInstance();
        if (!plugin.getConfigManager().isParticleEnabled()) return;
        
        try {
            Particle particle = Particle.valueOf(particleName.toUpperCase().replace(".", "_"));
            location.getWorld().spawnParticle(particle, location.clone().add(0.5, 0.5, 0.5), 
                count, 0.3, 0.3, 0.3, 0.05);
        } catch (IllegalArgumentException e) {
            plugin.debug("Invalid particle: " + particleName);
        }
    }

    /**
     * Play chopping sound
     */
    public static void playChoppingSound(Player player) {
        AetherTreeSystem plugin = AetherTreeSystem.getInstance();
        var config = plugin.getConfigManager();
        
        playSound(player, 
            config.getChoppingSound(),
            config.getChoppingSoundVolume(),
            config.getChoppingSoundPitch()
        );
    }

    /**
     * Play complete sound
     */
    public static void playCompleteSound(Player player) {
        AetherTreeSystem plugin = AetherTreeSystem.getInstance();
        var config = plugin.getConfigManager();
        
        playSound(player,
            config.getCompleteSound(),
            config.getCompleteSoundVolume(),
            config.getCompleteSoundPitch()
        );
    }

    /**
     * Play cancel sound
     */
    public static void playCancelSound(Player player) {
        AetherTreeSystem plugin = AetherTreeSystem.getInstance();
        var config = plugin.getConfigManager();
        
        playSound(player,
            config.getCancelSound(),
            config.getCancelSoundVolume(),
            config.getCancelSoundPitch()
        );
    }

    /**
     * [NEW v2.0] Play inventory locked sound
     */
    public static void playInventoryLockedSound(Player player) {
        AetherTreeSystem plugin = AetherTreeSystem.getInstance();
        var config = plugin.getConfigManager();
        
        playSound(player,
            config.getInventoryLockedSound(),
            config.getInventoryLockedSoundVolume(),
            config.getInventoryLockedSoundPitch()
        );
    }

    /**
     * [NEW v2.0] Play hungry sound
     */
    public static void playHungrySound(Player player) {
        AetherTreeSystem plugin = AetherTreeSystem.getInstance();
        var config = plugin.getConfigManager();
        
        playSound(player,
            config.getHungrySound(),
            config.getHungrySoundVolume(),
            config.getHungrySoundPitch()
        );
    }
}
