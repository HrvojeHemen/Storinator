package storinator.storinator.ui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
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

import static storinator.storinator.data.StorageComparator.BY_COUNT_REVERSED;

public class StorageUI implements Listener, CommandExecutor {

    private static final String INVENTORY_NAME = "Storinator";

    private static final int ITEMS_PER_ROW = 9;
    private static final int TOTAL_ROWS = 6;
    private static final int TOTAL_INVENTORY_SLOTS = ITEMS_PER_ROW * TOTAL_ROWS;
    private static final int ITEMS_PER_PAGE = ITEMS_PER_ROW * (TOTAL_ROWS - 1);

    private static final int PREVIOUS_PAGE_INDEX = 0;
    private static final int NEXT_PAGE_INDEX = 1;
    private static final int SORT_BY_COUNT_INDEX = 2;
    private static final int ITEM_COUNT_INDEX = ITEMS_PER_ROW - 1;


    private final Storinator storinator;
    private ItemStorage itemStorage;
    private Inventory inventory;
    private int currentPage;

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

        if (event.getView().getBottomInventory().equals(event.getClickedInventory())) {
            boolean refreshInventory = handlePlayerInventoryClick(event, slot, rawSlot);
            if (refreshInventory) displayInventory(event.getInventory(), player, currentPage);
        } else if (event.getView().getTopInventory().equals(event.getClickedInventory())) {
            handleStorinatorInventoryClick(event, slot, rawSlot);
            displayInventory(event.getInventory(), player, currentPage);
        } else {
            storinator.getLogger().severe("Unknown inventory: " + event.getClickedInventory());
        }
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

        MyItemStack itemStack = new MyItemStack(itemsAtIndex);
        boolean added = itemStorage.addItem(itemStack, true);
        if (!added) {
            event.setCancelled(true);
            return false;
        }
        playerInventory.setItem(slot, null);
        return true;
    }

    private boolean handleStorinatorInventoryClick(InventoryClickEvent event, int slot, int rawSlot) {
        storinator.getLogger().info("Storinator inventory click");
        event.setCancelled(true);
        if (rawSlot < ITEMS_PER_ROW) {
            handleNavigationBarClicked(rawSlot);
        } else {
            handleItemClick(event, rawSlot);
        }

        return true;
    }

    private void handleNavigationBarClicked(int rawSlot) {
        switch (rawSlot) {
            case PREVIOUS_PAGE_INDEX -> setCurrentPage(Integer.max(currentPage - 1, 0));
            case NEXT_PAGE_INDEX -> {
                int lastPage = itemStorage.getItems().size() / ITEMS_PER_PAGE;
                storinator.getLogger().info(itemStorage.getItems().size() + " " + ITEMS_PER_PAGE + " " + lastPage);
                setCurrentPage(Integer.min(currentPage + 1, lastPage));
            }
            case SORT_BY_COUNT_INDEX -> itemStorage.setComparator(BY_COUNT_REVERSED);
        }
    }

    private void handleItemClick(InventoryClickEvent event, int rawSlot) {
        storinator.getLogger().info("Giving player item at slot " + rawSlot);
        MyItemStack itemStack = itemStorage.getItem(currentPage * ITEMS_PER_PAGE + rawSlot - ITEMS_PER_ROW);
        int count = itemStack.getCount();
        int countPlayerGets = Integer.min(itemStack.getMaxStackSize(), count);

        if (count == countPlayerGets) {
            itemStorage.removeItemStack(itemStack);
        } else {
            itemStorage.decrementItemStackCount(itemStack, countPlayerGets);
        }

        ItemStack toGivePlayer = new ItemStack(itemStack);
        toGivePlayer.setAmount(countPlayerGets);

        //TODO this is behaving a bit weird, it prefers hotbar instead of inv, make it prefer inventory, index 0 is hot bar, index 9 is top left of player inv, check it out

        //TODO check if player inventory has empty space for items


        event.getView().getBottomInventory().addItem(toGivePlayer);
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

    private void setNavigation(Inventory inventory) {
        inventory.setItem(PREVIOUS_PAGE_INDEX, getNavigationButton(getText("Previous Page")));
        inventory.setItem(NEXT_PAGE_INDEX, getNavigationButton(getText("Next Page")));
        inventory.setItem(SORT_BY_COUNT_INDEX, getNavigationButton(getText("Sort by count desc.")));
        inventory.setItem(ITEM_COUNT_INDEX, getNavigationButton(getCapacityLabelText()));
    }

    private ItemStack getNavigationButton(Component textComponent) {
        ItemStack itemStack = new ItemStack(Material.GLASS_PANE);
        var meta = itemStack.getItemMeta();
        meta.displayName(textComponent);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    private Component getText(String text) {
        return Component.text(text);
    }

    private Component getCapacityLabelText() {
        long size = itemStorage.getSize();
        long capacity = itemStorage.getCapacity();
        double fullness = 1.0 * size / capacity;
        String capacityLabel = String.format("Utilization: %,d/%,d [%.2f%%]", itemStorage.getSize(), itemStorage.getCapacity(), 100.0 * fullness);

        storinator.getLogger().info(fullness + " -> " + interpolateColor(fullness));
        return Component.text(capacityLabel).color(interpolateColor(fullness));
    }

    private TextColor interpolateColor(double fullness) {
        int r = (int) (255 * fullness);
        int g = (int) (255 * (1 - fullness));

        return TextColor.color(r, g, 0);
    }


    private void setItems(Inventory inventory, final List<MyItemStack> items, int startIndex) {
        for (final var item : items) {

            //we copy this, so we don't lose some metadata in the storage
            MyItemStack copyForUI = new MyItemStack(item);

            List<Component> lore = copyForUI.lore() == null ? new ArrayList<>() : copyForUI.lore();
            lore.add(Component.text().content(item.getCount() + "").build());
            copyForUI.lore(lore);

            inventory.setItem(startIndex++, copyForUI);
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
