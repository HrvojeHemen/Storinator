package storinator.storinator.handlers;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.blockdata.DataBlock;
import storinator.storinator.Storinator;

import java.util.ArrayList;
import java.util.List;

import static storinator.storinator.handlers.StorinatorRecipe.STORINATOR_ITEM;

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

        if (!item.isSimilar(STORINATOR_ITEM)) {
            return;
        }

        DataBlock dataBlock = storinator.getBlockDataManager().getDataBlock(block);
        String storageId = null;
        if(item.lore() == null || item.lore().isEmpty()) {
             storageId = storinator.createNewStorage();
        }
        else {
            for (var lore : item.lore()) {
                String lString = lore.toString();
                if(lString.startsWith("storageId")){
                    storinator.getLogger().info("Premade storage");

                    storageId = lString.substring("storageId: ".length());
                    break;
                }
            }
        }
        if(storageId == null){
            storageId = storinator.createNewStorage();

        }
        dataBlock.set("storageId", storageId);
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


    private ItemStack getStorinatorItem(String storageId) {
        ItemStack is = new ItemStack(STORINATOR_ITEM);
        List<Component> lore = is.lore() == null ? new ArrayList<>() : is.lore();

        lore.add(Component.text().content("storageId:" + storageId).build());
        is.lore(lore);

        return is;

    }
}
