package storinator.storinator.handlers;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import redempt.redlib.blockdata.BlockDataManager;
import redempt.redlib.blockdata.DataBlock;
import storinator.storinator.Storinator;

import static storinator.storinator.blocks.StorinatorRecipe.getStorinatorItem;

public class BlockBreakHandler implements Listener {

    private final Storinator storinator;

    public BlockBreakHandler(Storinator storinator) {
        this.storinator = storinator;
        Bukkit.getPluginManager().registerEvents(this, storinator);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!isStorinator(event.getBlock())) {
            return;
        }

        String storageId = getStorageId(event.getBlock());
        removeBlockFromDataManager(event.getBlock());
        dropStorinatorItem(event, storageId);
    }

    private boolean isStorinator(Block block) {
        DataBlock dataBlock = storinator.getBlockDataManager().getDataBlock(block, false);

        return dataBlock != null && dataBlock.getString("storageId") != null;
    }

    private String getStorageId(Block block) {
        return storinator.getBlockDataManager()
                         .getDataBlock(block)
                         .getString("storageId");
    }

    private void removeBlockFromDataManager(Block block) {
        BlockDataManager dataManager = storinator.getBlockDataManager();
        DataBlock dataBlock = dataManager.getDataBlock(block, false);
        dataManager.remove(dataBlock);
    }

    private void dropStorinatorItem(BlockBreakEvent event, String storageId) {
        event.setDropItems(false);

        event.getPlayer()
                .getWorld()
                .dropItem(event.getBlock().getLocation(),
                          getStorinatorItem(storageId));
    }

}
