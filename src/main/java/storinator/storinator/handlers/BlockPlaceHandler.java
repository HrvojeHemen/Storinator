package storinator.storinator.handlers;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import redempt.redlib.blockdata.DataBlock;
import storinator.storinator.Storinator;

import static storinator.storinator.handlers.StorinatorRecipe.RECIPE_KEY;
import static storinator.storinator.handlers.StorinatorRecipe.STORINATOR_CUSTOM_MODEL_DATA;

public class BlockPlaceHandler implements Listener {

    private final Storinator storinator;

    public BlockPlaceHandler(Storinator storinator) {
        this.storinator = storinator;
        Bukkit.getPluginManager().registerEvents(this, storinator);
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        ItemStack item = event.getItemInHand();

        if (!item.getItemMeta().hasCustomModelData()) {
            return;
        }

        if (item.getItemMeta().getCustomModelData() != STORINATOR_CUSTOM_MODEL_DATA) {
            return;
        }

        DataBlock dataBlock = storinator.getBlockDataManager().getDataBlock(block);

        ItemMeta meta = item.getItemMeta();
        String storageId = meta.getPersistentDataContainer().get(NamespacedKey.fromString(RECIPE_KEY), PersistentDataType.STRING);

        if (storageId == null) {
            storageId = storinator.createNewStorage();
        }

        dataBlock.set("storageId", storageId);
    }
}
