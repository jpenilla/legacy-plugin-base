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

    public static MinecraftRelease minecraftRelease(final int minor, final int patch) {
        return new MinecraftRelease(1, minor, patch);
    }
}
