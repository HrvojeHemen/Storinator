package storinator.storinator.data;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemStorage {
   private List<ItemStack> items;


    public ItemStorage(List<ItemStack> items) {
        this.items = items;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }
}
