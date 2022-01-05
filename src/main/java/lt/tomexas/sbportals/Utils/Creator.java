package lt.tomexas.sbportals.Utils;

import com.bgsoftware.superiorskyblock.api.island.Island;
import lt.tomexas.sbportals.Skyblock;
import org.bukkit.*;

import java.util.HashMap;
import java.util.Map;

public class Creator {

    private final Skyblock plugin;
    private final Map<Island, Integer> tasks = new HashMap<>();

    public Creator (Skyblock plugin) {
        this.plugin = plugin;
    }

    public void createPortal(Island island) {
        Location portalLoc = island.getCenter(World.Environment.NORMAL).add(8.5, 0, 4.5);
        portalLoc.getWorld().getBlockAt(portalLoc).setType(Material.END_PORTAL_FRAME);

        this.plugin.portalLoc.put(island, portalLoc);
        createPortalParticle(island);
    }

    public void createPortalAt(Island island, Location location) {
        location.getWorld().getBlockAt(location).setType(Material.END_PORTAL_FRAME);

        this.plugin.portalLoc.put(island, location);
        createPortalParticle(island);
    }

    public void createPortalParticle(Island island) {
        if (this.plugin.portalLoc.get(island) == null) return;
        Location loc = this.plugin.portalLoc.get(island);
        int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
            loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX()+0.5, loc.getY()+0.9, loc.getZ()+0.5,
                    10, new Particle.DustOptions(Color.fromBGR(255,0,255), 1));
        }, 20, 0);
        this.tasks.put(island, task);
    }

    public void removePortal(Island island) {
        this.plugin.portalLoc.remove(island);
        removePortalParticle(island);
    }

    private void removePortalParticle(Island island) {
        Bukkit.getScheduler().cancelTask(this.tasks.get(island));
        this.tasks.remove(island);
    }
}
