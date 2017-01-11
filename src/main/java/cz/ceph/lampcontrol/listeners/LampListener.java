package cz.ceph.lampcontrol.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

import static cz.ceph.lampcontrol.LampControl.getMain;

public class LampListener implements Listener {

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent e) {
        boolean lampItem = e.getBlock().getType().equals(Material.REDSTONE_LAMP_ON);

        if (!getMain().cachedBooleanValues.get("use-plate-wooden") || !getMain().cachedBooleanValues.get("use-plate-stone") || lampItem && !getMain().containMaterials(e.getChangedType())) {
            e.setCancelled(true);
        }
    }
}