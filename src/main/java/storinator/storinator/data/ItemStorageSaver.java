package storinator.storinator.data;

import lombok.RequiredArgsConstructor;
import storinator.storinator.Storinator;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

@RequiredArgsConstructor
public class ItemStorageSaver {

    private final Storinator storinator;

    public void saveStorages(Map<String, ItemStorage> storages, File directory) {
        storinator.getLogger().info("Saving storages");
        Util.createDataFolderIfNotExists(directory);

        for (Map.Entry<String, ItemStorage> entry : storages.entrySet()) {
            saveStorage(entry.getKey(), entry.getValue(), directory);
        }
    }

    private void saveStorage(String storageName, ItemStorage storage, File directory) {
        File saveFile = new File(directory, storageName);
        try {

            String base64 = Util.serializeToBase64(storage.getItems());

            FileWriter fw = new FileWriter(saveFile);
            fw.write(base64);
            fw.close();

        } catch (Exception e) {
            storinator.getLogger().severe("Could not save storage " + e);
        }
    }
}
