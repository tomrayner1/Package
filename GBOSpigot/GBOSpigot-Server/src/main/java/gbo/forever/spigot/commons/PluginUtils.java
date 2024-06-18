package gbo.forever.spigot.commons;
import org.bukkit.plugin.Plugin;

/**
 * @author Elierrr
 */
public class PluginUtils {
    public static int getCitizensBuild(Plugin plugin) {
        try {
            return Integer.parseInt(plugin.getDescription().getVersion().split("\\(build ")[1].replace(")", ""));
        } catch (Throwable ignored) {
            return 2396;
        }
    }
}