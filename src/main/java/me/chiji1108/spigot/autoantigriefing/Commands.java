package me.chiji1108.spigot.autoantigriefing;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Commands implements Listener,CommandExecutor {

    public String cmd1 = "gp";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (sender instanceof Player) {
            if (command.getName().equalsIgnoreCase(cmd1)) {
                MySQL mysql= new MySQL();
                mysql.checkGP(player);
                return true;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }
        return false;
    }
}
