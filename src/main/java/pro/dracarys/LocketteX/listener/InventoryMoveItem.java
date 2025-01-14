package pro.dracarys.LocketteX.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Hopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;
import pro.dracarys.LocketteX.api.LocketteXAPI;
import pro.dracarys.LocketteX.config.Config;
import pro.dracarys.LocketteX.utils.Util;

public class InventoryMoveItem implements Listener {

    @EventHandler
    public void onHopper(InventoryMoveItemEvent e) {
        if (!Config.USE_INV_MOVE.getOption() || !e.getDestination().getType().equals(InventoryType.HOPPER))
            return;
        InventoryHolder holder = e.getSource().getHolder();
        if (holder == null) return;
        Location loc = Util.getHolderLocation(holder);
        if (loc == null || !Util.isEnabledWorld(loc.getWorld().getName())) return;
        if (LocketteXAPI.getChestOwner(loc.getBlock().getState()) != null) {
            e.setCancelled(true);
            if (e.getDestination().getHolder() instanceof Hopper) {
                Hopper hopper = (Hopper) e.getDestination().getHolder();
                hopper.getBlock().setType(Material.AIR);
            }
        }
    }


}
