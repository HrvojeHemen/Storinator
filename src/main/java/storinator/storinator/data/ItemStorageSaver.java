package storinator.storinator.data;

import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;


public class ItemStorageSaver {

    public static void saveStorages(Map<String, ItemStorage> storages, File directory) {
        Util.createDataFolderIfNotExists(directory);

        for (Map.Entry<String, ItemStorage> entry : storages.entrySet()) {
            boolean saved = saveStorage(entry.getKey(), entry.getValue(), directory);
            if (!saved) {
                Bukkit.getLogger().info("Could not save storage " + entry.getKey()); // isto ko za onaj drugi Bukkit.getLogger
            }
        }
    }

    private static boolean saveStorage(String storageName, ItemStorage storage, File directory) {
        File saveFile = new File(directory, storageName);
        if (!saveFile.exists()) {
            try {
                boolean created = saveFile.createNewFile();
                if (!created) return false;

                FileWriter fw = new FileWriter(saveFile);
                for (MyItemStack stack : storage.getItems()) {
                    fw.write(stack.toString() + "\n");
                }

                fw.close();

            } catch (IOException e) {
                return false;
            }
        }

        return true;
    }
}
