/*
	Code has been adapted from richie3366's LumosMaxima.
	Code is modified by Ceph.
	GNU General Public License version 3 (GPLv3)
*/
package cz.ceph.LampControl.utils;

import cz.ceph.LampControl.LampControl;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.Powerable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Ceph on 17.07.2016.
 */

public class SwitchBlock {

    private Object craftWorld;
    private Field isClientSideField;


    public void initWorld(World world) {
        try {
            craftWorld = getNMCWorld(getInstanceOfCW(getCraftWorld(world)));
            isClientSideField = craftWorld.getClass().getField("isClientSide");
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            try {
                isClientSideField = craftWorld.getClass().getField("isStatic");
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            }
        }

        isClientSideField.setAccessible(true);
    }

    private void setStatic(boolean value) {
        try {
            isClientSideField.set(craftWorld, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void switchLamp(Block block) {
        Lightable lightable = (Lightable) block.getState().getBlockData();
        setStatic(true);
        lightable.setLit(!lightable.isLit());
        block.getState().setBlockData(lightable);
        setStatic(false);
    }

    void switchLamp(Block block, boolean lit) {
        Lightable lightable = (Lightable) block.getState().getBlockData();
        setStatic(true);
        lightable.setLit(lit);
        block.getState().setBlockData(lightable);
        setStatic(false);
    }

    private Object getNMCWorld(Object cW) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + LampControl.getNMSVersion() + ".World", false, LampControl.class.getClassLoader()).cast(cW);
    }

    private Object getCraftWorld(Object worldInstance) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        return Class.forName("org.bukkit.craftbukkit." + LampControl.getNMSVersion() + ".CraftWorld", false, LampControl.class.getClassLoader()).cast(worldInstance);
    }

    private Object getInstanceOfCW(Object cW) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return cW.getClass().getDeclaredMethod("getHandle").invoke(cW);
    }

    public void switchRail(Block block) {
        Powerable powerable = (Powerable) block.getState().getBlockData();
        setStatic(true);
        powerable.setPowered(!powerable.isPowered());
        setStatic(false);
    }
}