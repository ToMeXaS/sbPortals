package lt.tomexas.sbportals.CraftablePortal.Packets;

import lt.tomexas.sbportals.Skyblock;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PortalHolo {

    private final Skyblock plugin;

    public PortalHolo (Skyblock plugin) {
        this.plugin = plugin;
    }

    public void createHolo(Location location) {

        if (this.plugin.portalHoloList.containsKey(location))
            return;
        if (this.plugin.portalDisplayName.get(location).isEmpty())
            return;

        Location loc = new Location(location.getWorld(), location.getX()+0.5, location.getY(), location.getZ()+0.5);
        String displayName;

        for (Player player : Bukkit.getOnlinePlayers()) {
            WorldServer world = ((CraftWorld) player.getWorld()).getHandle();
            EntityArmorStand as = new EntityArmorStand(world, loc.getX(), loc.getY(), loc.getZ());

            displayName = ChatColor.translateAlternateColorCodes('&', this.plugin.portalDisplayName.get(location));
            as.setCustomName(new ChatComponentText(displayName));

            as.setSmall(true);
            as.setInvisible(true);
            as.setCustomNameVisible(true);

            addHoloPacket(player, as);
            this.plugin.portalHoloList.put(location, as);
        }
    }

    public void addHoloPacket(Player player, EntityArmorStand as) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
        connection.sendPacket(new PacketPlayOutSpawnEntity(as));
        connection.sendPacket(new PacketPlayOutEntityMetadata(as.getId(), as.getDataWatcher(), true));
    }

    public void removeHoloPacket(Location location) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
            connection.sendPacket(new PacketPlayOutEntityDestroy(this.plugin.portalHoloList.get(location).getId()));
        }
        this.plugin.portalHoloList.remove(location);
    }
}
