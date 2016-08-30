/*
	Code has been adapted from gomeow.
	Code is modified by Ceph.
	GNU General Public License version 3 (GPLv3)
*/
package cz.ceph.LampControl;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Created by Granik24 on 09.08.2016.
 */

public enum MessagesManager {
    PREFIX("plugin-prefix", "&8[&eLamp&cControl&8]&r"),
    TOO_MANY_ARGUMENTS("too-many-arguments", "&cToo many arguments!"),
    NO_WORLDEDIT("no-worldedit", "&cWorldEdit isn't installed. &aInstall &cit, if you need this feature."),
    NO_SELECTION("no-selection", "&cMake a region selection first."),
    NO_LAMPS_AFFECTED("no-lamps-affected", "&dNo lamps were affected."),
    ON_LAMPS("affected-lamps", "&f%affected &dlamps were turned on."),
    OFF_LAMPS("affected-lamps", "&f%affected &dlamps were turned off."),
    CONSOLE("console", "&cThis command cannot be run from the console.."),
    NO_PERMS("no-permissions", "&cYou don't have permissions to build here!");

    private String path;
    private String def;
    private static YamlConfiguration Config;

    MessagesManager(String path, String start) {
        this.path = path;
        this.def = start;
    }

    public static void setFile(YamlConfiguration config) {
        Config = config;
    }

    public String toString() {
        if (this == PREFIX)
            return ChatColor.translateAlternateColorCodes('&', Config.getString(this.path, def)) + " ";
        return ChatColor.translateAlternateColorCodes('&', Config.getString(this.path, def));
    }
}