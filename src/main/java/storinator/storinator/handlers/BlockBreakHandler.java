package storinator.storinator.handlers;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import redempt.redlib.blockdata.DataBlock;
import storinator.storinator.Storinator;

import static storinator.storinator.handlers.StorinatorRecipe.getStorinatorItem;

public class BlockBreakHandler implements Listener {

    private final Storinator storinator;

    public BlockBreakHandler(Storinator storinator) {
        this.storinator = storinator;
        Bukkit.getPluginManager().registerEvents(this, storinator);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        DataBlock dataBlock = storinator.getBlockDataManager().getDataBlock(block);
        String storageId = dataBlock.getString("storageId");

        if (storageId == null) {
            return;
        }

        event.setDropItems(false);
        event.getPlayer().getWorld().dropItem(block.getLocation(), getStorinatorItem(storageId));
    }

}
