package com.devkeni.quickzrepair.managers;

import com.devkeni.quickzrepair.utilities.Utils;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final FileConfiguration config;

    public ConfigManager(Utils utils) {
        config = utils.getPlugin().getConfig();
    }

    public Object getValue(String value) {
        return config.get("config." + value);
    }

    public String getPermission(String permission) {
        return config.getString("config." + permission);
    }

    public String getMessage(String message) {
        return config.getString("messages." + message).replace("&", "§");
    }

}
