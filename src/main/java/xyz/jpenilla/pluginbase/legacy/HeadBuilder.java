package xyz.jpenilla.pluginbase.legacy;

import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

@Deprecated
public class HeadBuilder extends ItemBuilder {
    /**
     * HeadBuilder constructor from base64 Player Head URL
     *
     * @param base64 the {@link String} containing the base64 URL
     */
    public HeadBuilder(@NonNull String base64) {
        super(SkullCreator.itemFromBase64(base64));
    }

    /**
     * HeadBuilder constructor from Player {@link UUID} to get their head
     *
     * @param uuid the {@link UUID} of the player whose head we want
     * @deprecated does a Bukkit.getOfflinePlayer lookup, use the OfflinePlayer variant instead to be more explicit about this behavior
     */
    @Deprecated
    public HeadBuilder(@NonNull UUID uuid) {
        super(SkullCreator.itemFromUuid(uuid));
    }

    public HeadBuilder(@NonNull OfflinePlayer offlinePlayer) {
        super(SkullCreator.itemFromOfflinePlayer(offlinePlayer));
    }

    /**
     * Set the owner of the Skull with base64
     *
     * @param base64 The base64 skin url
     * @return HeadBuilder
     */
    public HeadBuilder setSkin(@NonNull String base64) {
        setMeta(SkullCreator.itemWithBase64(build(), base64).getItemMeta());
        return this;
    }

    /**
     * Set the owner of the Skull with a UUID
     *
     * @param uuid The UUID
     * @return HeadBuilder
     */
    public HeadBuilder setSkin(@NonNull UUID uuid) {
        setMeta(SkullCreator.metaWithUuid(getMeta(), uuid));
        return this;
    }
}
