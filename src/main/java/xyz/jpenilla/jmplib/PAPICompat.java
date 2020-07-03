package xyz.jpenilla.jmplib;

import lombok.NonNull;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class PAPICompat {
    public String translate(@NonNull Player player, @NonNull String msg) {
        return PlaceholderAPI.setPlaceholders(player, msg);
    }
}
