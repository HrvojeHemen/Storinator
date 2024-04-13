package storinator.storinator.ui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import storinator.storinator.Storinator;
import storinator.storinator.data.ItemStorage;
import storinator.storinator.data.MyItemStack;

import java.util.ArrayList;
import java.util.List;

public class StorageUI implements Listener, CommandExecutor {
    private Storinator storinator;
    private ItemStorage itemStorage;
    private Inventory inventory;
    private int currentPage;

    private static final String INVENTORY_NAME = "Storinator";
    private static final int PREVIOUS_PAGE_INDEX = 0;
    private static final int NEXT_PAGE_INDEX = 1;
    private static final int SORT_BY_AMOUNT_INDEX = 2;
    private static final int ITEMS_PER_ROW = 9;
    private static final int TOTAL_ROWS = 6;
    private static final int TOTAL_INVENTORY_SLOTS = ITEMS_PER_ROW * TOTAL_ROWS;
    private static final int ITEMS_PER_PAGE = ITEMS_PER_ROW * (TOTAL_ROWS - 1);

    public StorageUI(Storinator storinator) {
        this.storinator = storinator;
        Bukkit.getPluginManager().registerEvents(this, storinator);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().startsWith(INVENTORY_NAME)) return;
        if (event.getCurrentItem() == null) return;
        storinator.getLogger().info("Clicked: " + event.getCurrentItem() + ", slot = " + event.getSlot() + ", raw = " + event.getRawSlot());
        Player player = (Player) event.getWhoClicked();

        int slot = event.getSlot();
        int rawSlot = event.getRawSlot();

        if(event.getView().getBottomInventory().equals(event.getClickedInventory())) {
            handlePlayerInventoryClick(event, slot, rawSlot);
        }
        else if(event.getView().getTopInventory().equals(event.getClickedInventory())) {
            handleStorinatorInventoryClick(event, slot, rawSlot);
        }
        else {
            storinator.getLogger().severe("Unknown inventory: " + event.getClickedInventory());
            return;
        }

        displayInventory(event.getInventory(), player, currentPage);
    }

    private void handlePlayerInventoryClick(InventoryClickEvent event, int slot, int rawSlot){
        storinator.getLogger().info("Player inventory click");
        if(!event.isShiftClick()){
            return;
        }

        Inventory playerInventory =  event.getView().getInventory(rawSlot);
        if(playerInventory == null) return;

        ItemStack itemsAtIndex = playerInventory.getItem(slot);
        if(itemsAtIndex == null) return;

        event.setCancelled(true);
        playerInventory.removeItem(itemsAtIndex);
    }

    private void handleStorinatorInventoryClick(InventoryClickEvent event, int slot, int rawSlot){
        storinator.getLogger().info("Storinator inventory click");
        event.setCancelled(true);
        // NAVIGATION BAR
        if(rawSlot < ITEMS_PER_ROW) {
            switch (rawSlot) {
                case PREVIOUS_PAGE_INDEX -> currentPage = Integer.max(currentPage - 1, 0);
                case NEXT_PAGE_INDEX -> {
                    int lastPage = itemStorage.getItems().size() % ITEMS_PER_PAGE;
                    currentPage = Integer.min(currentPage + 1, lastPage);
                }
                case SORT_BY_AMOUNT_INDEX -> {
                    sortItemsByAmount(itemStorage);
                }
            }
        }
        //ITEM CLICKS
        else {
            storinator.getLogger().info("Giving player item at slot " + rawSlot);
            MyItemStack itemStack = itemStorage.getItem(currentPage * ITEMS_PER_PAGE + rawSlot - ITEMS_PER_ROW);

            ItemStack toGivePlayer = new ItemStack(itemStack.getType(), 64);

            //TODO this is behaving a bit weird sometimes, check it out
            event.getView().getBottomInventory().addItem(toGivePlayer);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be executed by a player");
            return true;
        }
        String storageId = "1";
        currentPage = 0;
        itemStorage = Storinator.itemStorages.get(storageId);
        inventory = Bukkit.createInventory(player, TOTAL_INVENTORY_SLOTS, Component.text(INVENTORY_NAME + "#" + storageId + ", Page " + (currentPage + 1)));

        displayInventory(inventory, player, currentPage);
        return true;
    }

    private void displayInventory(Inventory inventory, Player player, int page) {
        var items = getPaginated(itemStorage, page, ITEMS_PER_PAGE);
        int index = 9;
        setNavigation(inventory);
        setItems(inventory, items, index, TOTAL_INVENTORY_SLOTS);
        player.openInventory(inventory);
    }

    private void sortItemsByAmount(ItemStorage itemStorage) {
        itemStorage.sort(ItemStorage.BY_AMOUNT);
    }

    private void setNavigation(Inventory inventory) {
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, new MyItemStack(Material.GLASS_PANE, 1));
        }
    }

    private void setItems(Inventory inventory, List<MyItemStack> items, int startIndex, int maxIndex) {
        for (var item : items) {
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text().content(item.getCount() + "").build());
            item.lore(lore); //TODO check if there is a case where item lore exists, and this over writes is

            inventory.setItem(startIndex++, item);
//            storinator.getLogger().info("[" + startIndex + "] = " + item + "x" + item.getCount());
            if (startIndex >= maxIndex) {
                return;
            }
        }
    }

    List<MyItemStack> getPaginated(ItemStorage itemStorage, int page, int perPage) {
//        storinator.getLogger().info("Getting page " + page + " of " + perPage + " " + page * perPage + " -> " + (page + 1) * perPage);
        var items = itemStorage.getItems(page * perPage, (page + 1) * perPage);
//        storinator.getLogger().info("Found " + items.size() + " items");
        return items;
    }
}
