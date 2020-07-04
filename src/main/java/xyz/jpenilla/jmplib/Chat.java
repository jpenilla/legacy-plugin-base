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
import java.util.HashMap;
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
    private final HashMap<String, String> centeredTempReplacements = new HashMap<>();
    private JMPLibPAPIHook papi = null;
    private JMPLibPrismaHook prisma = null;

    public Chat(JavaPlugin plugin) {
        centeredTempReplacements.put("<bold>", "§l");
        centeredTempReplacements.put("</bold>", "§r");
        instance = plugin;
        audience = BukkitAudiences.create(plugin);
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            papi = new JMPLibPAPIHook();
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Prisma")) {
            prisma = new JMPLibPrismaHook();
        }
        miniMessage = MiniMessage.get();
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

    /**
     * Get the amount of spaces needed to center the message
     *
     * @param message MiniMessage formatted message
     * @return String of spaces
     */
    public String getCenteredSpacePrefix(String message) {
        String specialMessage = TextUtil.replacePlaceholders(message, centeredTempReplacements, false);
        return LegacyChat.getCenteredSpacePrefix(miniMessage.stripTokens(specialMessage));
    }

    /**
     * Get a MiniMessage string prefixed with spaces to center it
     *
     * @param message MiniMessage formatted message
     * @return Centered MiniMessage
     */
    public String getCenteredMessage(String message) {
        return getCenteredSpacePrefix(message) + message;
    }

    /**
     * Parse the given string with PAPI, Prisma, and Placeholders
     *
     * @param player       The player
     * @param message      The message
     * @param placeholders Placeholders
     * @return Parsed Message
     */
    public String replacePlaceholders(@Nullable Player player, @NonNull String message, @Nullable Map<String, String> placeholders) {
        String finalMessage = TextUtil.replacePlaceholders(message, placeholders);
        if (prisma != null) {
            finalMessage = prisma.translate(finalMessage);
        }
        return papiParse(player, finalMessage);
    }

    /**
     * Parse the given strings with PAPI, Prisma, and Placeholders
     *
     * @param player       The player
     * @param messages     The message
     * @param placeholders Placeholders
     * @return Parsed messages
     */
    public List<String> replacePlaceholders(@Nullable Player player, @NonNull List<String> messages, @Nullable Map<String, String> placeholders) {
        ArrayList<String> l = new ArrayList<>();
        for (String m : messages) {
            l.add(replacePlaceholders(player, m, placeholders));
        }
        return l;
    }
}
