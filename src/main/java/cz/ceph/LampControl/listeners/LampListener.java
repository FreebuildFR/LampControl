package cz.ceph.LampControl.listeners;


import cz.ceph.LampControl.LampControl;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;


public class LampListener implements Listener {
    private LampControl plugin;

    public LampListener(LampControl p) {
        this.plugin = p;
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent e) {
        boolean lamp = e.getBlock().getType().equals(Material.REDSTONE_LAMP);

        if (!LampControl.woodPlateControl || !LampControl.stonePlateControl || lamp && !plugin.containMaterials(e.getChangedType())) {
                e.setCancelled(true);
        }
    }
}