package storinator.storinator.data;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static storinator.storinator.data.StorageComparator.BY_COUNT;

@Getter
public class ItemStorage {

    private final List<MyItemStack> items;
    private Comparator<MyItemStack> currentComparator = BY_COUNT.getComparator();


    public ItemStorage(){
        this(new ArrayList<>());
    }

    public ItemStorage(List<MyItemStack> items) {
        this.items = items;
        sortByActiveComparator();
    }

    public MyItemStack getItem(int index) {
        return items.get(index);
    }

    public List<MyItemStack> getItems(int start, int end) {
        return items.subList(start, end);
    }

    public void addItem(MyItemStack item) {
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

    public void removeItemStack(MyItemStack itemStack) {
        items.remove(itemStack);
    }

    public void setComparator(StorageComparator comparator) {
        currentComparator = comparator.getComparator();
        items.sort(currentComparator);
    }

}
