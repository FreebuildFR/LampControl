/*
	Code has been adapted from jacklink01.
	Code is modified by Ceph.
	GNU General Public License version 3 (GPLv3)
*/
package cz.ceph.LampControl;

import cz.ceph.LampControl.listeners.LampListener;
import cz.ceph.LampControl.events.ReflectPlayerInteractEvent;
import cz.ceph.LampControl.utils.Commands;
import cz.ceph.LampControl.events.ReflectEvent;
import cz.ceph.LampControl.utils.SwitchBlock;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LampControl extends JavaPlugin {
    public static boolean opUsesHand = true, toggleLamps = true, takeItemOnUse = false, usePermissions = false, woodPlateControl = false, stonePlateControl = false, controlRails = true;
    public static PluginDescriptionFile pluginInfo;
    public static YamlConfiguration messagesConfig;
    private static final int CONFIG_VERSION = 7;

    public Material toolMaterial;
    private ReflectEvent reflectEvent;
    private List<Material> rMats = new ArrayList<>();
    private SwitchBlock switchBlock;


    @Override
    public void onDisable() {
        // Output info to console on disable
        getLogger().info(pluginInfo.getName() + " v" + pluginInfo.getVersion() + " was disabled!");
    }

    @Override
    public void onLoad() {
        reflectEvent = new ReflectEvent(this);
    }

    @Override
    public void onEnable() {
        // Create default config if not exist yet.
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }

        // Load config and messages.
        loadConfig();
        loadMessages();

        // Check for old config
        if (!getConfig().isSet("config-version") || getConfig().getInt("config-version") < CONFIG_VERSION) {
            saveResource("config.yml", true);
            getLogger().info("Older config version found, created new one.");
        }

        switchBlock = new SwitchBlock();

        // Register listeners
        LampListener lampListener = new LampListener(this);
        reflectEvent.initListener(lampListener);
        reflectEvent.registerPlayerInteractEvent(new ReflectPlayerInteractEvent(this));
        getServer().getPluginManager().registerEvents(lampListener, this);

        // Register commands executors
        Commands commandExecutor = new Commands(this);
        getCommand("lamp").setExecutor(commandExecutor);
        getCommand("/lamp").setExecutor(commandExecutor);
        getCommand("/lc").setExecutor(commandExecutor);

        // Output info to console on load
        pluginInfo = this.getDescription();
        getLogger().info(pluginInfo.getName() + " v" + pluginInfo.getVersion() + " was enabled!");

        rMats.add(Material.DETECTOR_RAIL);
        rMats.add(Material.POWERED_RAIL);
        rMats.add(Material.REDSTONE_WIRE);
        rMats.add(Material.REDSTONE_BLOCK);
        rMats.add(Material.PISTON);
        rMats.add(Material.REDSTONE_TORCH);
        rMats.add(Material.REDSTONE_WALL_TORCH);
        rMats.add(Material.REPEATER);
        rMats.add(Material.COMPARATOR);
        rMats.add(Material.LEVER);
        rMats.add(Material.STONE_BUTTON);
        rMats.add(Material.BIRCH_BUTTON);
        rMats.add(Material.ACACIA_BUTTON);
        rMats.add(Material.DARK_OAK_BUTTON);
        rMats.add(Material.JUNGLE_BUTTON);
        rMats.add(Material.OAK_BUTTON);
        rMats.add(Material.SPRUCE_BUTTON);
        rMats.add(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
        rMats.add(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
        rMats.add(Material.TRIPWIRE);
        rMats.add(Material.TRIPWIRE_HOOK);
        rMats.addAll(Arrays.stream(Material.values()).filter(mat -> mat.toString().equalsIgnoreCase("DAYLIGHT_DETECTOR") || mat.toString().equalsIgnoreCase("DAYLIGHT_DETECTOR_INVERTED")).collect(Collectors.toList()));
        if (stonePlateControl) {
            rMats.add(Material.BIRCH_PRESSURE_PLATE);
            rMats.add(Material.ACACIA_PRESSURE_PLATE);
            rMats.add(Material.DARK_OAK_PRESSURE_PLATE);
            rMats.add(Material.JUNGLE_PRESSURE_PLATE);
            rMats.add(Material.OAK_PRESSURE_PLATE);
            rMats.add(Material.SPRUCE_PRESSURE_PLATE);
        }
        if (woodPlateControl)
            rMats.add(Material.STONE_PRESSURE_PLATE);
    }

    // Load config from the file
    private void loadConfig() {
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
        reloadConfig();
        toolMaterial = Material.getMaterial(getConfig().getString("lampControlItem"));
        usePermissions = getConfig().getBoolean("usePermissions");
        woodPlateControl = getConfig().getBoolean("woodPlateControl");
        stonePlateControl = getConfig().getBoolean("stonePlateControl");
        opUsesHand = getConfig().getBoolean("opUsesHand");
        takeItemOnUse = getConfig().getBoolean("takeItemOnUse");
        toggleLamps = getConfig().getBoolean("toggleLamps");
        controlRails = getConfig().getBoolean("controlRails");
    }

    // Load messages from the file
    private void loadMessages() {
        File messages = new File(getDataFolder(), "messages.yml");
        if (!messages.exists()) {
            saveResource("messages.yml", false);
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messages);
    }

    public boolean containMaterials(Material mat) {
        return rMats.contains(mat);
    }

    // Get what version of Spigot you're using
    public static String getNMSVersion() {
        final String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }

    public SwitchBlock getSwitchBlock() {
        return switchBlock;
    }
}