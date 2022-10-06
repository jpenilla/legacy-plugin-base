/**
 * MIT License
 * <p>
 * Copyright (c) 2017 Dean Bassett
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package xyz.jpenilla.pluginbase.legacy;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A library for the Bukkit API to create player skulls
 * from names, base64 strings, and texture URLs.
 * <p>
 * Does not use any NMS code, and should work across all versions.
 *
 * @author Dean B on 12/28/2016.
 */
public class SkullCreator {
    /**
     * Creates a player skull with a UUID
     *
     * @param id The Player's UUID
     * @return The head of the Player
     */
    public static ItemStack itemFromUuid(UUID id) {
        ItemStack item = getPlayerSkullItem();
        return itemWithUuid(item, id);
    }

    public static ItemStack itemFromOfflinePlayer(final @NonNull OfflinePlayer player) {
        final ItemStack item = getPlayerSkullItem();
        return ItemBuilder.<SkullMeta>editMeta(item, meta -> meta.setOwningPlayer(player));
    }

    /**
     * Creates a player skull based on a UUID
     *
     * @param item The item to apply the name to
     * @param id   The Player's UUID
     * @return The head of the Player
     */
    public static ItemStack itemWithUuid(@NonNull ItemStack item, @NonNull UUID id) {
        item.setItemMeta(metaWithUuid(item.getItemMeta(), id));
        return item;
    }

    /**
     * Modifies a ItemMeta to SkullMeta with owner from a UUID
     *
     * @param meta ItemMeta to edit
     * @param id   Player UUID
     * @return SkullMeta
     */
    public static SkullMeta metaWithUuid(@NonNull ItemMeta meta, @NonNull UUID id) {
        SkullMeta skullMeta = (SkullMeta) meta;
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(id));
        return skullMeta;
    }

    /**
     * Creates a player skull based on a base64 string containing the link to the skin.
     *
     * @param base64 The base64 string containing the texture
     * @return The head with a custom texture
     */
    public static ItemStack itemFromBase64(String base64) {
        ItemStack item = getPlayerSkullItem();
        return itemWithBase64(item, base64);
    }

    /**
     * Applies the base64 string to the ItemStack.
     *
     * @param item   The ItemStack to put the base64 onto
     * @param base64 The base64 string containing the texture
     * @return The head with a custom texture
     */
    public static ItemStack itemWithBase64(@NonNull ItemStack item, @NonNull String base64) {
        UUID hashAsId = new UUID(base64.hashCode(), base64.hashCode());
        if (Environment.majorMinecraftVersion() < 16) {
            return Bukkit.getUnsafe().modifyItemStack(item,
                    "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + base64 + "\"}]}}}"
            );
        } else {
            long m = hashAsId.getMostSignificantBits();
            long l = hashAsId.getLeastSignificantBits();
            int[] id = new int[]{(int) l, (int) (l >> 32), (int) m, (int) (m >> 32)};
            return Bukkit.getUnsafe().modifyItemStack(item,
                    "{SkullOwner:{Id:[I;" + id[0] + "," + id[1] + "," + id[2] + "," + id[3] + "],Properties:{textures:[{Value:\"" + base64 + "\"}]}}}"
            );
        }
    }

    private static final boolean newerApi;

    static {
        newerApi = newerApi();
    }

    private static boolean newerApi() {
        try {
            Material.valueOf("PLAYER_HEAD");
            return true;
        } catch (IllegalArgumentException e) { // If PLAYER_HEAD doesn't exist
            return false;
        }
    }

    private static ItemStack getPlayerSkullItem() {
        if (newerApi) {
            return new ItemStack(Material.valueOf("PLAYER_HEAD"));
        } else {
            return new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (byte) 3);
        }
    }
}
