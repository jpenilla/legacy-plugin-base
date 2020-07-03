package xyz.jpenilla.jmplib;

import lombok.NonNull;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import xyz.jpenilla.jmplib.compatability.JMPLibPAPIHook;
import xyz.jpenilla.jmplib.compatability.JMPLibPrismaHook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Chat message sending utilities
 *
 * @author jmp
 */
public class Chat {
    private final JavaPlugin instance;
    private final BukkitAudiences audience;
    private final MiniMessage miniMessage;
    private JMPLibPAPIHook papi = null;
    private JMPLibPrismaHook prisma = null;

    public Chat(JavaPlugin plugin) {
        instance = plugin;
        audience = BukkitAudiences.create(plugin);
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            papi = new JMPLibPAPIHook();
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Prisma")) {
            prisma = new JMPLibPrismaHook();
        }
        miniMessage = MiniMessage.instance();
    }

    /**
     * Parse a string with PlaceholderAPI returning an unchanged string if Player is null
     *
     * @param player  The player
     * @param message The message to parse
     * @return The parsed message
     */
    public String papiParse(@Nullable Player player, @NonNull String message) {
        if (player != null && papi != null) {
            return papi.translate(player, message);
        } else {
            return message;
        }
    }

    /**
     * Parse a list of string with PlaceholderAPI returning an unchanged list if Player is null
     *
     * @param player   The player
     * @param messages The messages
     * @return The parsed messages
     */
    public List<String> papiParse(@Nullable Player player, @NonNull List<String> messages) {
        if (player != null && papi != null) {
            ArrayList<String> l = new ArrayList<>();
            for (String m : messages) {
                l.add(papiParse(player, m));
            }
            return l;
        } else {
            return messages;
        }
    }

    public void sendPlaceholders(@NonNull CommandSender sender, @NonNull List<String> messages) {
        sendPlaceholders(sender, messages, null);
    }

    public void sendPlaceholders(@NonNull CommandSender sender, @NonNull List<String> messages, @Nullable Map<String, String> placeholders) {
        for (String message : messages) {
            sendPlaceholders(sender, message, placeholders);
        }
    }

    public void send(@NonNull CommandSender sender, @NonNull List<String> messages) {
        for (String message : messages) {
            send(sender, message);
        }
    }

    public void sendPlaceholders(@NonNull CommandSender sender, @NonNull String message, @Nullable Map<String, String> placeholders) {
        String msg;
        if (sender instanceof Player) {
            msg = replacePlaceholders((Player) sender, message, placeholders);
        } else {
            msg = replacePlaceholders(null, message, placeholders);
        }
        send(sender, msg);
    }

    public void sendPlaceholders(@NonNull CommandSender sender, @NonNull String message) {
        sendPlaceholders(sender, message, null);
    }

    public void send(@NonNull CommandSender sender, @NonNull String message) {
        if (sender instanceof Player) {
            audience.player((Player) sender).sendMessage(miniMessage.parse(message));
        } else {
            audience.console().sendMessage(miniMessage.parse(miniMessage.stripTokens(message)));
        }
    }

    private String replacePlaceholders(@Nullable Player player, @NonNull String message, @Nullable Map<String, String> placeholders) {
        String finalMessage = TextUtil.replacePlaceholders(message, placeholders);
        if (prisma != null) {
            finalMessage = prisma.translate(finalMessage);
        }
        return papiParse(player, finalMessage);
    }
}
