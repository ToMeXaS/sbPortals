package lt.tomexas.sbportals.MainPortal.Inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class MainPortalInventory implements InventoryHolder {

    private final Inventory inv;
    private final int[] town1slots = { 0, 1, 2, 9, 10, 11 };
    private final int[] town2slots = { 3, 4, 5, 6, 7, 8, 12, 13, 14, 15, 16, 17, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44 };

    public MainPortalInventory() {
        inv = Bukkit.createInventory(null, 45, "" +
                "§f\uF808\uE003\uF80C\uF809\uF809\uF802§8\uE004§f\uF80A\uF808\uF802\uE007\uF827§8\uE008§f\uF80A\uF808\uF802\uE007\uF827§8\uE008§f\uF80A\uF808\uF802\uE007" +
                "\uF80C\uF809\uF802§8\uE010§f\uF80A\uF808\uF802\uE011\uF827§8\uE010§f\uF80A\uF808\uF802\uE011\uF827§8\uE010§f\uF80A\uF808\uF802\uE011");
        String town1name = "&e&lArchangel visata";
        List<String> town1lore = Arrays.asList(
                "",
                "&7 Tai pradinė ir pati pagrindinė",
                "&7visata, joje rasite viską, ko jums",
                "&7gali prireikti pačioje žaidimo pradžioje!",
                "",
                "&eReikalavimai:",
                " &e• &fĮvykdyti &e9 &fužduotis",
                "",
                "&c Jūs dar neatitinkate visų reikalavimų!",
                ""
        );
        init(town1name, town1lore, town1slots);

        String town2name = "&8&l???";
        List<String> town2lore = Arrays.asList(
                "",
                "&7???",
                "",
                "&8Reikalavimai:",
                " &8• &7???",
                ""
        );
        init(town2name, town2lore, town2slots);
    }

    private void init(String name, List<String> lore, int[] slots) {
        for (int i : slots)
            inv.setItem(i, createItem(Material.PAPER, name, lore, 1));
    }

    private ItemStack createItem(Material material, String displayName, List<String> lore, Integer customModelData) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (displayName != null)
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));

        if (lore != null)
            for (int i = 0; i < lore.size(); i++)
                lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
            meta.setLore(lore);

        if (customModelData != null)
            meta.setCustomModelData(customModelData);

        item.setItemMeta(meta);

        return item;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    public int[] getTown1slots() {
        return town1slots;
    }

    public int[] getTown2slots() {
        return town2slots;
    }
}
