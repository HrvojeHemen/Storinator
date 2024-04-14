package storinator.storinator.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class MyItemStack extends ItemStack {

    private int count;

    public MyItemStack(MyItemStack myItemStack) {
        super(myItemStack);
        this.count = myItemStack.getCount();
    }

    public MyItemStack(@NotNull Material type, int count) {
        super(type);
        this.count = count;
    }

    public MyItemStack(@NotNull ItemStack stack) throws IllegalArgumentException {
        super(stack);
        this.count = stack.getAmount();
        this.setAmount(1);
    }

    @NotNull
    public static MyItemStack deserializeBytes(@NotNull byte[] bytes) {


        try{
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            DataInputStream dis = new DataInputStream(bais);

            // Read the count
            int count = dis.readInt();

            // Calculate the length of the remaining bytes
            int remainingLength = bytes.length - Integer.BYTES;

            // Create a byte array to hold the remaining bytes
            byte[] remainingBytes = new byte[remainingLength];

            // Read the remaining bytes
            dis.readFully(remainingBytes);

            // Deserialize the MyItemStack using the remainingBytes
            ItemStack itemStack = ItemStack.deserializeBytes(remainingBytes);


            MyItemStack myItemStack = new MyItemStack(itemStack);
            // Set the count
            myItemStack.setCount(count);

            return myItemStack;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte @NotNull [] serializeAsBytes() {
        // Serialize count
        ByteArrayOutputStream countBaos = new ByteArrayOutputStream();
        DataOutputStream countDos = new DataOutputStream(countBaos);
        try {
            countDos.writeInt(count);
            countDos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] countSerialized = countBaos.toByteArray();

        // Serialize itemStack
        byte[] itemStackSerialized = super.serializeAsBytes();

        // Concatenate countSerialized and itemStackSerialized
        byte[] combined = new byte[countSerialized.length + itemStackSerialized.length];
        System.arraycopy(countSerialized, 0, combined, 0, countSerialized.length);
        System.arraycopy(itemStackSerialized, 0, combined, countSerialized.length, itemStackSerialized.length);

        return combined;
    }

    @Override
    public String toString() {
        return super.toString() + " x" + count;
    }
}

