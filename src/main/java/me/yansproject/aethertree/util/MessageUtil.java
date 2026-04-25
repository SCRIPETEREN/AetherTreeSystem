package me.yansproject.aethertree.util;

import me.yansproject.aethertree.AetherTreeSystem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MessageUtil {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    private MessageUtil() {}

    /**
     * Convert legacy (& + hex) → Adventure Component
     */
    public static Component toComponent(String message) {
        if (message == null || message.isEmpty()) return Component.empty();

        // Convert HEX (&#FFFFFF → §x§F§F...)
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuilder buffer = new StringBuilder();

        while (matcher.find()) {
            String hex = matcher.group(1);
            StringBuilder replacement = new StringBuilder("§x");

            for (char c : hex.toCharArray()) {
                replacement.append('§').append(c);
            }

            matcher.appendReplacement(buffer, replacement.toString());
        }
        matcher.appendTail(buffer);

        return LegacyComponentSerializer.legacySection().deserialize(buffer.toString().replace('&', '§'));
    }

    /**
     * Send message with prefix
     */
    public static void send(CommandSender sender, String message) {
        if (sender == null || message == null) return;

        AetherTreeSystem plugin = AetherTreeSystem.getInstance();
        String prefix = plugin != null ? plugin.getConfigManager().getPrefix() : "";

        sender.sendMessage(toComponent(prefix + message));
    }

    /**
     * Send raw message
     */
    public static void sendRaw(CommandSender sender, String message) {
        if (sender == null || message == null) return;

        sender.sendMessage(toComponent(message));
    }

    /**
     * Modern Title (NO deprecated)
     */
    public static void sendTitle(Player player, String title, String subtitle,
                                 int fadeIn, int stay, int fadeOut) {
        if (player == null) return;

        Title.Times times = Title.Times.times(
                Duration.ofMillis(fadeIn * 50L),
                Duration.ofMillis(stay * 50L),
                Duration.ofMillis(fadeOut * 50L)
        );

        Title t = Title.title(
                toComponent(title),
                toComponent(subtitle),
                times
        );

        player.showTitle(t);
    }

    /**
     * Replace placeholders
     */
    public static String replacePlaceholders(String message, Object... replacements) {
        if (message == null) return "";

        for (int i = 0; i < replacements.length - 1; i += 2) {
            message = message.replace(
                    String.valueOf(replacements[i]),
                    String.valueOf(replacements[i + 1])
            );
        }

        return message;
    }
}