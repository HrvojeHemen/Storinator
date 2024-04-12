package storinator.storinator.data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MyItemStack extends ItemStack {
    private int count;

    public MyItemStack(int count) {
        this.count = count;
    }

    public MyItemStack(@NotNull Material type, int count) {
        super(type);
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

