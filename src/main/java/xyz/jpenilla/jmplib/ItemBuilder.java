package xyz.jpenilla.jmplib;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
    @Getter @Setter private ItemMeta meta;

    /**
     * ItemBuilder constructor from {@link Material}
     *
     * @param material the {@link Material} to use
     */
    public ItemBuilder(@NonNull Material material) {
        this(new ItemStack(material));
    }

    /**
     * ItemBuilder constructor from {@link ItemStack}
     *
     * @param itemStack the {@link ItemStack} to use
     */
    public ItemBuilder(@NonNull ItemStack itemStack) {
        this.itemStack = itemStack;
        this.meta = itemStack.getItemMeta();
    }

    /**
     * Set the amount of Items in the ItemStack for this ItemBuilder instance
     *
     * @param amount The amount of items for the ItemStack
     * @return The ItemBuilder instance
     */
    @NonNull
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
    @NonNull
    public ItemBuilder setName(String displayName) {
        meta.setDisplayName(MiniMessageUtil.miniMessageToLegacy(displayName));
        return this;
    }

    /**
     * Set the lore of the ItemStack from one or multiple strings
     *
     * @param lore The String(s) to use for lore. Will be colorized using the {@literal &} color code
     * @return The ItemBuilder instance
     */
    @NonNull
    public ItemBuilder setLore(String... lore) {
        meta.setLore(MiniMessageUtil.miniMessageToLegacy(Arrays.stream(lore).collect(Collectors.toList())));
        return this;
    }

    /**
     * Add one or more Strings to the lore of the ItemStack
     *
     * @param lore The String(s) to add to the lore. Will be colorized using the {@literal &} color code
     * @return The ItemBuilder instance
     */
    @NonNull
    public ItemBuilder addLore(String... lore) {
        final List<String> newLore = meta.getLore();
        final List<String> newLines = MiniMessageUtil.miniMessageToLegacy(Arrays.stream(lore).collect(Collectors.toList()));
        if (newLore != null) {
            newLore.addAll(newLines);
            return setLore(newLore);
        } else {
            return setLore(newLines);
        }
    }

    /**
     * Add a List of Strings to the lore of the ItemStack
     *
     * @param lore The List of Strings to add to the lore. Will be colorized using the {@literal &} color code
     * @return The ItemBuilder instance
     */
    @NonNull
    public ItemBuilder addLore(List<String> lore) {
        List<String> temp = meta.getLore();
        if (temp == null) {
            temp = MiniMessageUtil.miniMessageToLegacy(lore);
        } else {
            temp.addAll(MiniMessageUtil.miniMessageToLegacy(lore));
        }
        return setLore(temp);
    }

    /**
     * Set the lore of the ItemStack from a List of Strings
     *
     * @param lore The {@link List} of {@link String} to use as lore. Will be colorized using the {@literal &} color code
     * @return The ItemBuilder instance
     */
    @NonNull
    public ItemBuilder setLore(List<String> lore) {
        meta.setLore(MiniMessageUtil.miniMessageToLegacy(lore));
        return this;
    }

    /**
     * Clear the lore of the ItemStack by setting it to an empty {@link ArrayList}
     *
     * @return The ItemBuilder instance
     */
    @NonNull
    public ItemBuilder clearLore() {
        meta.setLore(new ArrayList<>());
        return this;
    }

    /**
     * Get the Enchantments and levels of those Enchantments for the ItemBuilder instance
     *
     * @return {@link Map} of {@link Enchantment},{@link Integer}
     */
    @NonNull
    public Map<Enchantment, Integer> getEnchants() {
        return meta.getEnchants();
    }

    /**
     * Clears the Enchantments from the ItemBuilder and replaces them with the provided ones
     *
     * @param enchants {@link Map} of {@link Enchantment},{@link Integer} containing the Enchants and their levels
     * @return The ItemBuilder instance
     */
    @NonNull
    public ItemBuilder setEnchants(@NonNull Map<Enchantment, Integer> enchants) {
        clearEnchants();
        return addEnchants(enchants);
    }

    /**
     * Add an Enchantment to the ItemBuilder with a level
     *
     * @param enchantment The {@link Enchantment} to add
     * @param level       The level to use for the Enchantment
     * @return The ItemBuilder instance
     */
    @NonNull
    public ItemBuilder addEnchant(@NonNull Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    /**
     * Adds multiple Enchantments to the ItemBuilder with levels
     *
     * @param enchants {@link Map} of {@link Enchantment},{@link Integer} containing the Enchants and their levels
     * @return The ItemBuilder instance
     */
    @NonNull
    public ItemBuilder addEnchants(@NonNull Map<Enchantment, Integer> enchants) {
        enchants.keySet().forEach(e -> addEnchant(e, enchants.get(e)));
        return this;
    }

    /**
     * Clears all {@link Enchantment}s from the ItemBuilder
     *
     * @return The ItemBuilder instance
     */
    @NonNull
    public ItemBuilder clearEnchants() {
        meta.getEnchants().keySet().forEach(meta::removeEnchant);
        return this;
    }

    /**
     * Build the ItemStack created by this ItemBuilder instance
     *
     * @return The built {@link ItemStack}
     */
    @NonNull
    public ItemStack build() {
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
