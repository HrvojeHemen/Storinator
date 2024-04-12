package storinator.storinator;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;


public final class Storinator extends JavaPlugin {
    private final Logger logger;
    public Storinator(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

       logger.info("Hello world");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        logger.info("Goodbye cruel world.");
    }
}
