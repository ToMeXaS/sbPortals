package lt.tomexas.sbportals.Utils;

import com.bgsoftware.superiorskyblock.api.island.Island;
import lt.tomexas.sbportals.Skyblock;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

public class SaveRestore {

    private final Skyblock plugin;

    public SaveRestore(Skyblock plugin) {
        this.plugin = plugin;
    }

    public void savePortalLoc(Island island) {
        if (!this.plugin.portalLoc.containsKey(island)) return;
        Location location = this.plugin.portalLoc.get(island);
        this.plugin.portalFile.getConfig().set("mainPortals." + island.getOwner().getName() + ".location", location);
        this.plugin.portalFile.saveConfig();
    }

    public void restorePortalLoc(Island island) {
        if (this.plugin.portalFile.getConfig().get("mainPortals." + island.getOwner().getName() + ".location") == null) return;
        Location loc = this.plugin.portalFile.getConfig().getLocation("mainPortals." + island.getOwner().getName() + ".location");
        this.plugin.portalLoc.put(island, loc);
        this.plugin.portalFile.getConfig().set("mainPortals." + island.getOwner().getName(), null);
        this.plugin.portalFile.saveConfig();
    }

    public void saveCPortalLoc(Island island) {
        if (!this.plugin.cPortalLoc.containsKey(island)) return;
        Map<Location, Location> portalHash = this.plugin.cPortalLoc.get(island);

        int i = 0;
        for (Map.Entry<Location, Location> map : portalHash.entrySet()) {
            i++;
            this.plugin.portalFile.getConfig().set("craftPortals." + island.getUniqueId() + "." + i + ".tier", this.plugin.portalTiers.get(map.getKey()));
            this.plugin.portalFile.getConfig().set("craftPortals." + island.getUniqueId() + "." + i + ".from", map.getKey());
            this.plugin.portalFile.getConfig().set("craftPortals." + island.getUniqueId() + "." + i + ".to", map.getValue());
            if (this.plugin.portalDisplayName.get(map.getKey()) != null)
                this.plugin.portalFile.getConfig().set("craftPortals." + island.getUniqueId() + "." + i + ".displayname", this.plugin.portalDisplayName.get(map.getKey()));
            else
                this.plugin.portalFile.getConfig().set("craftPortals." + island.getUniqueId() + "." + i + ".displayname", "");
        }

        this.plugin.portalFile.saveConfig();
    }

    public void restoreCPortalLoc(Island island) {
        if (this.plugin.portalFile.getConfig().get("craftPortals." + island.getUniqueId()) == null) return;
        ConfigurationSection configSec = this.plugin.portalFile.getConfig().getConfigurationSection("craftPortals." + island.getUniqueId());
        for (String key : configSec.getKeys(false)) {
            Map<Location, Location> map = this.plugin.portalHash;
            map.put(this.plugin.portalFile.getConfig().getLocation("craftPortals." + island.getUniqueId() + "." + key + ".from"),
                    this.plugin.portalFile.getConfig().getLocation("craftPortals." + island.getUniqueId() + "." + key + ".to"));

            Map<Location, Integer> tierMap = this.plugin.portalTiers;
            tierMap.put(this.plugin.portalFile.getConfig().getLocation("craftPortals." + island.getUniqueId() + "." + key + ".from"),
                    this.plugin.portalFile.getConfig().getInt("craftPortals." + island.getUniqueId() + "." + key + ".tier"));

            Map<Location, String> nameMap = this.plugin.portalDisplayName;
            nameMap.put(this.plugin.portalFile.getConfig().getLocation("craftPortals." + island.getUniqueId() + "." + key + ".from"),
                    this.plugin.portalFile.getConfig().getString("craftPortals." + island.getUniqueId() + "." + key + ".displayname"));

            this.plugin.cPortalLoc.put(island, map);
        }
        this.plugin.portalFile.getConfig().set("craftPortals." + island.getUniqueId(), null);
        this.plugin.portalFile.saveConfig();
    }
}
