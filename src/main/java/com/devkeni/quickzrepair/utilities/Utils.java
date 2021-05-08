package com.devkeni.quickzrepair.utilities;

import com.devkeni.quickzrepair.managers.ConfigManager;
import lombok.Getter;
import org.bukkit.plugin.Plugin;

@Getter
public class Utils {

    private final Plugin plugin;
    private final ConfigManager configManager;

    public Utils(Plugin plugin) {
        this.plugin = plugin;
        configManager = new ConfigManager(this);
    }

}
