package storinator.storinator.handlers;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import redempt.redlib.blockdata.DataBlock;
import storinator.storinator.Storinator;
import storinator.storinator.ui.StorageUI;

public class PlayerInteractHandler implements Listener {

    private final Storinator storinator;
    private final StorageUI storageUI;

    public PlayerInteractHandler(Storinator storinator, StorageUI storageUI) {
        this.storinator = storinator;
        this.storageUI = storageUI;
        Bukkit.getPluginManager().registerEvents(this, storinator);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!isStorinator(event.getClickedBlock()) || !event.getAction().isRightClick()) {
            return;
        }

        storageUI.displayStorageUI(event.getPlayer(), getStorageId(event.getClickedBlock()));
        event.setCancelled(true);
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

}
