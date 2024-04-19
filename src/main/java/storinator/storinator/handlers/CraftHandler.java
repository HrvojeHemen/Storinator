package storinator.storinator.handlers;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import storinator.storinator.Storinator;

public class CraftHandler implements Listener {
    private final Storinator storinator;

    public CraftHandler(Storinator storinator) {
        this.storinator = storinator;
        Bukkit.getPluginManager().registerEvents(this, storinator);
    }

    @EventHandler
    public void craft(CraftItemEvent event) {

        ItemStack currentItem =  event.getCurrentItem();
        if(currentItem == null || !currentItem.hasItemMeta() || !currentItem.getItemMeta().hasCustomModelData()) return;

        int modelData = currentItem.getItemMeta().getCustomModelData();
        if(modelData != StorinatorRecipe.STORINATOR_CUSTOM_MODEL_DATA) return;


        String storageId = storinator.createNewStorage();

        ItemStack storinatorItem = StorinatorRecipe.getStorinatorItem(storageId);
        storinatorItem.setAmount(1);
        event.setCurrentItem(storinatorItem);
    }
}
