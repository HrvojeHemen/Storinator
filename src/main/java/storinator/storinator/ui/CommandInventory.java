package storinator.storinator.ui;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import storinator.storinator.Storinator;


@Deprecated
@RequiredArgsConstructor
public class CommandInventory implements Listener, CommandExecutor {

    private static final String INVENTORY_COMMAND_STORAGE_ID = "admin";

    private final StorageUI storageUI;

    public CommandInventory(Storinator storinator, StorageUI storageUI) {
        this.storageUI = storageUI;
        storinator.createStorageIfIdDoesntExist(INVENTORY_COMMAND_STORAGE_ID);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be executed by a player");
            return true;
        }

        storageUI.displayStorageUI(player, INVENTORY_COMMAND_STORAGE_ID);
        return true;
    }
}
