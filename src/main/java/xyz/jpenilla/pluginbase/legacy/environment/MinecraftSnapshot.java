package xyz.jpenilla.pluginbase.legacy.environment;

import java.util.regex.Pattern;

public final class MinecraftSnapshot implements MinecraftVersion {
    private static final Pattern WEEKLY_SNAPSHOT = Pattern.compile("\\d{2}w\\d{2}[a-z]");
    private static final Pattern TARGET_RELEASE_SNAPSHOT = Pattern.compile("\\d+(?:\\.\\d+){1,2}-(?:snapshot|rc|pre)-\\d+");
    private final String name;

    public MinecraftSnapshot(final String name) {
        this.name = name;
    }

    public static boolean isSnapshot(final String versionName) {
        return WEEKLY_SNAPSHOT.matcher(versionName).matches()
                || TARGET_RELEASE_SNAPSHOT.matcher(versionName).matches();
    }

    @Override
    public boolean isRelease() {
        return false;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
