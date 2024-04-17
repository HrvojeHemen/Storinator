package storinator.storinator;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.blockdata.BlockDataManager;
import storinator.storinator.data.ItemStorage;
import storinator.storinator.data.ItemStorageLoader;
import storinator.storinator.data.ItemStorageSaver;
import storinator.storinator.handlers.BlockPlaceHandler;
import storinator.storinator.handlers.PlayerInteractHandler;
import storinator.storinator.handlers.StorinatorRecipe;
import storinator.storinator.ui.CommandInventory;
import storinator.storinator.ui.StorageUI;

import java.util.Map;
import java.util.Objects;

public final class Storinator extends JavaPlugin {

    public static Map<String, ItemStorage> itemStorages;
    private StorageUI storageUI;

    private ItemStorageLoader itemStorageLoader;
    private ItemStorageSaver itemStorageSaver;

    @Getter
    private BlockDataManager blockDataManager;
    private BlockPlaceHandler blockPlaceHandler;
    private PlayerInteractHandler playerInteractHandler;
    private StorinatorRecipe storinatorRecipe;

    private CommandInventory commandInventory;

    @Override
    public void onEnable() {
        // Plugin startup logic
        blockDataManager = BlockDataManager.createAuto(this, this.getDataFolder().toPath().resolve("blocks.db"),true, true);
        itemStorageLoader = new ItemStorageLoader(this);
        itemStorageSaver = new ItemStorageSaver(this);

        storageUI = new StorageUI(this);
        blockPlaceHandler = new BlockPlaceHandler(this);
        playerInteractHandler = new PlayerInteractHandler(this, storageUI);

        getLogger().info("Loading storages");
        itemStorages = itemStorageLoader.loadExistingStorages(this.getDataFolder());
        getLogger().info("Loaded storages");

        storinatorRecipe = new StorinatorRecipe(this);
        storinatorRecipe.addRecipe();

        Objects.requireNonNull(getCommand("inventory")).setExecutor(new CommandInventory(this, storageUI));
    }

    @Override
    public void onDisable() {
        getLogger().info("Saving storages");
        itemStorageSaver.saveStorages(itemStorages, this.getDataFolder());
        blockDataManager.saveAndClose();
        getLogger().info("Saved storages");

        storinatorRecipe.removeRecipe();
    }

    public void createIfDoesNotExist(String storageId) {
        if (itemStorages.containsKey(storageId)) {
            return;
        }

        itemStorages.put(storageId, new ItemStorage());
        this.getLogger().info("Created new storage: " + storageId);
    }

    public String createNewStorage() {
        String storageId = Integer.valueOf(itemStorages.size()).toString();
        createIfDoesNotExist(storageId);

        return storageId;
    }
}
