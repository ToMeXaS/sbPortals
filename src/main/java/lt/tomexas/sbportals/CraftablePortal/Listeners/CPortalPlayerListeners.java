package lt.tomexas.sbportals.CraftablePortal.Listeners;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import lt.tomexas.sbportals.CraftablePortal.Inventories.PortalInventory;
import lt.tomexas.sbportals.Skyblock;
import net.minecraft.network.protocol.game.PacketPlayOutAnimation;
import net.minecraft.network.protocol.game.PacketPlayOutWorldParticles;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class CPortalPlayerListeners implements Listener {

    private final Skyblock plugin;
    private final Inventory inv;

    String rangeError = ChatColor.translateAlternateColorCodes('&', "&e&lPortalai | &7Negalite pastatyti šio lygio portalo, taip toli nuo jo teleportuojamos vietos.");
    String locationSet = ChatColor.translateAlternateColorCodes('&', "&e&lPortalai | &7Lokacija buvo išsaugota sėkmingai.");
    String locationNotSet = ChatColor.translateAlternateColorCodes('&', "&e&lPortalai | &7Portalo lokacija nėra nustatyta šiam portalui.");
    String portalCreated = ChatColor.translateAlternateColorCodes('&', "&e&lPortalai | &7Portalas buvo sukurtas sėkmingai.");
    String portalBroken = ChatColor.translateAlternateColorCodes('&', "&e&lPortalai | &7Portalas buvo išgriautas.");
    String cantPlaceInWorld = ChatColor.translateAlternateColorCodes('&', "&e&lPortalai | &7Šiame pasaulyje statyti portalo negalite.");
    String cantSetInWorld = ChatColor.translateAlternateColorCodes('&', "&e&lPortalai | &7Šiame pasaulyje nustatyti teleportacijos lokacijos negalite.");

    public CPortalPlayerListeners (Skyblock plugin) {
        this.plugin = plugin;
        inv = new PortalInventory().getInventory();
    }

    @EventHandler
    public void setPortalTPLoc(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();
        Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();

        if (island == null) return;

        if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)) {
            if (player.isSneaking() && player.getInventory().getItemInMainHand().getType().equals(Material.END_PORTAL_FRAME)) {
                if (ChatColor.stripColor(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).contains("★")) {
                    event.setCancelled(true);
                    ItemStack item = player.getInventory().getItemInMainHand();
                    ItemMeta meta = item.getItemMeta();

                    if(!meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) return;

                    if (!player.getWorld().getName().equalsIgnoreCase("superiorworld")) {
                        event.setCancelled(true);
                        player.sendMessage(cantSetInWorld);
                        return;
                    }

                    List<String> lore = Arrays.asList(
                            "&8" + player.getLocation().getBlockX() + " " + player.getLocation().getBlockY() + " " + player.getLocation().getBlockZ() + " "
                                    + player.getLocation().getYaw() + " " + player.getLocation().getPitch()
                    );

                    for (int i = 0; i < lore.size(); i++)
                        lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));

                    meta.setLore(lore);
                    item.setItemMeta(meta);

                    player.getInventory().setItemInMainHand(item);
                    player.sendMessage(locationSet);
                }
            }
        }
    }


    @EventHandler
    public void onCPortalPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();

        if (island == null) return;

        if (block.getType().equals(Material.END_PORTAL_FRAME)) {
            if (player.getInventory().getItemInMainHand().getItemMeta() == null) return;
            if (ChatColor.stripColor(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).contains("★")) {
                ItemStack item = player.getInventory().getItemInMainHand();
                ItemMeta meta = item.getItemMeta();
                if (meta.getLore() == null) {
                    event.setCancelled(true);
                    player.sendMessage(locationNotSet);
                    return;
                }

                if (!player.getWorld().getName().equalsIgnoreCase("superiorworld")) {
                    event.setCancelled(true);
                    player.sendMessage(cantPlaceInWorld);
                    return;
                }

                if(!meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) return;

                String lore = meta.getLore().get(0);
                String[] splitLore = lore.split(" ");

                Location loc = new Location(player.getWorld(), Double.parseDouble(splitLore[0].replace("§8", "")), Double.parseDouble(splitLore[1]), Double.parseDouble(splitLore[2]), Float.parseFloat(splitLore[3]), Float.parseFloat(splitLore[4]));

                switch (ChatColor.stripColor(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName())) {
                    case "★☆☆☆☆☆ Portalas": {
                        if (block.getLocation().distance(loc) <= 10) {
                            this.plugin.portalHash.put(block.getLocation(), loc);
                            this.plugin.cPortalLoc.put(island, this.plugin.portalHash);
                            this.plugin.portalTiers.put(block.getLocation(), 1);
                            this.plugin.createParticles(block.getLocation());
                            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                            player.sendMessage(portalCreated);
                        } else {
                            event.setCancelled(true);
                            player.sendMessage(rangeError);
                        }
                        break;
                    }
                    case "★★☆☆☆☆ Portalas": {
                        if (block.getLocation().distance(loc) <= 30) {
                            this.plugin.portalHash.put(block.getLocation(), loc);
                            this.plugin.cPortalLoc.put(island, this.plugin.portalHash);
                            this.plugin.portalTiers.put(block.getLocation(), 2);
                            this.plugin.createParticles(block.getLocation());
                            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                            player.sendMessage(portalCreated);
                        } else {
                            event.setCancelled(true);
                            player.sendMessage(rangeError);
                        }
                        break;
                    }
                    case "★★★☆☆☆ Portalas": {
                        if (block.getLocation().distance(loc) <= 50) {
                            this.plugin.portalHash.put(block.getLocation(), loc);
                            this.plugin.cPortalLoc.put(island, this.plugin.portalHash);
                            this.plugin.portalTiers.put(block.getLocation(), 3);
                            this.plugin.createParticles(block.getLocation());
                            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                            player.sendMessage(portalCreated);
                        } else {
                            event.setCancelled(true);
                            player.sendMessage(rangeError);
                        }
                        break;
                    }
                    case "★★★★☆☆ Portalas": {
                        if (block.getLocation().distance(loc) <= 80) {
                            this.plugin.portalHash.put(block.getLocation(), loc);
                            this.plugin.cPortalLoc.put(island, this.plugin.portalHash);
                            this.plugin.portalTiers.put(block.getLocation(), 4);
                            this.plugin.createParticles(block.getLocation());
                            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                            player.sendMessage(portalCreated);
                        } else {
                            event.setCancelled(true);
                            player.sendMessage(rangeError);
                        }
                        break;
                    }
                    case "★★★★★☆ Portalas": {
                        if (block.getLocation().distance(loc) <= 100) {
                            this.plugin.portalHash.put(block.getLocation(), loc);
                            this.plugin.cPortalLoc.put(island, this.plugin.portalHash);
                            this.plugin.portalTiers.put(block.getLocation(), 5);
                            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                            player.sendMessage(portalCreated);
                        } else {
                            event.setCancelled(true);
                            player.sendMessage(rangeError);
                        }
                        break;
                    }
                    case "★★★★★★ Portalas": {
                        if (block.getLocation().distance(loc) <= 120) {
                            this.plugin.portalHash.put(block.getLocation(), loc);
                            this.plugin.cPortalLoc.put(island, this.plugin.portalHash);
                            this.plugin.portalTiers.put(block.getLocation(), 6);
                            this.plugin.createParticles(block.getLocation());
                            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                            player.sendMessage(portalCreated);
                        } else {
                            event.setCancelled(true);
                            player.sendMessage(rangeError);
                        }
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPortalBreak(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();

        if (!event.hasBlock()) return;
        if (!action.equals(Action.LEFT_CLICK_BLOCK)) return;
        if (event.getClickedBlock() == null) return;
        if (island == null) return;
        if (this.plugin.cPortalLoc.get(island) == null) return;

        Map<Location, Location> portalLocations = this.plugin.cPortalLoc.get(island);
        Location blockLoc = event.getClickedBlock().getLocation();

        for (Location location : portalLocations.keySet()) {
            if (player.getWorld().getName().equals("SuperiorWorld")
                    && blockLoc.equals(location)) {
                event.setCancelled(true);
                event.getClickedBlock().setType(Material.AIR);
                player.getInventory().addItem(portalItem(this.plugin.portalTiers.get(blockLoc), portalLocations.get(location)));

                this.plugin.cPortalLoc.remove(island);
                this.plugin.portalHash.remove(blockLoc);
                this.plugin.portalTiers.remove(blockLoc);

                if (this.plugin.portalHoloList.get(blockLoc) != null)
                    this.plugin.portalHolo.removeHoloPacket(blockLoc);

                this.plugin.portalHoloList.remove(blockLoc);
                this.plugin.portalDisplayName.remove(blockLoc);

                this.plugin.removeParticles(blockLoc);

                player.sendMessage(portalBroken);
            }
        }
    }

    @EventHandler
    public void onPortalInvOpen(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();

        if (!event.hasBlock()) return;
        if (!action.equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (event.getClickedBlock() == null) return;
        if (island == null) return;
        if (this.plugin.cPortalLoc.get(island) == null) return;

        Map<Location, Location> portalLocations = this.plugin.cPortalLoc.get(island);
        Location blockLoc = event.getClickedBlock().getLocation();

        for (Location location : portalLocations.keySet()) {
            if (player.getWorld().getName().equals("SuperiorWorld")
                    && blockLoc.equals(location)) {
                event.setCancelled(true);

                PacketPlayOutAnimation animation = new PacketPlayOutAnimation(((CraftPlayer) player).getHandle(), 0);
                ((CraftPlayer) player).getHandle().b.sendPacket(animation);

                player.openInventory(inv);
            }
        }
    }

    @EventHandler
    public void onWalkOn(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();

        if (island == null) return;
        if (!player.isOp()) return;
        if (this.plugin.cPortalLoc.get(island) == null) return;

        Map<Location, Location> portalLocations = this.plugin.cPortalLoc.get(island);

        for (Location location : portalLocations.keySet()) {
            if (player.getWorld().getName().equals("SuperiorWorld")
                    && !event.getFrom().getBlock().getType().equals(Material.END_PORTAL_FRAME)
                    && event.getTo().getBlock().getType().equals(Material.END_PORTAL_FRAME)
                    && event.getTo().getBlock().getLocation().equals(location)) {
                Location tpLocation = new Location(portalLocations.get(location).getWorld(),
                        portalLocations.get(location).getX()+0.5,
                        portalLocations.get(location).getY(),
                        portalLocations.get(location).getZ()+0.5,
                        portalLocations.get(location).getYaw(),
                        portalLocations.get(location).getPitch());
                player.teleport(tpLocation);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lPortalai | &7Jūs buvote nuteleportuotas į nustatytą lokaciją!"));
            }
        }
    }

    private ItemStack portalItem(int tier, Location location) {
        ItemStack item = new ItemStack(Material.END_PORTAL_FRAME);
        ItemMeta meta = item.getItemMeta();

        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getTierName(tier)));

        List<String> lore = Collections.singletonList("§8" + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + " "
                + location.getYaw() + " " + location.getPitch());
        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;
    }

    private String getTierName(int tier) {
        switch (tier) {
            case 1:
                return "&e★&7☆☆☆☆☆ &fPortalas";
            case 2:
                return "&e★★&7☆☆☆☆ &fPortalas";
            case 3:
                return "&e★★★&7☆☆☆ &fPortalas";
            case 4:
                return "&e★★★★&7☆☆ &fPortalas";
            case 5:
                return "&e★★★★★&7☆ &fPortalas";
            case 6:
                return "&e★★★★★★ &fPortalas";
        }
        return null;
    }
}
