package com.devkeni.quickzrepair.commands;

import com.devkeni.quickzrepair.managers.ConfigManager;
import com.devkeni.quickzrepair.utilities.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FixCommand implements CommandExecutor {

    private final Economy economy;
    private final double fixCost;
    private final double fixAllCost;
    private final String fixPermission;
    private final String fixAllPermission;
    private final String allItemsFixed;
    private final String alreadyFixed;
    private final String playersOnly;
    private final String invalidItem;
    private final String fixSuccessful;
    private final String invalidUsage;
    private final String inventoryEmpty;
    private final String fixAllSuccessful;
    private final String insufficientMoney;
    private final String insufficientPermission;

    public FixCommand(Utils utils, Economy economy) {
        this.economy = economy;
        ConfigManager configManager = utils.getConfigManager();
        fixCost = (double) configManager.getValue("fix-cost");
        fixAllCost = (double) configManager.getValue("fixall-cost");
        invalidItem = configManager.getMessage("invalid-item");
        playersOnly = configManager.getMessage("players-only");
        invalidUsage = configManager.getMessage("invalid-usage");
        alreadyFixed = configManager.getMessage("already-fixed");
        fixSuccessful = configManager.getMessage("successful-fix");
        allItemsFixed = configManager.getMessage("all-items-fixed");
        inventoryEmpty = configManager.getMessage("inventory-empty");
        fixPermission = configManager.getPermission("permission-fix");
        fixAllSuccessful = configManager.getMessage("successful-fixall");
        insufficientMoney = configManager.getMessage("insufficient-money");
        fixAllPermission = configManager.getPermission("permission-fixall");
        insufficientPermission = configManager.getMessage("insufficient-permission");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(playersOnly);
            return true;
        }
        Player player = (Player) sender;

        //Fix All
        if (args.length > 0 && args[0].equalsIgnoreCase("all")) {
            if (player.hasPermission(fixAllPermission)) {
                if (economy.getBalance(player) <= fixAllCost) {
                    player.sendMessage(insufficientMoney.replace("{cost}", String.valueOf(fixAllCost)));
                } else {
                    fixPlayerInventory(player);
                }
            } else {
                player.sendMessage(insufficientPermission);
            }
            return true;
        }

        //Fix
        if (args.length < 1) {
            if (player.hasPermission(fixPermission)) {
                if (economy.getBalance(player) <= fixCost) {
                    player.sendMessage(insufficientMoney.replace("{cost}", String.valueOf(fixCost)));
                } else {
                    fixPlayerItemInHand(player);
                }
            } else {
                player.sendMessage(insufficientPermission);
            }
            return true;
        }

        //Invalid usage
        player.sendMessage(invalidUsage);
        return true;
    }

    private void fixPlayerInventory(Player player) {
        if (inventoryIsEmpty(player)) {
            player.sendMessage(inventoryEmpty);
            return;
        }
        int items = 0;
        int slots = player.getInventory().getContents().length;

        //Fix Inventory
        for (int i = 0; i < slots; i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null && !(item.getType().equals(Material.AIR))) {
                if (item.getDurability() != 0) {

                    item.setDurability((short) 0);
                    items++;
                }
            }
        }

        //Fix player armors
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null && !(armor.getType().equals(Material.AIR))) {
                if (armor.getDurability() != 0) {

                    armor.setDurability((short) 0);
                    items++;
                }
            }
        }

        if (items < 1) {
            player.sendMessage(allItemsFixed);
            return;
        }
        economy.withdrawPlayer(player, fixAllCost);
        player.sendMessage(fixAllSuccessful.replace("{amount}", String.valueOf(items)));
    }

    private void fixPlayerItemInHand(Player player) {
        if (player.getItemInHand() == null
                || player.getItemInHand().getType().equals(Material.AIR)) {
            player.sendMessage(invalidItem);
            return;
        }
        if (player.getItemInHand().getDurability() == 0) {
            player.sendMessage(alreadyFixed);
            return;
        }
        player.getItemInHand().setDurability((short) 0);
        economy.withdrawPlayer(player, fixCost);
        player.sendMessage(fixSuccessful);
    }

    private boolean inventoryIsEmpty(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) return false;
        }
        return true;
    }

}