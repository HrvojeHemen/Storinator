package storinator.storinator;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerValidator {

    private static final Integer HOTBAR_OFFSET = 9;

    public static int findEmptySlot(HumanEntity entity) {
        if (!(entity instanceof Player player)) {
            return -1;
        }

        ItemStack[] contents = player.getInventory().getStorageContents();

        for (int i = 0; i < contents.length; i++) {
            int index = (i + HOTBAR_OFFSET) % contents.length;
            if (contents[index] == null || contents[index].isEmpty()) {
                return index;
            }
        }

        return - 1;
    }
}
