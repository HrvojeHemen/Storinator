package storinator.storinator.data;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.io.File;
import java.util.*;

public class ItemStorageLoader {

    public static Map<String, ItemStorage> loadExistingStorages(File directory) {
        Util.createDataFolderIfNotExists(directory);
        Map<String, ItemStorage> storages = new HashMap<>();
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile()) {
                Bukkit.getLogger().info("Loading " + file.getName()); // maknut ovaj log il nekak povezat na logger or plugina bas
            }
        }
        storages.put("1", getDummyStorage());
        return storages;
    }


    private static ItemStorage getDummyStorage(){
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
