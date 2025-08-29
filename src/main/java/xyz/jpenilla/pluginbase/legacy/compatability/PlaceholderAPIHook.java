package xyz.jpenilla.pluginbase.legacy.compatability;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PlaceholderAPIHook {
    public String replacePlaceholders(final Player player, final String msg) {
        return PlaceholderAPI.setPlaceholders(player, msg);
    }
}
