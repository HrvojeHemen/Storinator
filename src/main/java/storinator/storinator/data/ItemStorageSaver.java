package storinator.storinator.data;

import lombok.RequiredArgsConstructor;
import storinator.storinator.Storinator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class ItemStorageSaver {

    private final Storinator storinator;

    public void saveStorages(Map<String, ItemStorage> storages, File directory) {
        Util.createDataFolderIfNotExists(directory);

        for (Map.Entry<String, ItemStorage> entry : storages.entrySet()) {
            boolean saved = saveStorage(entry.getKey(), entry.getValue(), directory);
            if (!saved) {
                storinator.getLogger().info("Could not save storage " + entry.getKey());
            }
        }
    }

    private boolean saveStorage(String storageName, ItemStorage storage, File directory) {
        File saveFile = new File(directory, storageName);
        if (!saveFile.exists()) {
            try {
                boolean created = saveFile.createNewFile();
                if (!created) return false;

                FileWriter fw = new FileWriter(saveFile);
                for (MyItemStack stack : storage.getItems()) {
                   //TODO saving impl
                }

                fw.close();

            } catch (IOException e) {
                return false;
            }
        }

        return true;
    }
}
