package me.yansproject.aethertree.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AetherTabCompleter implements TabCompleter {

    private static final List<String> SUBCOMMANDS = Arrays.asList(
        "help", "toggle", "stats", "reload", "version"
    );

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, 
            String alias, String[] args) {
        
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String input = args[0].toLowerCase();
            
            for (String sub : SUBCOMMANDS) {
                if (sub.startsWith(input)) {
                    // Check permission for reload
                    if (sub.equals("reload") && !sender.hasPermission("aethertree.admin")) {
                        continue;
                    }
                    completions.add(sub);
                }
            }
        }

        return completions;
    }
}
