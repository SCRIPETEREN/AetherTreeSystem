package me.yansproject.aethertree.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

public final class ActionBarUtil {

    private ActionBarUtil() {
        // Utility class
    }

    /**
     * Send an action bar message to a player (MODERN - Adventure API)
     */
    public static void send(Player player, String message) {
        if (player == null || message == null) return;

        Component component = LegacyComponentSerializer.legacyAmpersand()
                .deserialize(message);

        player.sendActionBar(component);
    }

    /**
     * Clear the action bar
     */
    public static void clear(Player player) {
        send(player, "");
    }

    /**
     * Build a progress bar string
     */
    public static String buildProgressBar(double progress, int length,
                                          String filledChar, String emptyChar,
                                          String filledColor, String emptyColor) {

        progress = Math.max(0, Math.min(1, progress)); // Clamp 0-1

        int filled = (int) Math.round(progress * length);
        int empty = length - filled;

        StringBuilder bar = new StringBuilder();
        bar.append(filledColor);
        bar.append(filledChar.repeat(Math.max(0, filled)));
        bar.append(emptyColor);
        bar.append(emptyChar.repeat(Math.max(0, empty)));

        return bar.toString();
    }

    /**
     * Build a modern animated progress bar
     */
    public static String buildModernBar(double progress, int length) {
        progress = Math.max(0, Math.min(1, progress));

        int filled = (int) Math.round(progress * length);
        int empty = length - filled;

        StringBuilder bar = new StringBuilder();

        // Dynamic color based on progress
        String progressColor;
        if (progress < 0.33) {
            progressColor = "&c"; // Red
        } else if (progress < 0.66) {
            progressColor = "&e"; // Yellow
        } else {
            progressColor = "&a"; // Green
        }

        bar.append(progressColor);
        bar.append("█".repeat(Math.max(0, filled)));
        bar.append("&8");
        bar.append("░".repeat(Math.max(0, empty)));

        return bar.toString();
    }

    /**
     * Build progress bar based on style
     */
    public static String buildStyledBar(double progress, int length, String style,
                                        String filledChar, String emptyChar,
                                        String filledColor, String emptyColor) {

        return switch (style.toUpperCase()) {
            case "MODERN" -> buildModernBar(progress, length);
            case "CLASSIC" -> buildProgressBar(progress, length, "=", "-", "&a", "&7");
            case "MINIMAL" -> buildProgressBar(progress, length, "●", "○", "&a", "&8");
            case "BLOCKS" -> buildProgressBar(progress, length, "■", "□", "&a", "&7");
            case "ARROWS" -> buildProgressBar(progress, length, "▸", "▹", "&a", "&8");
            default -> buildProgressBar(progress, length, filledChar, emptyChar, filledColor, emptyColor);
        };
    }
}