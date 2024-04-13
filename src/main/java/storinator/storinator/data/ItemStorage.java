package storinator.storinator.data;

import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.List;

public class ItemStorage {
    private List<MyItemStack> items;

    public ItemStorage(List<MyItemStack> items) {
        this.items = items;
    }

    public List<MyItemStack> getItems() {
        return items;
    }

    public MyItemStack getItem(int index) {
        return items.get(index);
    }

    public List<MyItemStack> getItems(int start, int end) {
        return items.subList(start, end);
    }

    public void setItems(List<MyItemStack> items) {
        this.items = items;
    }

    public void sort(Comparator<MyItemStack> comparator) {
        items.sort(comparator);
    }

    public static Comparator<MyItemStack> BY_AMOUNT = Comparator.comparingInt(MyItemStack::getCount);
}
