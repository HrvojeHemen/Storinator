package storinator.storinator.data;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static storinator.storinator.data.StorageComparator.BY_COUNT;

@Getter
public class ItemStorage {
    private static final long DEFAULT_CAPACITY = 64_000;

    private final List<MyItemStack> items;
    private long capacity;
    private long size;
    private Comparator<MyItemStack> currentComparator = BY_COUNT.getComparator();


    public ItemStorage() {
        this.capacity = DEFAULT_CAPACITY;
        this.size = 0;
        this.items = new ArrayList<>();
    }

    public MyItemStack getItem(int index) {
        return items.get(index);
    }

    public List<MyItemStack> getItems(int start, int end) {
        return items.subList(start, end);
    }

    public boolean addItem(MyItemStack item, boolean checkCount) {
        long newSize = this.size + item.getCount();
        if (checkCount) {
            if (newSize > this.capacity) {
                return false;
            }
        }
        this.size = newSize;

        addItem(item);
        return true;
    }

    private void addItem(MyItemStack item) {
        for (MyItemStack itemStack : items) {
            if (itemStack.isSimilar(item)) {
                itemStack.setCount(itemStack.getCount() + item.getCount());
                sortByActiveComparator();
                return;
            }
        }

        items.add(item);
        sortByActiveComparator();
    }

    public void sortByActiveComparator() {
        items.sort(currentComparator);
    }

    public ItemStack removeFullStackOfAnItem(int index) {
        return removeFullStackOfAnItem(items.get(index));
    }

    public ItemStack removeFullStackOfAnItem(MyItemStack itemStack) {
        int countPlayerGets = removeItem(itemStack, itemStack.getMaxStackSize());
        ItemStack toGivePlayer = new ItemStack(itemStack);
        toGivePlayer.setAmount(countPlayerGets);

        return toGivePlayer;
    }

    public void setComparator(StorageComparator comparator) {
        if(currentComparator.equals(comparator.getComparator())){
            currentComparator = currentComparator.reversed();
        }
        else {
            currentComparator = comparator.getComparator();
        }
        items.sort(currentComparator);
    }

    private int removeItem(MyItemStack itemStack, int amount) {
        int decrement = Integer.min(amount, itemStack.getCount());
        itemStack.setCount(itemStack.getCount() - decrement);

        if (itemStack.getCount() <= 0) {
            items.remove(itemStack);
        }

        this.size -= decrement;
        sortByActiveComparator();

        return decrement;
    }

}
