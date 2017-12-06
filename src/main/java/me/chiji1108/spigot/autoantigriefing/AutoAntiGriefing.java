package me.chiji1108.spigot.autoantigriefing;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class AutoAntiGriefing extends JavaPlugin {

    public static String prefix = ChatColor.GRAY + "[" + ChatColor.BOLD + "" + ChatColor.RED + "AAG" + ChatColor.RESET + "" + ChatColor.GRAY + "] " + ChatColor.GREEN + "";
    private String version = "1.1";

    MySQL mysql = new MySQL();

    @Override
    public void onEnable() {
        loadConfig();
        mysql.openConnection();
        mysql.createTables();
        getCommand(mysql.cmd1).setExecutor(mysql);
        mysql.runnable();
        getServer().getConsoleSender().sendMessage(prefix + "AutoAntiGriefing ver." + version + "は有効になりました");

    }

    @Override
    public void onDisable() {
        mysql.closeConnection();
        getServer().getConsoleSender().sendMessage(prefix + "AutoAntiGriefing ver." + version + "は無効になりました");
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
}
