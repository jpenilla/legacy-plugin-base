package fun.ccmc.jmplib;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Various ItemStack builders for GUIs
 *
 * @author jmp
 *
 * @deprecated use {@link ItemBuilder} instead
 */
@Deprecated
public class Gui {

    private static ItemStack build(ItemStack is, String name, ArrayList<String> lore) {
        return new ItemBuilder(is).setName(name).setLore(lore).build();
    }

    /**
     * Build an ItemStack from material, name, and ArrayList of strings lore
     *
     * @param m    The Material
     * @param name The Custom Display Name
     * @param lore The Lore
     * @return The ItemStack
     *
     * @deprecated
     */
    @Deprecated
    public static ItemStack build(Material m, String name, ArrayList<String> lore) {
        return new ItemBuilder(m).setName(name).setLore(lore).build();
    }

    /**
     * Build an ItemStack from material, name, and a single string lore
     *
     * @param m    The Material
     * @param name The Custom Display Name
     * @param lore The Lore
     * @return The ItemStack
     *
     * @deprecated
     */
    @Deprecated
    public static ItemStack buildLore(Material m, String name, String lore) {
        return new ItemBuilder(m).setName(name).setLore(lore).build();
    }

    /**
     * Build an ItemStack from material and custom name
     *
     * @param m    The Material
     * @param name The Custom Display Name
     * @return The ItemStack
     *
     * @deprecated
     */
    @Deprecated
    public static ItemStack build(Material m, String name) {
        return new ItemBuilder(m).setName(name).build();
    }

    /**
     * Build an ItemStack from a material with no name or lore
     *
     * @param m The Material
     * @return The ItemStack
     *
     * @deprecated
     */
    @Deprecated
    public static ItemStack build(Material m) {
        return new ItemBuilder(m).setName(" ").build();
    }

    /**
     * Build a Player Head ItemStack from a custom name,
     *  lore arraylist, and a base64 head skin url
     *
     * @param name   The custom name
     * @param lore   the lore arraylist
     * @param base64 the base64 head skin
     * @return The Player Head ItemStack
     *
     * @deprecated
     */
    @Deprecated
    public static ItemStack buildHead(String name, ArrayList<String> lore, String base64) {
        return new ItemBuilder(base64).setName(name).setLore(lore).build();
    }

    /**
     * Build a Player Head ItemStack from a custom name,
     *  lore string, and a base64 head skin url
     *
     * @param name   the custom name
     * @param lore   the lore string
     * @param base64 the base64 head skin
     * @return The Player Head ItemStack
     *
     * @deprecated
     */
    @Deprecated
    public static ItemStack buildHeadLore(String name, String lore, String base64) {
        return new ItemBuilder(base64).setName(name).setLore(lore).build();
    }

    /**
     * Build a Player Head ItemStack from a custom name
     *  and a base64 head skin url
     *
     * @param name   the custom name
     * @param base64 the base64 head skin
     * @return The Player Head ItemStack
     *
     * @deprecated
     */
    @Deprecated
    public static ItemStack buildHead(String name, String base64) {
        return new ItemBuilder(base64).setName(name).build();
    }

}
