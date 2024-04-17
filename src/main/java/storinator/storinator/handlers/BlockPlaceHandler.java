package storinator.storinator.handlers;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.blockdata.DataBlock;
import storinator.storinator.Storinator;

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

        if (!item.isSimilar(StorinatorRecipe.STORINATOR_ITEM)) {
            return;
        }

        DataBlock dataBlock = storinator.getBlockDataManager().getDataBlock(block);
        String newStorageId = storinator.createNewStorage();
        dataBlock.set("storageId", newStorageId);
    }
}
