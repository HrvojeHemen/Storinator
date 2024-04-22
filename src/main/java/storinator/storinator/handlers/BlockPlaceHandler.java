package storinator.storinator.handlers;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import storinator.storinator.Storinator;

import static storinator.storinator.blocks.StorinatorRecipe.RECIPE_KEY;
import static storinator.storinator.blocks.StorinatorRecipe.STORINATOR_CUSTOM_MODEL_DATA;

public class BlockPlaceHandler implements Listener {

    private final Storinator storinator;

    public BlockPlaceHandler(Storinator storinator) {
        this.storinator = storinator;
        Bukkit.getPluginManager().registerEvents(this, storinator);
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {
        if (!isStorinator(event.getItemInHand())) {
            return;
        }

        storinator.getBlockDataManager()
                    .getDataBlock(event.getBlock(), true)
                    .set("storageId", getStorageId(event.getItemInHand()));
    }

    private boolean isStorinator(ItemStack item) {
        return item.getItemMeta().hasCustomModelData() &&
                item.getItemMeta().getCustomModelData() == STORINATOR_CUSTOM_MODEL_DATA;
    }

    private String getStorageId(ItemStack item) {
        return item.getItemMeta()
                    .getPersistentDataContainer()
                    .get(NamespacedKey.fromString(RECIPE_KEY), PersistentDataType.STRING);
    }

}
