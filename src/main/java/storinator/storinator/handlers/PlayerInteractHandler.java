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
        storinator.getLogger().info("PlayerInteractEvent");
        if(!event.getAction().isRightClick()) return;
        storinator.getLogger().info("RIGHT CLICK");

        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        DataBlock dataBlock = storinator.getBlockDataManager().getDataBlock(block);
        if(dataBlock == null || dataBlock.getString("storageId") == null) return;

        String storageId = dataBlock.getString("storageId");
        storageUI.displayStorageUI(player, storageId);
        event.setCancelled(true);
    }

}
