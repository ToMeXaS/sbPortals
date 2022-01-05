package lt.tomexas.sbportals.MainPortal.Listeners;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.events.IslandChunkResetEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandCreateEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.guillaumevdn.gcore.GCore;
import com.guillaumevdn.questcreator.QuestCreator;
import com.guillaumevdn.questcreator.data.user.UserQC;
import com.guillaumevdn.questcreator.lib.quest.QuestEndType;
import lt.tomexas.sbportals.MainPortal.Inventories.MainPortalInventory;
import lt.tomexas.sbportals.MainPortal.Inventories.MainPortalInventory$1;
import lt.tomexas.sbportals.MainPortal.Inventories.MainPortalInventoryClickListener;
import lt.tomexas.sbportals.Skyblock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class PortalPlayerListeners implements Listener {

    private final Skyblock plugin;

    public PortalPlayerListeners(Skyblock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPortalHit(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();
        Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();

        if (island == null) return;

        if (action.equals(Action.LEFT_CLICK_BLOCK)) {
            Block block = event.getClickedBlock();
            Location loc = this.plugin.portalLoc.get(island);
            Location blockLoc = block.getLocation();
            if (block.getType().equals(Material.END_PORTAL_FRAME) && blockLoc.equals(loc)) {
                this.plugin.creator.removePortal(island);
                block.setType(Material.AIR);
                player.getInventory().addItem(createItem(Material.END_PORTAL_FRAME, "&cPortalas", null));
            }
        }
    }

    @EventHandler
    public void onPortalPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();
        Block block = event.getBlock();
        Island islandAt = SuperiorSkyblockAPI.getIslandAt(block.getLocation());

        if (island == null) return;

        if (block.getType().equals(Material.END_PORTAL_FRAME)
                && player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&cPortalas"))
                && island == islandAt) {
            this.plugin.creator.createPortalAt(island, block.getLocation());
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();

        if (island == null) return;

        this.plugin.creator.createPortalParticle(island);
    }

    @EventHandler
    public void IslandCreateEvent(IslandCreateEvent event) {
        this.plugin.creator.createPortal(event.getIsland());
    }

    @EventHandler
    public void IslandDisbandEvent(IslandDisbandEvent event) {
        this.plugin.creator.removePortal(event.getIsland());
    }

    private ItemStack createItem(Material material, String displayName, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (!displayName.isEmpty())
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));

        if (lore != null)
            for (int i = 0; i < lore.size(); i++)
                lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));

        item.setItemMeta(meta);

        return item;
    }
}
