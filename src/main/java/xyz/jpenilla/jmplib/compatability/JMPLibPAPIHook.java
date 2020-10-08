package xyz.jpenilla.jmplib.compatability;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public class JMPLibPAPIHook {
    public String translate(@NonNull Player player, @NonNull String msg) {
        return PlaceholderAPI.setPlaceholders(player, msg);
    }
}
