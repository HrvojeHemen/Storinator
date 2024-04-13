package storinator.storinator.data;

import org.bukkit.Material;
import storinator.storinator.Storinator;

import java.io.File;
import java.util.*;

public class ItemStorageLoader {
    Storinator storinator;

    public ItemStorageLoader(Storinator storinator) {
        this.storinator = storinator;
    }

    public Map<String, ItemStorage> loadExistingStorages(File directory) {
        Util.createDataFolderIfNotExists(directory);
        Map<String, ItemStorage> storages = new HashMap<>();
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile()) {
                storinator.getLogger().info("Loading " + file.getName());

                //TODO load impl
            }
        }
        storages.put("1", getDummyStorage());
        return storages;
    }


    private ItemStorage getDummyStorage(){
        List<MyItemStack> items = new ArrayList<>();
        var allBlocks = Material.values();
        for (int i = 0; i < 500; i++) {
            var block = allBlocks[new Random().nextInt(allBlocks.length)];
            if(block.isItem()){
                items.add(new MyItemStack(block, i * 10));
            }
            else{
                i --;
            }
        }
        return new ItemStorage(items);
    }

}
