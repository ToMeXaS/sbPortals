package lt.tomexas.sbportals.MainPortal.Inventories;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.guillaumevdn.questcreator.data.user.UserQC;
import com.guillaumevdn.questcreator.lib.quest.QuestEndType;
import dev.lone.itemsadder.api.CustomBlock;
import lt.tomexas.sbportals.Skyblock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.util.Collections;

public class MainPortalInventoryClickListener implements Listener {

    private Skyblock plugin;

    private final MainPortalInventory invInstance = new MainPortalInventory();
    private final Inventory inv;
    private final MainPortalInventory$1 invInstance$1 = new MainPortalInventory$1();
    private final Inventory inv$1;

    public MainPortalInventoryClickListener(Skyblock plugin) {
        this.plugin = plugin;

        inv = invInstance.getInventory();
        inv$1 = invInstance$1.getInventory();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().equals(inv)) {
            event.setCancelled(true);

            int clickedSlot = event.getRawSlot();
            for (int i : invInstance.getTown1slots()) {
                if (clickedSlot == i) {
                    UserQC.processWithQuests(player.getUniqueId(), user -> {
                        if(!user.getQuestHistory().hasElement("9", Collections.singleton(QuestEndType.SUCCESS))) {
                            player.closeInventory();
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cJūs dar negalite keliauti į šią visatą!"));
                        } else {
                            player.teleport(getSpawnLoc());
                        }
                    });
                }
            }

            for (int i : invInstance.getTown2slots()) {
                if (clickedSlot == i) {
                    player.closeInventory();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick$1(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().equals(inv$1)) {
            event.setCancelled(true);

            int clickedSlot = event.getRawSlot();
            for (int i : invInstance$1.getTown1slots()) {
                if (clickedSlot == i) {
                    UserQC.processWithQuests(player.getUniqueId(), user -> {
                        if(!user.getQuestHistory().hasElement("9", Collections.singleton(QuestEndType.SUCCESS))) {
                            player.closeInventory();
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cJūs dar negalite keliauti į šią visatą!"));
                        } else {
                            player.teleport(getSpawnLoc());
                        }
                    });
                }
            }

            for (int i : invInstance$1.getTown2slots()) {
                if (clickedSlot == i) {
                    player.closeInventory();
                }
            }
        }
    }

    @EventHandler
    public void onWalkOn(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();

        if (island == null) return;
        if (event.getTo() == null) return;
        if (event.getTo().getBlockX() == event.getFrom().getBlockX() && event.getTo().getBlockZ() == event.getFrom().getBlockZ()) return;

        CustomBlock customBlock = CustomBlock.byAlreadyPlaced(event.getTo().getBlock().getRelative(BlockFace.DOWN));
        if (customBlock != null) {
            UserQC.processWithQuests(player.getUniqueId(), user -> {
                if (!user.getQuestHistory().hasElement("9", Collections.singleton(QuestEndType.SUCCESS))) {
                    player.openInventory(inv);
                } else {
                    player.openInventory(inv$1);
                }
            });
        }

    }

    private Location getSpawnLoc() {
        File file = new File("plugins/Essentials/spawn.yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        String worldName = configuration.getString("spawns.default.world-name");
        float x = configuration.getLong("spawns.default.x");
        float y = configuration.getLong("spawns.default.y");
        float z = configuration.getLong("spawns.default.z");
        float yaw = configuration.getLong("spawns.default.yaw");
        float pitch = configuration.getLong("spawns.default.pitch");
        Location loc = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);

        return loc;
    }

    private Location getWarpLoc(String name) {
        File file = new File("plugins/Essentials/warps/" + name + ".yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        String worldName = configuration.getString("world-name");
        float x = configuration.getLong("x");
        float y = configuration.getLong("y");
        float z = configuration.getLong("z");
        float yaw = configuration.getLong("yaw");
        float pitch = configuration.getLong("pitch");
        Location loc = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);

        return loc;
    }

    public Inventory getInv() {
        return inv;
    }

    public Inventory getInv$1() {
        return inv$1;
    }
}
