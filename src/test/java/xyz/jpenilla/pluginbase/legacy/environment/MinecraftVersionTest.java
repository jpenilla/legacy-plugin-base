package xyz.jpenilla.pluginbase.legacy.environment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static xyz.jpenilla.pluginbase.legacy.environment.MinecraftReleases.*;

class MinecraftVersionTest {
    @Test
    public void testIsAtLeast() {
        // Same version
        assertTrue(v1_20_4.isAtLeast(v1_20_4));
        assertTrue(v1_21.isAtLeast(v1_21));

        // Older version should return false
        assertFalse(v1_20_4.isAtLeast(v1_21));
        assertFalse(v1_20_4.isAtLeast(v1_20_5));
        assertFalse(v1_8.isAtLeast(v1_21_10));

        // Newer version should return true
        assertTrue(v1_21.isAtLeast(v1_20_4));
        assertTrue(v1_20_5.isAtLeast(v1_20_4));
        assertTrue(v1_21_10.isAtLeast(v1_21));
        assertTrue(v1_21_10.isAtLeast(v1_21_9));

        // Minor version differences
        assertTrue(v1_16_5.isAtLeast(v1_16));
        assertTrue(v1_16_5.isAtLeast(v1_16_1));
        assertFalse(v1_16.isAtLeast(v1_16_5));

        // Snapshots are always at least as new as releases
        final MinecraftVersion v25w41a = new MinecraftSnapshot("25w41a");
        final MinecraftVersion v26w01a = new MinecraftSnapshot("26w01a");
        assertTrue(v25w41a.isAtLeast(v1_20_4));
        assertTrue(v26w01a.isAtLeast(v1_21));
        assertTrue(v26w01a.isAtLeast(v25w41a));
    }

    @Test
    public void testIsOlderThan() {
        // Same version should return false
        assertFalse(v1_20_4.isOlderThan(v1_20_4));
        assertFalse(v1_21.isOlderThan(v1_21));

        // Older version should return true
        assertTrue(v1_20_4.isOlderThan(v1_21));
        assertTrue(v1_20_4.isOlderThan(v1_20_5));
        assertTrue(v1_8.isOlderThan(v1_21_10));
        assertTrue(v1_16.isOlderThan(v1_16_5));

        // Newer version should return false
        assertFalse(v1_21.isOlderThan(v1_20_4));
        assertFalse(v1_20_5.isOlderThan(v1_20_4));
        assertFalse(v1_21_10.isOlderThan(v1_21));

        // Releases are older than snapshots
        final MinecraftVersion v25w41a = new MinecraftSnapshot("25w41a");
        assertTrue(v1_20_4.isOlderThan(v25w41a));
        assertTrue(v1_21.isOlderThan(v25w41a));

        // Snapshots are not older than releases
        assertFalse(v25w41a.isOlderThan(v1_20_4));
        assertFalse(v25w41a.isOlderThan(v1_21));
    }

    @Test
    public void testIsOrOlder() {
        // Same version should return true
        assertTrue(v1_20_4.isOrOlder(v1_20_4));
        assertTrue(v1_21.isOrOlder(v1_21));

        // Older version should return true
        assertTrue(v1_20_4.isOrOlder(v1_21));
        assertTrue(v1_20_4.isOrOlder(v1_20_5));
        assertTrue(v1_8.isOrOlder(v1_21_10));
        assertTrue(v1_16.isOrOlder(v1_16_5));

        // Newer version should return false
        assertFalse(v1_21.isOrOlder(v1_20_4));
        assertFalse(v1_20_5.isOrOlder(v1_20_4));
        assertFalse(v1_21_10.isOrOlder(v1_21));

        // Releases are always older or equal to snapshots
        final MinecraftVersion v25w41a = new MinecraftSnapshot("25w41a");
        assertTrue(v1_20_4.isOrOlder(v25w41a));
        assertTrue(v1_21.isOrOlder(v25w41a));

        // Snapshots are not older or equal to releases
        assertFalse(v25w41a.isOrOlder(v1_20_4));
        assertFalse(v25w41a.isOrOlder(v1_21));
    }

    @Test
    public void testIsRelease() {
        assertTrue(v1_20_4.isRelease());
        assertTrue(v1_21.isRelease());
        assertTrue(v1_8.isRelease());
        assertTrue(v1_21_10.isRelease());

        final MinecraftVersion snapshot = new MinecraftSnapshot("25w41a");
        assertFalse(snapshot.isRelease());
    }
}
