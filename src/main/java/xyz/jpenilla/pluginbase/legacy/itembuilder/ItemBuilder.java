package xyz.jpenilla.pluginbase.legacy.itembuilder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import xyz.jpenilla.pluginbase.legacy.ComponentUtil;
import xyz.jpenilla.pluginbase.legacy.Environment;
import xyz.jpenilla.pluginbase.legacy.PaperComponentUtil;
import xyz.jpenilla.pluginbase.legacy.PluginBase;

/**
 * Immutable builder for {@link ItemStack}.
 *
 * @param <B> self type
 * @param <I> meta type
 */
@DefaultQualifier(NonNull.class)
@SuppressWarnings("unused")
public class ItemBuilder<B extends ItemBuilder<B, I>, I extends ItemMeta> {
    @SuppressWarnings("deprecation")
    private static final MetaFacet<String> LEGACY = new MetaFacet<>(
            ComponentUtil.legacySerializer()::serialize,
            ItemMeta::setDisplayName,
            ItemMeta::getLore,
            ItemMeta::setLore
    );
    private static final @Nullable MetaFacet<Object> PAPER_ADVENTURE = createPaperFacet();
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static final MetaFacet<Object> FACET = PAPER_ADVENTURE != null
            ? PAPER_ADVENTURE
            : (MetaFacet<Object>) (MetaFacet) LEGACY;

    private final ItemStack stack;
    private final I meta;

    protected ItemBuilder(final ItemStack stack, final I meta) {
        this.stack = stack;
        this.meta = meta;
    }

    protected ItemBuilder(final Material material) {
        this(new ItemStack(material));
    }

    @SuppressWarnings("unchecked")
    protected ItemBuilder(final ItemStack stack) {
        this.stack = stack.clone();
        this.meta = (I) stack.getItemMeta();
    }

    public final MiniMessageContext miniMessageContext() {
        return new MiniMessageContext();
    }

    @SuppressWarnings("unchecked")
    protected final B edit(final BiConsumer<ItemStack, I> consumer) {
        final ItemStack stack = this.stack.clone();
        final I meta = (I) this.meta.clone();
        consumer.accept(stack, meta);
        return this.create(stack, meta);
    }

    @SuppressWarnings("unchecked")
    protected B create(final ItemStack stack, final I meta) {
        return (B) new ItemBuilder<B, I>(stack, meta);
    }

    public final B stackSize(final int amount) {
        return this.edit((stack, meta) -> stack.setAmount(amount));
    }

    public final B customName(final Component displayName) {
        return this.editMeta(meta -> FACET.setCustomName.accept(
                meta,
                FACET.serialize.apply(ComponentUtil.disableItalics(displayName))
        ));
    }

    public final B addLore(final List<Component> lore) {
        return this.editMeta(meta -> {
            final List<Object> existing = new ArrayList<>(FACET.getLore.apply(meta));
            existing.addAll(
                    lore.stream()
                            .map(ComponentUtil::disableItalics)
                            .map(FACET.serialize)
                            .collect(Collectors.toList())
            );
            FACET.setLore.accept(meta, existing);
        });
    }

    public final B addLore(final Component... lore) {
        return this.addLore(Arrays.asList(lore));
    }

    public final B lore(final List<Component> lore) {
        return this.editMeta(meta -> {
            final List<Object> mapped = lore.stream()
                    .map(ComponentUtil::disableItalics)
                    .map(FACET.serialize)
                    .collect(Collectors.toList());
            FACET.setLore.accept(meta, mapped);
        });
    }

    public final B lore(final Component... lore) {
        return this.lore(Arrays.asList(lore));
    }

    public final B clearLore() {
        return this.editMeta(meta -> FACET.setLore.accept(meta, new ArrayList<>()));
    }

    public final Map<Enchantment, Integer> enchants() {
        return this.meta.getEnchants();
    }

    public final B enchants(final Map<Enchantment, Integer> enchants) {
        return this.clearEnchants().addEnchants(enchants);
    }

    public final B addEnchant(final Enchantment enchantment, final int level) {
        return this.editMeta(meta -> meta.addEnchant(enchantment, level, true));
    }

    public final B addEnchants(final Map<Enchantment, Integer> enchants) {
        return this.editMeta(meta -> enchants.forEach((enchant, level) -> meta.addEnchant(enchant, level, true)));
    }

