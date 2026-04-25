package me.yansproject.aethertree.command;

import me.yansproject.aethertree.AetherTreeSystem;
import me.yansproject.aethertree.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AetherCommand implements CommandExecutor {

    private final AetherTreeSystem plugin;
    private final String version; // cache version (anti deprecated)

    public AetherCommand(AetherTreeSystem plugin) {
        this.plugin = plugin;
        this.version = plugin.getPluginMeta().getVersion(); // Paper modern way
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "help" -> showHelp(sender);
            case "toggle" -> handleToggle(sender);
            case "stats" -> handleStats(sender);
            case "reload" -> handleReload(sender);
            case "version", "info" -> showVersion(sender);
            default -> MessageUtil.send(sender, "&cUnknown command. Use &f/" + label + " help");
        }

        return true;
    }

    private void showHelp(CommandSender sender) {
        var config = plugin.getConfigManager();

        MessageUtil.sendRaw(sender, config.getMessage("help-header"));
        MessageUtil.sendRaw(sender, config.getMessage("help-toggle"));
        MessageUtil.sendRaw(sender, config.getMessage("help-stats"));

        if (sender.hasPermission("aethertree.admin")) {
            MessageUtil.sendRaw(sender, config.getMessage("help-reload"));
        }

        MessageUtil.sendRaw(sender, config.getMessage("help-footer"));
    }

    private void handleToggle(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            MessageUtil.send(sender, plugin.getConfigManager().getMessage("not-a-player"));
            return;
        }

        if (!player.hasPermission("aethertree.use")) {
            MessageUtil.send(sender, plugin.getConfigManager().getMessage("no-permission"));
            return;
        }

        boolean newState = plugin.togglePlayer(player.getUniqueId());

        if (newState) {
            MessageUtil.send(sender, plugin.getConfigManager().getMessage("toggle-enabled"));
        } else {
            MessageUtil.send(sender, plugin.getConfigManager().getMessage("toggle-disabled"));
        }
    }

    private void handleStats(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            MessageUtil.send(sender, plugin.getConfigManager().getMessage("not-a-player"));
            return;
        }

        var config = plugin.getConfigManager();
        int[] stats = plugin.getPlayerStats(player.getUniqueId());

        MessageUtil.sendRaw(sender, config.getMessage("stats-header"));
        MessageUtil.sendRaw(sender, config.getMessage("stats-trees")
                .replace("{count}", String.valueOf(stats[0])));
        MessageUtil.sendRaw(sender, config.getMessage("stats-logs")
                .replace("{count}", String.valueOf(stats[1])));
        MessageUtil.sendRaw(sender, config.getMessage("stats-footer"));
    }

    private void handleReload(CommandSender sender) {
        if (!sender.hasPermission("aethertree.admin")) {
            MessageUtil.send(sender, plugin.getConfigManager().getMessage("no-permission"));
            return;
        }

        plugin.reload();
        MessageUtil.send(sender, plugin.getConfigManager().getMessage("config-reloaded"));
    }

    private void showVersion(CommandSender sender) {
        MessageUtil.sendRaw(sender, "");
        MessageUtil.sendRaw(sender, "&a&lAetherTreeSystem &7v" + version);
        MessageUtil.sendRaw(sender, "&7Created by &fYansProject");
        MessageUtil.sendRaw(sender, "&7Running on &f" + plugin.getServer().getBukkitVersion());
        MessageUtil.sendRaw(sender, "");
    }
}