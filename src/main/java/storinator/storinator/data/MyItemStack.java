package storinator.storinator.data;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class MyItemStack extends ItemStack {
    private int count;

    public MyItemStack(int count) {
        this.count = count;
    }

    public MyItemStack(MyItemStack myItemStack) {
        super(myItemStack);
        this.count = myItemStack.getCount();
    }

    public MyItemStack(@NotNull Material type, int count) {
        super(type);
        this.count = count;
    }

    public MyItemStack(@NotNull ItemStack stack) throws IllegalArgumentException {
        super(stack);
        this.count = stack.getAmount();
        this.setAmount(1);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), count);
    }
}

