package storinator.storinator.data;

import org.bukkit.Bukkit;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ItemStorageLoader {

    public static Map<String, ItemStorage> loadExistingStorages(File directory) {
        Util.createDataFolderIfNotExists(directory);
        Map<String, ItemStorage> storages = new HashMap<>();
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile()) {
                Bukkit.getLogger().info("Loading " + file.getName()); // maknut ovaj log il nekak povezat na logger or plugina bas
            }
        }
        return storages;
    }

}
