package org.betterservernetwork.click2enchant;

import org.betterservernetwork.click2enchant.tools.Tools;
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

public class GlobalRenameCommand implements CommandExecutor, TabCompleter {
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
                    ChatColor.RESET + " /toggleglobalrename");
            return true;
        }

        DisableCommandHandler.instance.renameDisabled = !DisableCommandHandler.instance.renameDisabled;

        if (DisableCommandHandler.instance.renameDisabled) {
            player.sendMessage("Rename Globally Disabled.");
        } else {
            player.sendMessage("Rename Globally Enabled.");
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
