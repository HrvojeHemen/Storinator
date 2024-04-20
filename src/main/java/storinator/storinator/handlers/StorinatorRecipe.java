package storinator.storinator.handlers;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class StorinatorRecipe {

    public final static ItemStack STORINATOR_ITEM;
    public final static String RECIPE_KEY = "chest";
    public final static Integer STORINATOR_CUSTOM_MODEL_DATA = "STORINATOR_CUSTOM_MODEL_DATA".hashCode();

    private final JavaPlugin storinator;

    static {
        STORINATOR_ITEM = new ItemStack(Material.IRON_BLOCK, 1);
        ItemMeta meta = STORINATOR_ITEM.getItemMeta();
        meta.displayName(Component.text().content("Storinator").build());
        meta.setCustomModelData(STORINATOR_CUSTOM_MODEL_DATA);

        STORINATOR_ITEM.setItemMeta(meta);
    }

    public void addRecipe() {
        Bukkit.addRecipe(getRecipe());
    }

    public void removeRecipe() {
        Bukkit.removeRecipe(new NamespacedKey(storinator, RECIPE_KEY));
    }

    public static ItemStack getStorinatorItem(String storageId) {
        ItemStack is = new ItemStack(STORINATOR_ITEM);

        ItemMeta meta = is.getItemMeta();
        List<Component> lore = meta.lore() == null ? new ArrayList<>() : meta.lore();

        meta.setCustomModelData(STORINATOR_CUSTOM_MODEL_DATA);
        meta.getPersistentDataContainer().set(NamespacedKey.fromString(RECIPE_KEY), PersistentDataType.STRING, storageId);
        lore.add(Component.text().content("storageId:" + storageId).build());
        meta.lore(lore);
        is.setItemMeta(meta);

        return is;
    }

    private ShapedRecipe getRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(storinator, RECIPE_KEY), STORINATOR_ITEM);
        recipe.shape("WWW", "WAW", "WWW");
        recipe.setIngredient('W', Material.OAK_PLANKS);
        recipe.setIngredient('A', Material.AMETHYST_SHARD);

        return recipe;
    }

}
