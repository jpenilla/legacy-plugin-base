package fun.ccmc.jmplib;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Various ItemStack builders for GUIs
 */
public class Gui {

    private static ItemStack build(ItemStack is, String name, ArrayList<String> lore) {
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(TextUtil.colorize(name));
        meta.setDisplayName(TextUtil.colorize(name));
        if (lore != null) {
            meta.setLore(TextUtil.colorize(lore));
        }
        is.setItemMeta(meta);
        return is;
    }

    /**
     * Build an ItemStack from material, name, and lore
     * ArrayList of strings lore
     *
     * @param m The Material
     * @param name The Custom Display Name
     * @param lore The Lore
     * @return The ItemStack
     */
    public static ItemStack build(Material m, String name, ArrayList<String> lore) {
        return build(new ItemStack(m), name, lore);
    }

    /**
     * Build an ItemStack from material, name, and lore
     * Single string lore
     *
     * @param m The Material
     * @param name The Custom Display Name
     * @param lore The Lore
     * @return The ItemStack
     */
    public static ItemStack buildLore(Material m, String name, String lore) {
        ArrayList<String> lores = new ArrayList<>(Collections.singletonList(lore));
        return build(m, name, lores);
    }

    /**
     * Build an ItemStack from material and custom name
     *
     * @param m The Material
     * @param name The Custom Display Name
     * @return The ItemStack
     */
    public static ItemStack build(Material m, String name) {
        return build(m, name, null);
    }

    /**
     * Build an ItemStack from a material with no name or lore
     *
     * @param m The Material
     * @return The ItemStack
     */
    public static ItemStack build(Material m) {
        return build(m, " ");
    }

    /**
     * Build a Player Head ItemStack from a custom name,
     * lore arraylist, and a base64 head skin url
     *
     * @param name The custom name
     * @param lore the lore arraylist
     * @param base64 the base64 head skin
     * @return The Player Head ItemStack
     */
    public static ItemStack buildHead(String name, ArrayList<String> lore, String base64) {
        ItemStack is = SkullCreator.itemFromBase64(base64);
        return build(is, name, lore);
    }

    /**
     * Build a Player Head ItemStack from a custom name,
     * lore string, and a base64 head skin url
     *
     * @param name the custom name
     * @param lore the lore string
     * @param base64 the base64 head skin
     * @return The Player Head ItemStack
     */
    public static ItemStack buildHeadLore(String name, String lore, String base64) {
        ArrayList<String> lores = new ArrayList<>(Collections.singletonList(lore));
        return buildHead(name, lores, base64);
    }

    /**
     * Build a Player Head ItemStack from a custom name
     * and a base64 head skin url
     *
     * @param name the custom name
     * @param base64 the base64 head skin
     * @return The Player Head ItemStack
     */
    public static ItemStack buildHead(String name, String base64) {
        return buildHead(name, null, base64);
    }

}
