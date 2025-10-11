package xyz.jpenilla.pluginbase.legacy.environment;

public final class MinecraftSnapshot implements MinecraftVersion {
    private final String name;

    public MinecraftSnapshot(final String name) {
        this.name = name;
    }

    public static boolean isSnapshot(final String versionName) {
        // i.e.
        //   25w41a - split on letters -> ["25", "41", ""]
        //   1.21.8 - split on letters -> ["1.21.8"]
        final String[] snapshotSplit = versionName.split("[a-z]");
        if (snapshotSplit.length == 3) {
            if (!snapshotSplit[2].isEmpty()) {
                return false;
            }
            try {
                Integer.parseInt(snapshotSplit[0]);
                Integer.parseInt(snapshotSplit[1]);
                return true;
            } catch (final NumberFormatException ignored) {
                return false;
            }
        }
        return false;
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