    public final B clearEnchants() {
        return this.editMeta(meta -> meta.getEnchants().keySet().forEach(meta::removeEnchant));
    }

    public final ItemStack build() {
        final ItemStack stack = this.stack.clone();
        stack.setItemMeta(this.meta);
        return stack;
    }

    @SuppressWarnings("unchecked")
    public final I meta() {
        return (I) this.meta.clone();
    }

    @SuppressWarnings("unchecked")
    public final B meta(final I meta) {
        return this.create(this.stack, (I) meta.clone());
    }

    @SuppressWarnings("unchecked")
    public final B editMeta(final Consumer<I> consumer) {
        final I meta = (I) this.meta.clone();
        consumer.accept(meta);
        return this.create(
                this.stack,
                meta // A defensive clone would make sense here, but it's likely not needed
        );
    }

    public static <I extends ItemMeta> ItemBuilder<?, I> create(final Material material) {
        return new ItemBuilder<>(material);
    }

    public static <I extends ItemMeta> ItemBuilder<?, I> create(final ItemStack stack) {
        return new ItemBuilder<>(stack);
    }

    private static @Nullable MetaFacet<Object> createPaperFacet() {
        if (!Environment.paper() || Environment.majorMinecraftVersion() < 16) {
            return null;
        }
        final Method nameMethod;
        final Method setLoreMethod;
        final Method getLoreMethod;
        try {
            nameMethod = ItemMeta.class.getMethod("displayName", PaperComponentUtil.nativeAdventureComponentClass());
            setLoreMethod = ItemMeta.class.getMethod("lore", List.class);
            getLoreMethod = ItemMeta.class.getMethod("lore");
        } catch (final ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
        final BiConsumer<ItemMeta, Object> setName = (meta, name) -> {
            try {
                nameMethod.invoke(meta, name);
            } catch (final ReflectiveOperationException ex) {
                throw new RuntimeException(ex);
            }
        };
        final BiConsumer<ItemMeta, List<Object>> setLore = (meta, lore) -> {
            try {
                setLoreMethod.invoke(meta, lore);
            } catch (final ReflectiveOperationException ex) {
                throw new RuntimeException(ex);
            }
        };
        @SuppressWarnings("unchecked") final Function<ItemMeta, List<Object>> getLore = meta -> {
            try {
                return (List<Object>) getLoreMethod.invoke(meta);
            } catch (final ReflectiveOperationException ex) {
                throw new RuntimeException(ex);
            }
        };
        return new MetaFacet<>(PaperComponentUtil::toNative, setName, getLore, setLore);
    }

    private static final class MetaFacet<T> {
        final Function<Component, T> serialize;
        final BiConsumer<ItemMeta, T> setCustomName;
        final Function<ItemMeta, List<T>> getLore;
        final BiConsumer<ItemMeta, List<T>> setLore;

        MetaFacet(
                final Function<Component, T> serialize,
                final BiConsumer<ItemMeta, T> setCustomName,
                final Function<ItemMeta, List<T>> getLore,
                final BiConsumer<ItemMeta, List<T>> setLore
        ) {
            this.serialize = serialize;
            this.setCustomName = setCustomName;
            this.getLore = getLore;
            this.setLore = setLore;
        }
    }

    public final class MiniMessageContext {
        @SuppressWarnings("unchecked")
        public B exit() {
            return (B) ItemBuilder.this;
        }

        public ItemStack exitAndBuild() {
            return this.exit().build();
        }

        public MiniMessageContext customName(final String customName) {
            return ItemBuilder.this.customName(this.deserialize(customName)).miniMessageContext();
        }

        public MiniMessageContext addLore(final List<String> lore) {
            return ItemBuilder.this.addLore(this.deserialize(lore)).miniMessageContext();
        }

        public MiniMessageContext addLore(final String... lore) {
            return this.addLore(Arrays.asList(lore));
        }

        public MiniMessageContext lore(final List<String> lore) {
            return ItemBuilder.this.lore(this.deserialize(lore)).miniMessageContext();
        }

        public MiniMessageContext lore(final String... lore) {
            return this.lore(Arrays.asList(lore));
        }

        private Component deserialize(final String mini) {
            return PluginBase.instance().miniMessage().deserialize(mini);
        }

        private List<Component> deserialize(final List<String> mini) {
            return mini.stream().map(this::deserialize).collect(Collectors.toList());
        }
    }
}
