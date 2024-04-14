package storinator.storinator.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class MyItemStack extends ItemStack {

    private int count;

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

}
