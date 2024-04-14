package storinator.storinator.data;

import java.util.Comparator;
import java.util.List;

public class ItemStorage {
    private List<MyItemStack> items;
    private Comparator<MyItemStack> currentComparator;

    public ItemStorage(List<MyItemStack> items) {
        this.items = items;
        currentComparator = BY_COUNT;
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

    public void sort(Comparator<MyItemStack> comparator) {
        currentComparator = comparator;
        items.sort(currentComparator);
    }

    public void sortByActiveComparator() {
        items.sort(currentComparator);
    }

    public void removeItemStack(MyItemStack itemStack) {
        items.remove(itemStack);
    }

    public static Comparator<MyItemStack> BY_COUNT = Comparator.comparing(MyItemStack::getCount);
    public static Comparator<MyItemStack> BY_COUNT_REVERSED = BY_COUNT.reversed();
}
