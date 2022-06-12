package lt.tomexas.sbportals.Utils;

import com.bgsoftware.superiorskyblock.api.island.Island;
import dev.lone.itemsadder.api.CustomBlock;
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
        Location location = island.getCenter(World.Environment.NORMAL).add(8.5, 0, 4.5);
        CustomBlock customBlock = CustomBlock.getInstance("portal");
        if (customBlock != null)
            customBlock.place(location);
    }
}
