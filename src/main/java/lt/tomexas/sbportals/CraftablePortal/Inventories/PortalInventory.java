package lt.tomexas.sbportals.CraftablePortal.Inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class PortalInventory implements InventoryHolder {

    private final Inventory inv;

    public PortalInventory () {
        inv = Bukkit.createInventory(null, 27, "Portalo nustatymai");
        init();
    }

    private void init() {
        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, createItem(Material.BLACK_STAINED_GLASS_PANE, "", null));
    }

    private ItemStack createItem(Material material, String displayName, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (displayName != null)
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));

        if (lore != null)
            for (int i = 0; i < lore.size(); i++)
                lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));


        item.setItemMeta(meta);

        return item;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
