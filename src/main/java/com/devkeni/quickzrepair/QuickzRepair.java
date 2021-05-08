package com.devkeni.quickzrepair;

import com.devkeni.quickzrepair.commands.FixCommand;
import com.devkeni.quickzrepair.utilities.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class QuickzRepair extends JavaPlugin {

    private Utils utils;
    private Economy economy;
    private boolean vaultHooked = false;

    @Override
    public void onEnable() {
        loadConfig();
        hookVault();

        if (vaultHooked) {
            getLogger().info("Vault hooked.");
        } else {
            getLogger().severe("Vault not found.");
            setEnabled(false);
        }

        utils = new Utils(this);
        registerCommands();
    }

    private void loadConfig() {
        saveDefaultConfig();
        reloadConfig();
    }

    private void hookVault() {
        RegisteredServiceProvider<Economy> economyProvider =
                getServer().getServicesManager()
                        .getRegistration(net.milkbowl.vault.economy.Economy.class);

            if (economyProvider != null) {
                economy = economyProvider.getProvider();
            }
            if (economy != null) {
                vaultHooked = true;
            }
    }

    private void registerCommands() {
        getCommand("fix").setExecutor(new FixCommand(utils, economy));
    }

}
