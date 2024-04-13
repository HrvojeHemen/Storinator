package storinator.storinator;

import org.bukkit.plugin.java.JavaPlugin;
import storinator.storinator.data.ItemStorage;
import storinator.storinator.data.ItemStorageLoader;
import storinator.storinator.data.ItemStorageSaver;
import storinator.storinator.ui.StorageUI;

import java.util.Map;
import java.util.Objects;


public final class Storinator extends JavaPlugin {

    public static Map<String, ItemStorage> itemStorages;
    private ItemStorageLoader itemStorageLoader;
    private ItemStorageSaver itemStorageSaver;
    @Override
    public void onEnable() {
        // Plugin startup logic
        itemStorageLoader = new ItemStorageLoader(this);
        itemStorageSaver = new ItemStorageSaver(this);
        Objects.requireNonNull(getCommand("inventory")).setExecutor(new StorageUI(this));


        getLogger().info("Loading storages");
        itemStorages = itemStorageLoader.loadExistingStorages(this.getDataFolder());
        getLogger().info("Loaded storages");

    }


    @Override
    public void onDisable() {
        getLogger().info("Saving storages");
        itemStorageSaver.saveStorages(itemStorages, this.getDataFolder());
        getLogger().info("Saved storages");
    }
}
