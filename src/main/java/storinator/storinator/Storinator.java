package storinator.storinator;

import org.bukkit.plugin.java.JavaPlugin;
import storinator.storinator.data.ItemStorage;
import storinator.storinator.data.ItemStorageLoader;
import storinator.storinator.data.ItemStorageSaver;
import storinator.storinator.ui.StorageUI;

import java.util.Map;
import java.util.Objects;


public final class Storinator extends JavaPlugin {

    Map<String, ItemStorage> itemStorages;

    @Override
    public void onEnable() {
        // Plugin startup logic

        Objects.requireNonNull(getCommand("inventory")).setExecutor(new StorageUI());
        getLogger().info("Loading storages");
        this.itemStorages = ItemStorageLoader.loadExistingStorages(this.getDataFolder());
        getLogger().info("Loaded storages");

    }

    @Override
    public void onDisable() {
        getLogger().info("Saving storages");
        ItemStorageSaver.saveStorages(itemStorages, this.getDataFolder());
        getLogger().info("Saved storages");
    }
}
