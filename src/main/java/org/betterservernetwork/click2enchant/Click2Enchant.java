package org.betterservernetwork.click2enchant;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Click2Enchant extends JavaPlugin {
    DisableCommandHandler command1;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new ClickHandler(), this);

        command1 = new DisableCommandHandler();
        RenameCommandHandler command2 = new RenameCommandHandler();
        GlobalEnchantCommand command3 = new GlobalEnchantCommand();
        GlobalRenameCommand command4 = new GlobalRenameCommand();

        Objects.requireNonNull(getCommand("toggleenchant")).setExecutor(command1);
        Objects.requireNonNull(getCommand("toggleenchant")).setTabCompleter(command1);
        Objects.requireNonNull(getCommand("toggleglobalenchant")).setExecutor(command3);
        Objects.requireNonNull(getCommand("toggleglobalenchant")).setTabCompleter(command3);
        Objects.requireNonNull(getCommand("toggleglobalrename")).setExecutor(command4);
        Objects.requireNonNull(getCommand("toggleglobalrename")).setTabCompleter(command4);
        Objects.requireNonNull(getCommand("rename")).setExecutor(command2);
        Objects.requireNonNull(getCommand("rename")).setTabCompleter(command2);

        System.out.println("Enabled.");
    }

    @Override
    public void onDisable() {
        command1.onDisable();
    }
}
