package xyz.jpenilla.pluginbase.legacy.environment;

public interface MinecraftVersion {
    boolean isRelease();

    default boolean isAtLeast(final MinecraftVersion other) {
        if (this instanceof MinecraftRelease && other instanceof MinecraftRelease) {
            return ((MinecraftRelease) this).compareTo((MinecraftRelease) other) >= 0;
        }
        return this instanceof MinecraftSnapshot; // Always count snapshots as latest
    }

    default boolean isOlderThan(final MinecraftVersion other) {
        if (this instanceof MinecraftRelease && other instanceof MinecraftRelease) {
            return ((MinecraftRelease) this).compareTo((MinecraftRelease) other) < 0;
        }
        return other instanceof MinecraftSnapshot && this instanceof MinecraftRelease; // Releases are older than snapshots
    }

    default boolean isOrOlder(final MinecraftVersion other) {
        if (this instanceof MinecraftRelease && other instanceof MinecraftRelease) {
            return ((MinecraftRelease) this).compareTo((MinecraftRelease) other) <= 0;
        }
        return this instanceof MinecraftRelease; // Releases are always older than or equal to snapshots
    }
}
