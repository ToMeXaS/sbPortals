package lt.tomexas.sbportals.MainPortal.Listeners;

import com.bgsoftware.superiorskyblock.api.events.IslandCreateEvent;
import lt.tomexas.sbportals.Skyblock;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PortalPlayerListeners implements Listener {

    private final Skyblock plugin;

    public PortalPlayerListeners(Skyblock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void IslandCreateEvent(IslandCreateEvent event) {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            this.plugin.creator.createPortal(event.getIsland());
        }, 20L);
    }
}
