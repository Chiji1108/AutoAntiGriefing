package me.chiji1108.spigot.autoantigriefing;

import org.bukkit.plugin.java.JavaPlugin;

public final class AutoAntiGriefing extends JavaPlugin {

    private Commands commands = new Commands();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand(commands.cmd1).setExecutor(commands);
        loadConfig();

        MySQL mysql = new MySQL();
        mysql.runnable();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
}
