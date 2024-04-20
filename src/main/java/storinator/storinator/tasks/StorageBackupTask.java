package storinator.storinator.tasks;

import lombok.AllArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;
import storinator.storinator.Storinator;

@AllArgsConstructor
public class StorageBackupTask extends BukkitRunnable {
    private Storinator storinator;

    @Override
    public void run() {
        storinator.saveStorages();
    }
}
