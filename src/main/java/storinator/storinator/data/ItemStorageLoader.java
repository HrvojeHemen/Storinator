package storinator.storinator.data;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import storinator.storinator.Storinator;

import java.io.File;
import java.util.*;

@RequiredArgsConstructor
public class ItemStorageLoader {

    private final Storinator storinator;

    public Map<String, ItemStorage> loadExistingStorages(File directory) {
        storinator.getLogger().info("Loading storages");

        Util.createDataFolderIfNotExists(directory);
        Map<String, ItemStorage> storages = new HashMap<>();
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile()) {
                try {
                    ItemStorage itemStorage = new ItemStorage();

                    Scanner scanner = new Scanner(file.toPath());
                    StringBuilder data = new StringBuilder();
                    while (scanner.hasNextLine()) {
                        data.append(scanner.nextLine().strip());
                    }


                   MyItemStack[] itemStacks =  Util.deserializeFromBase64(data.toString());

                    for (MyItemStack itemStack : itemStacks) {
                        itemStorage.addItem(itemStack, false);
                    }

                    storages.put(file.getName(), itemStorage);


                } catch (Exception e) {
                    storinator.getLogger().severe("Error loading " + file.getName() + " " + e.getMessage());
                }

            }
        }
//        storages.put("1", getDummyStorage());
        return storages;
    }

    private ItemStorage getDummyStorage() {
        List<MyItemStack> items = new ArrayList<>();
        ItemStorage itemStorage = new ItemStorage();
        var allBlocks = Material.values();
        for (int i = 0; i < 500; i++) {
            var block = allBlocks[new Random().nextInt(allBlocks.length)];
            if (block.isItem()) {
                itemStorage.addItem(new MyItemStack(block, i * 10), false);
            } else {
                i--;
            }
        }

        return itemStorage;
    }

}
