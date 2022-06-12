package lt.tomexas.sbportals;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import lt.tomexas.sbportals.CraftablePortal.Listeners.CPortalPlayerListeners;
import lt.tomexas.sbportals.CraftablePortal.Packets.PortalHolo;
import lt.tomexas.sbportals.MainPortal.Inventories.MainPortalInventoryClickListener;
import lt.tomexas.sbportals.MainPortal.Listeners.PortalPlayerListeners;
import lt.tomexas.sbportals.Recipes.*;
import lt.tomexas.sbportals.Utils.Creator;
import lt.tomexas.sbportals.Utils.SaveRestore;
import lt.tomexas.sbportals.Utils.portalFileManager;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Skyblock extends JavaPlugin {

    private static Skyblock instance;

    public SaveRestore saveRestore;
    public Creator creator;
    public PortalHolo portalHolo;

    public portalFileManager portalFile;
    public Map<Island, Location> portalLoc = new HashMap<>();
    public Map<Island, Map<Location, Location>> cPortalLoc = new HashMap<>();
    public Map<Location, Location> portalHash = new HashMap<>();
    public Map<Location, Integer> portalTiers = new HashMap<>();
    public Map<Location, EntityArmorStand> portalHoloList = new HashMap<>();
    public Map<Location, String> portalDisplayName = new HashMap<>();
    public Map<Location, Integer> particleTasks = new HashMap<>();

    public List<ShapedRecipe> recipeList = new ArrayList<>();

    public void onLoad() {
        instance = this;

        this.portalFile = new portalFileManager();
    }

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new MainPortalInventoryClickListener(this), this);
        pm.registerEvents(new PortalPlayerListeners(this), this);
        pm.registerEvents(new CPortalPlayerListeners(this), this);

        this.saveRestore = new SaveRestore(this);
        this.creator = new Creator(this);
        this.portalHolo = new PortalHolo(this);

        for (Island island : SuperiorSkyblockAPI.getGrid().getIslands()) {
            this.saveRestore.restorePortalLoc(island);
            this.saveRestore.restoreCPortalLoc(island);
        }

        for (Location location : portalHash.keySet())
            createParticles(location);

        for (Location location : portalDisplayName.keySet())
            this.portalHolo.createHolo(location);

        TierOnePortalRecipe.register();
        TierTwoPortalRecipe.register();
        TierThreePortalRecipe.register();
        TierFourPortalRecipe.register();
        TierFivePortalRecipe.register();
        TierSixPortalRecipe.register();
    }

    @Override
    public void onDisable() {
        for (ShapedRecipe recipe : recipeList)
            removeRecipe(recipe);

        for (Island island : portalLoc.keySet()) {
            this.saveRestore.savePortalLoc(island);
            this.saveRestore.saveCPortalLoc(island);
        }

        for(Location location : portalHash.keySet())
            removeParticles(location);

        for (Location location : portalDisplayName.keySet())
            if (!portalDisplayName.get(location).isEmpty())
                this.portalHolo.removeHoloPacket(location);
    }

    public void removeParticles(Location location) {
        Bukkit.getScheduler().cancelTask(this.particleTasks.get(location));
    }

    public void createParticles(Location location) {
        int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () ->
                        location.getWorld().spawnParticle(Particle.REDSTONE, location.getX()+0.5, location.getY()+0.9, location.getZ()+0.5,
                                10, new Particle.DustOptions(Color.fromBGR(255,0,255), 1))
                , 20, 0);
        this.particleTasks.put(location, task);
    }

    private void removeRecipe(ShapedRecipe inputRecipe){
        Iterator<Recipe> it = getServer().recipeIterator();
        while(it.hasNext()){
            Recipe itRecipe = it.next();
            if(itRecipe instanceof ShapedRecipe){
                ShapedRecipe itShaped = (ShapedRecipe) itRecipe;

                Map<Character, ItemStack> m = itShaped.getIngredientMap();
                Map<Character, ItemStack> n = inputRecipe.getIngredientMap();

                if(m.values().containsAll(n.values())){
                    String[] list = itShaped.getShape();
                    String listString = list[0] + list[1] + list[2];

                    String[] list2 = inputRecipe.getShape();
                    String listString2 = list2[0] + list2[1] + list2[2];

                    for(int i = 0; i < listString.length(); i++){
                        if(!m.get(listString.charAt(i)).equals(n.get(listString2.charAt(i)))){
                            getLogger().fine("Recipe not found.");
                            return;
                        }
                    }
                    it.remove();
                    getLogger().fine("Recipe removed!");
                }
            }
        }
    }

    public static Skyblock getPlugin(){
        return instance;
    }

}
