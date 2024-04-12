package storinator.storinator.data;

import java.io.File;

public class Util {
    public static void createDataFolderIfNotExists(File directory) {
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
}
