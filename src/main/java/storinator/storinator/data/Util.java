package storinator.storinator.data;

import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.util.List;

public class Util {

    public static void createDataFolderIfNotExists(File directory) {
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static String serializeToBase64(List<MyItemStack> items) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            DataOutput output = new DataOutputStream(outputStream);
            output.writeInt(items.size());

            for (MyItemStack item : items) {
                if (item == null) {
                    continue;
                }

                byte[] bytes = item.serializeAsBytes();
                output.writeInt(bytes.length);
                output.write(bytes);
            }
            return Base64Coder.encodeLines(outputStream.toByteArray()); // Base64 encoding is not strictly needed
        } catch (IOException e) {
            throw new RuntimeException("Error while writing itemstack", e);
        }
    }

    public static MyItemStack[] deserializeFromBase64(String encodedItems) {
        byte[] bytes = Base64Coder.decodeLines(encodedItems); // Base64 encoding is not strictly needed
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            DataInputStream input = new DataInputStream(inputStream);
            int count = input.readInt();
            MyItemStack[] items = new MyItemStack[count];
            for (int i = 0; i < count; i++) {
                int length = input.readInt();
                if (length == 0) {
                    continue;
                }

                byte[] itemBytes = new byte[length];
                input.read(itemBytes);
                items[i] = MyItemStack.deserializeBytes(itemBytes);
            }
            return items;
        } catch (IOException e) {
            throw new RuntimeException("Error while reading itemstack", e);
        }
    }

}
