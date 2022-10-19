package xyz.jpenilla.pluginbase.legacy.itembuilder;

import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import xyz.jpenilla.pluginbase.legacy.SkullCreator;

@DefaultQualifier(NonNull.class)
@SuppressWarnings("unused")
public class HeadBuilder extends ItemBuilder<HeadBuilder, SkullMeta> {
    private HeadBuilder(final ItemStack stack, final SkullMeta meta) {
        super(stack, meta);
    }

    public HeadBuilder(final String base64) {
        super(SkullCreator.itemFromBase64(base64));
    }

    public HeadBuilder(final OfflinePlayer offlinePlayer) {
        super(SkullCreator.itemFromOfflinePlayer(offlinePlayer));
    }

    /**
     * HeadBuilder constructor from Player {@link UUID} to get their head
     *
     * @param uuid the {@link UUID} of the player whose head we want
     * @deprecated does a Bukkit.getOfflinePlayer lookup, use the OfflinePlayer variant instead to be more explicit about this behavior
     */
    @Deprecated
    public HeadBuilder(final UUID uuid) {
        super(SkullCreator.itemFromUuid(uuid));
    }

    public HeadBuilder skin(final String base64) {
        return this.meta((SkullMeta) SkullCreator.itemWithBase64(this.build(), base64).getItemMeta());
    }

    public HeadBuilder skin(final UUID uuid) {
        return this.meta(SkullCreator.metaWithUuid(this.meta(), uuid));
    }

    @Override
    protected HeadBuilder create(final ItemStack stack, final SkullMeta meta) {
        return new HeadBuilder(stack, meta);
    }
}
