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
    private final Storinator storinator;
    private ItemStorage itemStorage;
    private Inventory inventory;
    private int currentPage;

    private static final String INVENTORY_NAME = "Storinator";
    private static final int PREVIOUS_PAGE_INDEX = 0;
    private static final int NEXT_PAGE_INDEX = 1;
    private static final int SORT_BY_COUNT_INDEX = 2;
    private static final int ITEMS_PER_ROW = 9;
    private static final int TOTAL_ROWS = 6;
    private static final int TOTAL_INVENTORY_SLOTS = ITEMS_PER_ROW * TOTAL_ROWS;
    private static final int ITEMS_PER_PAGE = ITEMS_PER_ROW * (TOTAL_ROWS - 1);

    public StorageUI(Storinator storinator) {
        this.storinator = storinator;
        Bukkit.getPluginManager().registerEvents(this, storinator);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be executed by a player");
            return true;
        }
        String storageId = "1";
        setCurrentPage(0);
        itemStorage = Storinator.itemStorages.get(storageId);
        inventory = Bukkit.createInventory(player, TOTAL_INVENTORY_SLOTS, Component.text(INVENTORY_NAME + "#" + storageId + ", Page " + (currentPage + 1)));

        displayInventory(inventory, player, currentPage);
        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().startsWith(INVENTORY_NAME)) return;
        if (event.getCurrentItem() == null) return;
        storinator.getLogger().info("Clicked: " + event.getCurrentItem() + ", slot = " + event.getSlot() + ", raw = " + event.getRawSlot());
        Player player = (Player) event.getWhoClicked();

        int slot = event.getSlot();
        int rawSlot = event.getRawSlot();
        boolean updated;
        if (event.getView().getBottomInventory().equals(event.getClickedInventory())) {
            updated = handlePlayerInventoryClick(event, slot, rawSlot);
        } else if (event.getView().getTopInventory().equals(event.getClickedInventory())) {
            updated = handleStorinatorInventoryClick(event, slot, rawSlot);
        } else {
            storinator.getLogger().severe("Unknown inventory: " + event.getClickedInventory());
            return;
        }

        if (updated) displayInventory(event.getInventory(), player, currentPage);
    }

    private boolean handlePlayerInventoryClick(InventoryClickEvent event, int slot, int rawSlot) {
        storinator.getLogger().info("Player inventory click");
        if (!event.isShiftClick()) {
            return false;
        }

        Inventory playerInventory = event.getView().getInventory(rawSlot);
        if (playerInventory == null) return false;

        ItemStack itemsAtIndex = playerInventory.getItem(slot);
        if (itemsAtIndex == null) return false;

        event.setCancelled(true);
        playerInventory.setItem(slot, null);
        return true;
    }

    private boolean handleStorinatorInventoryClick(InventoryClickEvent event, int slot, int rawSlot) {
        storinator.getLogger().info("Storinator inventory click");
        event.setCancelled(true);
        // NAVIGATION BAR
        if (rawSlot < ITEMS_PER_ROW) {
            switch (rawSlot) {
                case PREVIOUS_PAGE_INDEX -> setCurrentPage(Integer.max(currentPage - 1, 0));
                case NEXT_PAGE_INDEX -> {
                    int lastPage = itemStorage.getItems().size() / ITEMS_PER_PAGE;
                    storinator.getLogger().info(itemStorage.getItems().size() + " " + ITEMS_PER_PAGE + " " + lastPage);
                    setCurrentPage(Integer.min(currentPage + 1, lastPage));
                }
                case SORT_BY_COUNT_INDEX -> sortItemsByCountDesc(itemStorage);
            }
        }
        //ITEM CLICKS
        else {
            storinator.getLogger().info("Giving player item at slot " + rawSlot);
            MyItemStack itemStack = itemStorage.getItem(currentPage * ITEMS_PER_PAGE + rawSlot - ITEMS_PER_ROW);
            int count = itemStack.getCount();
            int countPlayerGets = Integer.min(itemStack.getMaxStackSize(), count);

            if (count == countPlayerGets) {
                itemStorage.removeItemStack(itemStack);
            } else {
                itemStack.setCount(count - countPlayerGets);
                itemStorage.sortByActiveComparator();
            }

            ItemStack toGivePlayer = new ItemStack(itemStack.getType(), countPlayerGets);

            //TODO this is behaving a bit weird, it prefers hotbar instead of inv, make it prefer inventory,
            // index 0 is hot bar, index 9 is top left of player inv, check it out
            event.getView().getBottomInventory().addItem(toGivePlayer);
        }

        return true;
    }

    private void setCurrentPage(int newCurrentPage) {
        storinator.getLogger().info("Changing current page " + currentPage + " -> " + newCurrentPage);
        currentPage = newCurrentPage;
    }

    private void displayInventory(Inventory inventory, Player player, int page) {
        var items = getPaginated(itemStorage, page);
        int index = 9;
        setNavigation(inventory);
        setItems(inventory, items, index);
        player.openInventory(inventory);
    }

    private void sortItemsByCountDesc(ItemStorage itemStorage) {
        itemStorage.sort(ItemStorage.BY_COUNT_REVERSED);
    }

    private void setNavigation(Inventory inventory) {
        inventory.setItem(PREVIOUS_PAGE_INDEX, getNavigationButton("Previous Page"));
        inventory.setItem(NEXT_PAGE_INDEX, getNavigationButton("Next Page"));
        inventory.setItem(SORT_BY_COUNT_INDEX, getNavigationButton("Sort by count desc."));
    }

    private ItemStack getNavigationButton(String text) {
        ItemStack itemStack = new ItemStack(Material.GLASS_PANE);
        var meta = itemStack.getItemMeta();
        meta.displayName(Component.text().content(text).build());
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    private void setItems(Inventory inventory, List<MyItemStack> items, int startIndex) {
        for (var item : items) {
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text().content(item.getCount() + "").build());
            item.lore(lore); //TODO check if there is a case where item lore exists, and this over writes is

            inventory.setItem(startIndex++, item);
            if (startIndex >= TOTAL_INVENTORY_SLOTS) {
                break;
            }
        }

        //not enough items left, so we set remaining storage slots to null
        for (int i = startIndex; i < TOTAL_INVENTORY_SLOTS; i++) {
            inventory.setItem(i, null);
        }
    }

    List<MyItemStack> getPaginated(ItemStorage itemStorage, int page) {
        return itemStorage.getItems(page * StorageUI.ITEMS_PER_PAGE, Integer.min((page + 1) * StorageUI.ITEMS_PER_PAGE, itemStorage.getItems().size()));
    }
}
