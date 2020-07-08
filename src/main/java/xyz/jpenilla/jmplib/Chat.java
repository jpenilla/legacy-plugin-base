package xyz.jpenilla.jmplib;

import lombok.NonNull;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;
import xyz.jpenilla.jmplib.compatability.JMPLibPAPIHook;
import xyz.jpenilla.jmplib.compatability.JMPLibPrismaHook;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Chat message sending utilities
 *
 * @author jmp
 */
public class Chat {
    private static Chat CHAT_INSTANCE = null;

    private final JavaPlugin instance;
    private final BukkitAudiences audience;
    private final MiniMessage miniMessage;
    private final HashMap<String, String> centeredTempReplacements = new HashMap<>();
    private JMPLibPAPIHook papi = null;
    private JMPLibPrismaHook prisma = null;

    private Chat(JavaPlugin plugin) {
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
     * Will return null if chat hasn't been gotten with a JavaPlugin using {@link Chat#get(JavaPlugin)}
     *
     * @return The Chat instance
     */
    @Nullable
    public static Chat get() {
        return CHAT_INSTANCE;
    }

    @NonNull
    public static Chat get(@NonNull JavaPlugin plugin) {
        if (CHAT_INSTANCE == null) {
            CHAT_INSTANCE = new Chat(plugin);
        }
        return CHAT_INSTANCE;
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

    public void playSounds(@NonNull Player player, @NonNull String sounds, @NonNull boolean randomize) {
        final String[] s = sounds.split(",");
        if (randomize) {
            playSound(player, s[new Random().nextInt(s.length)]);
        } else {
            for (String sound : s) {
                playSound(player, sound);
            }
        }
    }

    public void playSound(@NonNull Player player, @NonNull String sound) {
        audience.player(player).playSound(Sound.of(Key.of(sound), Sound.Source.MASTER, 1.0f, 1.0f));
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
        final String msg;
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

    public Title getTitle(@NonNull String title, @NonNull String subTitle, @NonNull ChronoUnit fadeInTimeUnit, @NonNull int fadeInTime, @NonNull ChronoUnit stayTimeUnit, @NonNull int stayTime, @NonNull ChronoUnit fadeOutTimeUnit, @NonNull int fadeOutTime) {
        final Component titleComponent = miniMessage.parse(title);
        final Component subTitleComponent = miniMessage.parse(subTitle);
        return Title.of(titleComponent, subTitleComponent, Duration.of(fadeInTime, fadeInTimeUnit), Duration.of(stayTime, stayTimeUnit), Duration.of(fadeOutTime, fadeOutTimeUnit));
    }

    public Title getTitleSeconds(@NonNull String title, @NonNull String subTitle, @NonNull int fadeInTime, @NonNull int stayTime, @NonNull int fadeOutTime) {
        return getTitle(title, subTitle, ChronoUnit.SECONDS, fadeInTime, ChronoUnit.SECONDS, stayTime, ChronoUnit.SECONDS, fadeOutTime);
    }

    public void showTitle(@NonNull Player player, @NonNull String title, @NonNull String subTitle, @NonNull ChronoUnit fadeInTimeUnit, @NonNull int fadeInTime, @NonNull ChronoUnit stayTimeUnit, @NonNull int stayTime, @NonNull ChronoUnit fadeOutTimeUnit, @NonNull int fadeOutTime) {
        showTitle(player, getTitle(title, subTitle, fadeInTimeUnit, fadeInTime, stayTimeUnit, stayTime, fadeOutTimeUnit, fadeOutTime));
    }

    public void showTitle(@NonNull Player player, @NonNull Title title) {
        audience.player(player).showTitle(title);
    }

    public void sendActionBar(@NonNull Player player, @NonNull String text) {
        audience.player(player).sendActionBar(miniMessage.parse(text));
    }

    public BukkitTask sendActionBar(@NonNull Player player, @NonNull int durationSeconds, @NonNull String text) {
        BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(instance, () -> {
            sendActionBar(player, text);
        }, 0, 20L * 2);
        Bukkit.getScheduler().runTaskLaterAsynchronously(instance, task::cancel, 20L * durationSeconds);
        return task;
    }

    /**
     * Get the amount of spaces needed to center the message
     *
     * @param message MiniMessage formatted message
     * @return String of spaces
     */
    public String getCenteredSpacePrefix(@NonNull String message) {
        String specialMessage = TextUtil.replacePlaceholders(message, centeredTempReplacements, false);
        return LegacyChat.getCenteredSpacePrefix(miniMessage.stripTokens(specialMessage));
    }

    /**
     * Get a MiniMessage string prefixed with spaces to center it
     *
     * @param message MiniMessage formatted message
     * @return Centered MiniMessage
     */
    public String getCenteredMessage(@NonNull String message) {
        return getCenteredSpacePrefix(message) + message;
    }

    /**
     * Center a list of MiniMessage Strings
     *
     * @param messages List of MiniMessage Strings
     * @return Centered MiniMessage Strings
     */
    public List<String> getCenteredMessage(@NonNull List<String> messages) {
        ArrayList<String> l = new ArrayList<>();
        for (String message : messages) {
            l.add(getCenteredMessage(message));
        }
        return l;
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
        final ArrayList<String> l = new ArrayList<>();
        for (String m : messages) {
            l.add(replacePlaceholders(player, m, placeholders));
        }
        return l;
    }
}
