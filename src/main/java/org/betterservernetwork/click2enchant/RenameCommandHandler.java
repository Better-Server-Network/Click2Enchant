package org.betterservernetwork.click2enchant;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RenameCommandHandler implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(
            @NotNull CommandSender commandSender,
            @NotNull Command command, @NotNull String s,
            @NotNull String[] arguments) {
        if (!(commandSender instanceof Player)) {
            return true;
        }

        Player player = (Player) commandSender;

        if (DisableCommandHandler.instance.renameDisabled) {
            player.sendMessage(ChatColor.RED + "Rename disabled.");
            return true;
        } else if (arguments.length < 1) {
            player.sendMessage(ChatColor.RED + "Correct usage:" +
                    ChatColor.RESET + " /rename <name>");
            return true;
        } else if (player.getInventory().getItemInMainHand().getType().isAir()) {
            player.sendMessage(ChatColor.RED + "You need to hold an item to rename.");
            return true;
        }

        StringBuilder name = new StringBuilder();
        for (String arg : arguments) {
            name.append(name.toString().isEmpty() ? "" : " ").append(arg);
        }

        ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
        meta.setDisplayName(name.toString());
        player.getInventory().getItemInMainHand().setItemMeta(meta);

        player.sendMessage("Item renamed.");

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender commandSender,
            @NotNull Command command,
            @NotNull String s,
            @NotNull String[] strings) {
        return new ArrayList<>();
    }
}
