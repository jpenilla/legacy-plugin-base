package fun.ccmc.jmplib;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * Builder style class for ItemStacks
 * </p>
 *
 * @author jmp
 */
public class ItemBuilder {
    private final ItemStack itemStack;

    /**
     * The temporary {@link ItemMeta} store for the {@link ItemBuilder}
     *
     * @param meta The new {@link ItemMeta} to use for the Builder instance
     * @return The {@link ItemMeta} of the Builder instance
     */
    @Getter
    @Setter
    private ItemMeta meta;

    /**
     * ItemBuilder constructor from {@link Material}
     *
     * @param material the {@link Material} to use
     */
    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.meta = itemStack.getItemMeta();
    }

    /**
     * ItemBuilder constructor from {@link ItemStack}
     *
     * @param itemStack the {@link ItemStack} to use
     */
    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.meta = itemStack.getItemMeta();
    }

    /**
     * ItemBuilder constructor from base64 Player Head URL
     *
     * @param base64 the {@link String} containing the base64 URL
     */
    public ItemBuilder(String base64) {
        this.itemStack = SkullCreator.itemFromBase64(base64);
        this.meta = itemStack.getItemMeta();
    }

    /**
     * ItemBuilder constructor from Player {@link UUID} to get their head
     *
     * @param uuid the {@link UUID} of the player whose head we want
     */
    public ItemBuilder(UUID uuid) {
        this.itemStack = SkullCreator.itemFromUuid(uuid);
        this.meta = itemStack.getItemMeta();
    }

    /**
     * Set the amount of Items in the ItemStack for this ItemBuilder instance
     *
     * @param amount The amount of items for the ItemStack
     * @return The ItemBuilder instance
     */
    public ItemBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    /**
     * Set the Display Name of the ItemStack
     *
     * @param displayName The display name to use. Will be colorized using the {@literal &} color code
     * @return The ItemBuilder instance
     */
    public ItemBuilder setName(String displayName) {
        meta.setDisplayName(TextUtil.colorize(displayName));
        return this;
    }

    /**
     * Set the lore of the ItemStack from one or multiple strings
     *
     * @param lore The String(s) to use for lore. Will be colorized using the {@literal &} color code
     * @return The ItemBuilder instance
     */
    public ItemBuilder setLore(String... lore) {
        meta.setLore(TextUtil.colorize(Arrays.stream(lore).collect(Collectors.toList())));
        return this;
    }

    /**
     * Add one or more Strings to the lore of the ItemStack
     *
     * @param lore The String(s) to add to the lore. Will be colorized using the {@literal &} color code
     * @return The ItemBuilder instance
     */
    public ItemBuilder addLore(String... lore) {
        List<String> temp = meta.getLore();
        if (temp == null) {
            temp = Arrays.stream(lore).collect(Collectors.toList());
        } else {
            temp.addAll(Arrays.stream(lore).collect(Collectors.toList()));
        }
        setLore(temp);
        return this;
    }

    /**
     * Add a List of Strings to the lore of the ItemStack
     *
     * @param lore The List of Strings to add to the lore. Will be colorized using the {@literal &} color code
     * @return The ItemBuilder instance
     */
    public ItemBuilder addLore(List<String> lore) {
        List<String> temp = meta.getLore();
        if (temp == null) {
            temp = lore;
        } else {
            temp.addAll(lore);
        }
        setLore(temp);
        return this;
    }

    /**
     * Set the lore of the ItemStack from a List of Strings
     *
     * @param lore The {@link List} of {@link String} to use as lore. Will be colorized using the {@literal &} color code
     * @return The ItemBuilder instance
     */
    public ItemBuilder setLore(List<String> lore) {
        meta.setLore(TextUtil.colorize(lore));
        return this;
    }

    /**
     * Clear the lore of the ItemStack by setting it to an empty {@link ArrayList}
     *
     * @return The ItemBuilder instance
     */
    public ItemBuilder clearLore() {
        meta.setLore(new ArrayList<>());
        return this;
    }

    /**
     * Get the Enchantments and levels of those Enchantments for the ItemBuilder instance
     *
     * @return {@link Map} of {@link Enchantment},{@link Integer}
     */
    public Map<Enchantment, Integer> getEnchants() {
        return meta.getEnchants();
    }

    /**
     * Clears the Enchantments from the ItemBuilder and replaces them with the provided ones
     *
     * @param enchants {@link Map} of {@link Enchantment},{@link Integer} containing the Enchants and their levels
     * @return The ItemBuilder instance
     */
    public ItemBuilder setEnchants(Map<Enchantment, Integer> enchants) {
        clearEnchants();
        addEnchants(enchants);
        return this;
    }

    /**
     * Add an Enchantment to the ItemBuilder with a level
     *
     * @param enchantment The {@link Enchantment} to add
     * @param level       The level to use for the Enchantment
     * @return The ItemBuilder instance
     */
    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    /**
     * Adds multiple Enchantments to the ItemBuilder with levels
     *
     * @param enchants {@link Map} of {@link Enchantment},{@link Integer} containing the Enchants and their levels
     * @return The ItemBuilder instance
     */
    public ItemBuilder addEnchants(Map<Enchantment, Integer> enchants) {
        enchants.keySet().forEach(e -> addEnchant(e, enchants.get(e)));
        return this;
    }

    /**
     * Clears all {@link Enchantment}s from the ItemBuilder
     *
     * @return The ItemBuilder instance
     */
    public ItemBuilder clearEnchants() {
        meta.getEnchants().keySet().forEach(meta::removeEnchant);
        return this;
    }

    /**
     * Build the ItemStack created by this ItemBuilder instance
     *
     * @return The built {@link ItemStack}
     */
    public ItemStack build() {
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
