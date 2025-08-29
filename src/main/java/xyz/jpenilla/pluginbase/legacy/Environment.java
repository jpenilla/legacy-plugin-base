package xyz.jpenilla.pluginbase.legacy;

import org.bukkit.Bukkit;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Method;

@NullMarked
public class Environment {
    private static final boolean paper;
    private static final int majorMinecraftVersion;

    static {
        paper = classExists("com.destroystokyo.paper.PaperConfig")
                || classExists("io.papermc.paper.configuration.Configuration");
        final String serverPackageName = Bukkit.getServer().getClass().getPackage().getName();
        final String serverApiVersion = serverPackageName.substring(
                serverPackageName.lastIndexOf('.') + 1);
        int majorVer = -1;
        try {
            majorVer = Integer.parseInt(serverApiVersion.split("_")[1]);
        } catch (final NumberFormatException | ArrayIndexOutOfBoundsException e) {
            // No relocation (Paper 1.20.5+):
            try {
                final Class<?> sharedConstants = Class.forName("net.minecraft.SharedConstants");
                final Method getCurrentVersion = sharedConstants.getDeclaredMethod("getCurrentVersion");
                final Object currentVersion = getCurrentVersion.invoke(null);
				Method getName = null;
				try {
					getName = currentVersion.getClass().getDeclaredMethod("getName");
				} catch (final NoSuchMethodException ignored) {
				}
				if (getName == null) {
					// ~1.21.6+
					getName = currentVersion.getClass().getDeclaredMethod("name");
				}
                final String versionName = (String) getName.invoke(currentVersion);
                majorVer = Integer.parseInt(versionName.split("\\.")[1]);
            } catch (final ReflectiveOperationException ex) {
                throw new RuntimeException(ex);
            }
        }
        majorMinecraftVersion = majorVer;
    }

    public static boolean classExists(final String fullyQualifiedName) {
        try {
            Class.forName(fullyQualifiedName);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean paper() {
        return Environment.paper;
    }

    public static int majorMinecraftVersion() {
        return Environment.majorMinecraftVersion;
    }
}
