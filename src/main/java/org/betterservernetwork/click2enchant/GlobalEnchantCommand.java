package org.betterservernetwork.click2enchant;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GlobalEnchantCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(
            @NotNull CommandSender commandSender,
            @NotNull Command command, @NotNull String s,
            @NotNull String[] arguments) {
        if (!(commandSender instanceof Player)) {
            return true;
        }

        Player player = (Player) commandSender;

        if (arguments.length > 0) {
            player.sendMessage(ChatColor.RED + "Correct usage:" +
                    ChatColor.RESET + " /toggleglobalenchant");
            return true;
        }

        DisableCommandHandler.instance.enchantDisabled = !DisableCommandHandler.instance.enchantDisabled;

        if (DisableCommandHandler.instance.enchantDisabled) {
            player.sendMessage("Enchant Globally Disabled.");
        } else {
            player.sendMessage("Enchant Globally Enabled.");
        }

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
