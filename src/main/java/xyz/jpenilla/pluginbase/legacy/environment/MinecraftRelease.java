package xyz.jpenilla.pluginbase.legacy.environment;

public final class MinecraftRelease implements MinecraftVersion, Comparable<MinecraftRelease> {
    private final int major;
    private final int minor;
    private final int patch;

    public MinecraftRelease(final int major, final int minor, final int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    @Override
    public int compareTo(final MinecraftRelease o) {
        if (this.major != o.major) {
            return Integer.compare(this.major, o.major);
        }
        if (this.minor != o.minor) {
            return Integer.compare(this.minor, o.minor);
        }
        return Integer.compare(this.patch, o.patch);
    }

    @Override
    public boolean isRelease() {
        return true;
    }

    @Override
    public String toString() {
        return this.major + "." + this.minor + "." + this.patch;
    }

    public static MinecraftRelease oldSchemaRelease(final int minor, final int patch) {
        return new MinecraftRelease(1, minor, patch);
    }

    public static MinecraftRelease minecraftRelease(final int major, final int minor, final int patch) {
        return new MinecraftRelease(major, minor, patch);
    }

    public static MinecraftRelease parse(final String versionName) {
        final String baseVersion = versionName.split("-", 2)[0];
        final String[] split = baseVersion.split("\\.");
        if (split.length < 2 || split.length > 3) {
            throw new IllegalArgumentException("Invalid release version: " + versionName);
        }
        final int major;
        final int minor;
        try {
            major = Integer.parseInt(split[0]);
            minor = Integer.parseInt(split[1]);
        } catch (final NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid release version: " + versionName, ex);
        }
        int patch = 0;
        if (split.length > 2) {
            try {
                patch = Integer.parseInt(split[2]);
            } catch (final NumberFormatException ex) {
                throw new IllegalArgumentException("Invalid release version: " + versionName, ex);
            }
        }
        return new MinecraftRelease(major, minor, patch);
    }
}
