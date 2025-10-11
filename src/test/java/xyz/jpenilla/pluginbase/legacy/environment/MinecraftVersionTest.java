package xyz.jpenilla.pluginbase.legacy.environment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static xyz.jpenilla.pluginbase.legacy.environment.MinecraftRelease.minecraftRelease;

class MinecraftVersionTest {
    @Test
    public void testComparison() {
        final MinecraftVersion v1_20_4 = minecraftRelease(20, 4);
        final MinecraftVersion v1_21_0 = minecraftRelease(21, 0);
        final MinecraftVersion v1_21_10 = minecraftRelease(21, 10);
        final MinecraftVersion v1_21_11 = minecraftRelease(21, 11);
        final MinecraftVersion v25w41a = new MinecraftSnapshot("25w41a");
        final MinecraftVersion v26w01a = new MinecraftSnapshot("26w01a");

        assertTrue(v1_20_4.isAtLeast(v1_20_4)); // same version
        assertFalse(v1_20_4.isAtLeast(v1_21_0)); // older version
        assertTrue(v1_21_0.isAtLeast(v1_20_4)); // newer version
        assertTrue(v1_21_10.isAtLeast(v1_21_0));
        assertFalse(v1_21_0.isAtLeast(v1_21_10));
        assertTrue(v1_21_11.isAtLeast(v1_21_10));

        assertTrue(v25w41a.isAtLeast(v1_20_4)); // snapshot always newer than release
        assertTrue(v26w01a.isAtLeast(v1_21_0)); // snapshot always newer than release
        assertTrue(v26w01a.isAtLeast(v25w41a)); // any snapshot is newer than any other snapshot
    }
}
