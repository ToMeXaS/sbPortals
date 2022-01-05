package lt.tomexas.sbportals.Recipes;

import lt.tomexas.sbportals.Skyblock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class TierSixPortalRecipe {

    public static void register() {
        ItemStack item = new ItemStack(Material.END_PORTAL_FRAME);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e★★★★★★ &fPortalas"));
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Skyblock.getPlugin(Skyblock.class), "tier6portal");
        ShapedRecipe recipe = new ShapedRecipe(key, item);

        recipe.shape("DND", "EPE", "EEE");

        recipe.setIngredient('D', Material.DARK_PRISMARINE);
        recipe.setIngredient('E', Material.END_STONE);
        recipe.setIngredient('P', Material.ENDER_PEARL);
        recipe.setIngredient('N', Material.NETHERITE_INGOT);

        Skyblock.getPlugin().recipeList.add(recipe);

        Bukkit.addRecipe(recipe);
    }
}
