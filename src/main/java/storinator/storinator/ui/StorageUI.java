package storinator.storinator.ui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import storinator.storinator.Storinator;
import storinator.storinator.data.ItemStorage;
import storinator.storinator.data.MyItemStack;

import java.util.ArrayList;
import java.util.List;

public class StorageUI implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player");
            return true;
        }
        Player player = (Player) sender;

        ItemStorage itemStorage = Storinator.itemStorages.get("1");

        Inventory inventory = Bukkit.createInventory(player, 54);
        var items = getPaginated(itemStorage, 1, 54 - 9);
        int index = 9;
        setNavigation(inventory);
        setItems(inventory, items, index, 54);
        player.openInventory(inventory);



        return true;
    }

    private void setNavigation(Inventory inventory) {
        for(int i = 0; i < 9; i++) {
            inventory.setItem(i, new MyItemStack(Material.GLASS_PANE, 1));
        }
    }

    private void setItems(Inventory inventory, List<MyItemStack> items, int startIndex, int maxIndex){
        for (var item : items){
            var lore = item.lore();
            if(lore == null){
                lore = new ArrayList<>();
            }
            lore.add(Component.text().content(item.getCount() + "").build());
            item.lore(lore);
            inventory.setItem(startIndex++, item);
            Bukkit.getLogger().info("[" + startIndex + "] = " + item + "x" + item.getCount());
            if(startIndex >= maxIndex){
                return;
            }
        }
    }

    List<MyItemStack> getPaginated(ItemStorage itemStorage, int page, int perPage){
        Bukkit.getLogger().info("Getting page " + page + " of " + perPage + " " + page * perPage + " -> " + (page + 1) * perPage );
        var items =  itemStorage.getItems(page * perPage,(page + 1) * perPage);
        Bukkit.getLogger().info("Found " + items.size() + " items");
        return items;
    }
}
