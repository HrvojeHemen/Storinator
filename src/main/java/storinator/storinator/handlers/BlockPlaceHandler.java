package storinator.storinator.handlers;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
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
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();

        storinator.getLogger().info("Placed block detected");

        //TODO find a way to recognize which block is "storinator block" (We probably want inventory type so we can use hoppers) maybe by creating a custom crafting recipe which tags the block somehow

        DataBlock dataBlock = storinator.getBlockDataManager().getDataBlock(block);
        dataBlock.set("test", 1234);

    }
}
