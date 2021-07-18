package xyz.jpenilla.jmplib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * ItemStack builder
 */
public class ItemBuilder {
    private static final BungeeComponentSerializer serializer = BungeeComponentSerializer.get();
    private static final MiniMessage miniMessage = MiniMessage.get();
    private final boolean hasComponentApi = Environment.paper() && Environment.majorMinecraftVersion() >= 16;

    private final ItemStack itemStack;

    private ItemMeta meta;

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
    public @NonNull ItemBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    /**
     * Set the Display Name of the ItemStack using a MiniMessage string
     *
     * @param displayName The display name to use.
     * @return The ItemBuilder instance
     */
    public @NonNull ItemBuilder setName(String displayName) {
        if (hasComponentApi) {
            meta.setDisplayNameComponent(serializer.serialize(miniMessage.parse(displayName).decoration(TextDecoration.ITALIC, false)));
        } else {
            meta.setDisplayName(MiniMessageUtil.miniMessageToLegacy(displayName));
        }
        return this;
    }

    /**
     * Set the lore of the ItemStack from one or multiple MiniMessage strings
     *
     * @param lore The String(s) to use for lore.
     * @return The ItemBuilder instance
     */
    public @NonNull ItemBuilder setLore(String... lore) {
        if (hasComponentApi) {
            final List<BaseComponent[]> newLore = new ArrayList<>();
            for (String line : lore) {
                newLore.add(serializer.serialize(miniMessage.parse(line).decoration(TextDecoration.ITALIC, false)));
            }
            meta.setLoreComponents(newLore);
        } else {
            meta.setLore(MiniMessageUtil.miniMessageToLegacy(Arrays.asList(lore)));
        }
        return this;
    }

    /**
     * Add one or more MiniMessage Strings to the lore of the ItemStack
     *
     * @param lore The String(s) to add to the lore.
     * @return The ItemBuilder instance
     */
    public @NonNull ItemBuilder addLore(String... lore) {
        if (hasComponentApi) {
            final List<BaseComponent[]> newLore = meta.getLoreComponents();
            final List<BaseComponent[]> newLines = new ArrayList<>();
            for (String line : lore) {
                newLines.add(serializer.serialize(miniMessage.parse(line).decoration(TextDecoration.ITALIC, false)));
            }
            if (newLore != null) {
                newLore.addAll(newLines);
                meta.setLoreComponents(newLore);
            } else {
                meta.setLoreComponents(newLines);
            }
            return this;
        } else {
            final List<String> newLore = meta.getLore();
            final List<String> newLines = MiniMessageUtil.miniMessageToLegacy(Arrays.asList(lore));
            if (newLore != null) {
                newLore.addAll(newLines);
                return setLore(newLore);
            } else {
                return setLore(newLines);
            }
        }
    }

    /**
     * Add a List of MiniMessage Strings to the lore of the ItemStack
     *
     * @param lore The List of Strings to add to the lore.
     * @return The ItemBuilder instance
     */
    public @NonNull ItemBuilder addLore(@NonNull List<String> lore) {
        return addLore(lore.toArray(new String[0]));
    }

    /**
     * Set the lore of the ItemStack from a List of MiniMessage Strings
     *
     * @param lore The {@link List} of {@link String} to use as lore.
     * @return The ItemBuilder instance
     */
    public @NonNull ItemBuilder setLore(@NonNull List<String> lore) {
        return setLore(lore.toArray(new String[0]));
    }

    /**
     * Clear the lore of the ItemStack by setting it to an empty {@link ArrayList}
     *
     * @return The ItemBuilder instance
     */
    public @NonNull ItemBuilder clearLore() {
        meta.setLore(new ArrayList<>());
        return this;
    }

    /**
     * Get the Enchantments and levels of those Enchantments for the ItemBuilder instance
     *
     * @return {@link Map} of {@link Enchantment},{@link Integer}
     */
    public @NonNull Map<Enchantment, Integer> getEnchants() {
        return meta.getEnchants();
    }

    /**
     * Clears the Enchantments from the ItemBuilder and replaces them with the provided ones
     *
     * @param enchants {@link Map} of {@link Enchantment},{@link Integer} containing the Enchants and their levels
     * @return The ItemBuilder instance
     */
    public @NonNull ItemBuilder setEnchants(@NonNull Map<Enchantment, Integer> enchants) {
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
    public @NonNull ItemBuilder addEnchant(@NonNull Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    /**
     * Adds multiple Enchantments to the ItemBuilder with levels
     *
     * @param enchants {@link Map} of {@link Enchantment},{@link Integer} containing the Enchants and their levels
     * @return The ItemBuilder instance
     */
    public @NonNull ItemBuilder addEnchants(@NonNull Map<Enchantment, Integer> enchants) {
        enchants.keySet().forEach(e -> addEnchant(e, enchants.get(e)));
        return this;
    }

    /**
     * Clears all {@link Enchantment}s from the ItemBuilder
     *
     * @return The ItemBuilder instance
     */
    public @NonNull ItemBuilder clearEnchants() {
        meta.getEnchants().keySet().forEach(meta::removeEnchant);
        return this;
    }

    /**
     * Build the ItemStack created by this ItemBuilder instance
     *
     * @return The built {@link ItemStack}
     */
    public @NonNull ItemStack build() {
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public @NonNull ItemMeta getMeta() {
        return this.meta;
    }

    public void setMeta(final @NonNull ItemMeta meta) {
        this.meta = meta;
    }

    public @NonNull ItemMeta meta() {
        return this.meta;
    }

    public void meta(final @NonNull ItemMeta meta) {
        this.meta = meta;
    }

    @SuppressWarnings("unchecked")
    public <I extends ItemMeta> @NonNull ItemBuilder editMeta(final @NonNull Consumer<I> consumer) {
        final I meta = (I) this.meta();
        consumer.accept(meta);
        this.meta(meta);
        return this;
    }

    @SuppressWarnings("unchecked")
    public static <I extends ItemMeta> @NonNull ItemStack editMeta(final @NonNull ItemStack stack, final @NonNull Consumer<I> consumer) {
        if (stack.hasItemMeta()) {
            final I meta = (I) stack.getItemMeta();
            consumer.accept(meta);
            stack.setItemMeta(meta);
        }
        return stack;
    }
}
