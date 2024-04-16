package storinator.storinator;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.blockdata.BlockDataManager;
import storinator.storinator.data.ItemStorage;
import storinator.storinator.data.ItemStorageLoader;
import storinator.storinator.data.ItemStorageSaver;
import storinator.storinator.handlers.BlockPlaceHandler;
import storinator.storinator.handlers.PlayerInteractHandler;
import storinator.storinator.ui.StorageUI;

import java.util.Map;
import java.util.Objects;


public final class Storinator extends JavaPlugin {

    public static Map<String, ItemStorage> itemStorages;

    private ItemStorageLoader itemStorageLoader;
    private ItemStorageSaver itemStorageSaver;

    @Getter
    private BlockDataManager blockDataManager;
    private BlockPlaceHandler blockPlaceHandler;
    private PlayerInteractHandler playerInteractHandler;

    @Override
    public void onEnable() {
        // Plugin startup logic
        blockDataManager = BlockDataManager.createAuto(this, this.getDataFolder().toPath().resolve("blocks.db"),true, true);
        itemStorageLoader = new ItemStorageLoader(this);
        itemStorageSaver = new ItemStorageSaver(this);
        Objects.requireNonNull(getCommand("inventory")).setExecutor(new StorageUI(this));
        blockPlaceHandler = new BlockPlaceHandler(this);
        playerInteractHandler = new PlayerInteractHandler(this);

        getLogger().info("Loading storages");
        itemStorages = itemStorageLoader.loadExistingStorages(this.getDataFolder());
        getLogger().info("Loaded storages");
    }

    @Override
    public void onDisable() {
        getLogger().info("Saving storages");
        itemStorageSaver.saveStorages(itemStorages, this.getDataFolder());
        blockDataManager.saveAndClose();
        getLogger().info("Saved storages");
    }
}
