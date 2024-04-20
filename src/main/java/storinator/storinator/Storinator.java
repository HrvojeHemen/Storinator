package storinator.storinator;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.blockdata.BlockDataManager;
import storinator.storinator.blocks.StorinatorRecipe;
import storinator.storinator.data.ItemStorage;
import storinator.storinator.data.ItemStorageLoader;
import storinator.storinator.data.ItemStorageSaver;
import storinator.storinator.handlers.*;
import storinator.storinator.tasks.StorageBackupTask;
import storinator.storinator.ui.CommandInventory;
import storinator.storinator.ui.StorageUI;

import java.util.Map;
import java.util.Objects;

public final class Storinator extends JavaPlugin {
    public static final long SAVE_DELAY = 12_000L;


    public static Map<String, ItemStorage> itemStorages;
    private StorageUI storageUI;

    private ItemStorageLoader itemStorageLoader;
    @Getter
    private ItemStorageSaver itemStorageSaver;

    @Getter
    private BlockDataManager blockDataManager;
    private BlockBreakHandler blockBreakHandler;
    private BlockPlaceHandler blockPlaceHandler;
    private CraftHandler craftHandler;
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
        blockBreakHandler = new BlockBreakHandler(this);
        blockPlaceHandler = new BlockPlaceHandler(this);
        craftHandler = new CraftHandler(this);
        playerInteractHandler = new PlayerInteractHandler(this, storageUI);

        itemStorages = itemStorageLoader.loadExistingStorages(this.getDataFolder());
        storinatorRecipe = new StorinatorRecipe(this);
        storinatorRecipe.addRecipe();

        Objects.requireNonNull(getCommand("inventory")).setExecutor(new CommandInventory(this, storageUI));

        new StorageBackupTask(this).runTaskTimer(this, SAVE_DELAY, SAVE_DELAY);
    }

    @Override
    public void onDisable() {
        saveStorages();
        blockDataManager.saveAndClose();

        storinatorRecipe.removeRecipe();
    }

    public void saveStorages() {
        itemStorageSaver.saveStorages(itemStorages, this.getDataFolder());
    }

    public void createStorageIfIdDoesntExist(String storageId) {
        if (itemStorages.containsKey(storageId)) {
            return;
        }

        itemStorages.put(storageId, new ItemStorage());
        this.getLogger().info("Created new storage: " + storageId);
    }

    public String createNewStorage() {
        String storageId = Integer.valueOf(itemStorages.size()).toString();
        createStorageIfIdDoesntExist(storageId);

        return storageId;
    }
}
