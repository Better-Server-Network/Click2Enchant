package org.betterservernetwork.click2enchant;

import org.betterservernetwork.click2enchant.tools.Tools;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DisableCommandHandler implements CommandExecutor, TabCompleter {
    public static DisableCommandHandler instance;

    public boolean enchantDisabled;
    public boolean renameDisabled;

    private final ArrayList<String> disabled = new ArrayList<>();

    public DisableCommandHandler() {
        instance = this;
        loadData();
    }

    public void onDisable() {
        File file = new File("plugins/ClickToEnchant/disabled.yaml");
        YamlConfiguration config = new YamlConfiguration();

        config.set("enchantDisabled", enchantDisabled);
        config.set("renameDisabled", renameDisabled);

        for (String id : disabled) {
            config.set("players." + id, true);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Saved Click To Enchant data.");
    }

    public void loadData() {
        File file;

        try {
            file = new File("plugins/ClickToEnchant/disabled.yaml");
        } catch (Exception e){
            System.out.println(
                    "Error loading data file (if this is your first time using the plugin then ignore this).");

            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        if (config.get("enchantDisabled") != null) enchantDisabled = (boolean) config.get("enchantDisabled");
        if (config.get("renameDisabled") != null) renameDisabled = (boolean) config.get("renameDisabled");

        if (config.getConfigurationSection("players") != null) {
            disabled.addAll(Objects.requireNonNull(config.getConfigurationSection("players")).getKeys(false));
        }

        System.out.println("Loaded Click To Enchant data.");
    }

    public boolean isDisabled(Player player) {
        return enchantDisabled || disabled.contains(player.getUniqueId().toString());
    }

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
                    ChatColor.RESET + " /toggleenchant");
            return true;
        }

        if (disabled.remove(player.getUniqueId().toString())) {
            player.sendMessage("Enchant Enabled.");
        } else {
            player.sendMessage("Enchant Disabled.");
            disabled.add(player.getUniqueId().toString());
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
