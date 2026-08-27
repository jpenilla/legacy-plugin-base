package xyz.jpenilla.pluginbase.legacy.environment;

import org.bukkit.Bukkit;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Method;

@NullMarked
public final class Environment {
    private static final boolean paper;
    private static final MinecraftVersion version;

    static {
        paper = classExists("com.destroystokyo.paper.PaperConfig")
                || classExists("io.papermc.paper.configuration.Configuration");
        version = detectVersion();
    }

    public static boolean paper() {
        return Environment.paper;
    }

    public static MinecraftVersion currentMinecraft() {
        return version;
    }

    private static MinecraftVersion detectVersion() {
        Exception modernException;
        try {
            return detectVersionModern();
        } catch (final Exception e) {
            modernException = e;
        }

        Exception legacyException;
        try {
            return detectVersionLegacy();
        } catch (final Exception e) {
            legacyException = e;
        }

        final RuntimeException e = new RuntimeException("Failed to detect Minecraft version");
        e.addSuppressed(modernException);
        e.addSuppressed(legacyException);
        throw e;
    }

    private static MinecraftVersion detectVersionLegacy() {
        Exception bukkitVersionException = null;
        try {
            // e.g. "1.8.8-R0.1-SNAPSHOT" — includes patch, unlike the NMS package name
            return MinecraftRelease.parse(Bukkit.getBukkitVersion());
        } catch (final Exception e) {
            bukkitVersionException = e;
        }

        try {
            final String serverPackageName = Bukkit.getServer().getClass().getPackage().getName();
            final String serverApiVersion = serverPackageName.substring(
                    serverPackageName.lastIndexOf('.') + 1);
            return MinecraftRelease.parseNmsVersion(serverApiVersion);
        } catch (final Exception e) {
            if (bukkitVersionException != null) {
                e.addSuppressed(bukkitVersionException);
            }
            throw e;
        }
    }

    private static MinecraftVersion detectVersionModern() throws ReflectiveOperationException {
        // No relocation (Paper 1.20.5+):
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
        if (MinecraftSnapshot.isSnapshot(versionName)) {
            return new MinecraftSnapshot(versionName);
        }

        // We will treat pre-releases and rcs as releases for simplicity
        return MinecraftRelease.parse(versionName);
    }

    private static boolean classExists(final String fullyQualifiedName) {
        try {
            Class.forName(fullyQualifiedName);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
