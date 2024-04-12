package storinator.storinator;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public final class Storinator extends JavaPlugin {


    @Override
    public void onEnable() {
        // Plugin startup logic

        Bukkit.getLogger().info("Storinator is enabled");

    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Storinator is disabled");
    }
}
