package storinator.storinator.handlers;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import redempt.redlib.blockdata.DataBlock;
import storinator.storinator.Storinator;

public class PlayerInteractHandler implements Listener {

    private final Storinator storinator;

    public PlayerInteractHandler(Storinator storinator) {
        this.storinator = storinator;
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
        if(dataBlock == null) return;

        //TODO check if block is if a type we want, and if it has storinator-id set and then open storage
//        if(dataBlock.getInt("test") != null) {
//            event.setCancelled(true);
//        }

        storinator.getLogger().info("Test: " + dataBlock.getInt("test"));
    }
}
