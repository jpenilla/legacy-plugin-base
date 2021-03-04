package xyz.jpenilla.jmplib;

import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import xyz.jpenilla.jmplib.compatability.JMPLibPAPIHook;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Chat message sending utilities
 *
 * @author jmp
 */
public class Chat {
    private final BasePlugin basePlugin;

    public Chat(BasePlugin plugin) {
        basePlugin = plugin;
    }

    /**
     * Parse a string with PlaceholderAPI returning an unchanged string if Player is null
     *
     * @param player  The player
     * @param message The message to parse
     * @return The parsed message
     */
    public String papiParse(@Nullable Player player, @NonNull String message) {
        final JMPLibPAPIHook hook = basePlugin.papiHook();
        if (player != null && hook != null) {
            return hook.translate(player, message);
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
        if (player != null && basePlugin.papiHook() != null) {
            ArrayList<String> l = new ArrayList<>();
            for (String m : messages) {
                l.add(papiParse(player, m));
            }
            return l;
        } else {
            return messages;
        }
    }

    public void playSounds(@NonNull Player player, boolean randomize, @NonNull String... sounds) {
        if (randomize) {
            playSound(player, sounds[ThreadLocalRandom.current().nextInt(sounds.length)]);
        } else {
            for (String sound : sounds) {
                playSound(player, sound);
            }
        }
    }

    public void playSounds(@NonNull Player player, boolean randomize, @NonNull String sounds) {
        String[] s = sounds.split(",");
        playSounds(player, randomize, s);
    }

    public void playSound(@NonNull Player player, @NonNull String sound) {
        basePlugin.audiences().player(player).playSound(Sound.sound(Key.key(sound), Sound.Source.MASTER, 1.0f, 1.0f));
    }

    public void sendParsed(@NonNull CommandSender sender, @NonNull List<String> messages) {
        sendParsed(sender, messages, null);
    }

    public void sendParsed(@NonNull CommandSender sender, @NonNull List<String> messages, @Nullable Map<String, String> placeholders) {
        for (String message : messages) {
            sendParsed(sender, message, placeholders);
        }
    }

    public void broadcast(@NonNull String message) {
        basePlugin.audiences().players().sendMessage(Identity.nil(), basePlugin.miniMessage().parse(message));
    }

    public void broadcast(@NonNull List<String> messages) {
        for (String message : messages) {
            broadcast(message);
        }
    }

    public void send(@NonNull CommandSender sender, @NonNull Component component) {
        if (sender instanceof Player) {
            basePlugin.audiences().player((Player) sender).sendMessage(Identity.nil(), component);
        } else {
            basePlugin.audiences().console().sendMessage(Identity.nil(), component);
        }
    }

    public void send(@NonNull CommandSender sender, @NonNull List<?> messages) {
        if (!messages.isEmpty()) {
            if (messages.get(0) instanceof String) {
                for (Object message : messages) {
                    send(sender, (String) message);
                }
            } else {
                for (Object message : messages) {
                    send(sender, (Component) message);
                }
            }
        }
    }

    public void sendParsed(@NonNull CommandSender sender, @NonNull String message, @Nullable Map<String, String> placeholders) {
        final String msg;
        if (sender instanceof Player) {
            msg = parse((Player) sender, message, placeholders);
        } else {
            msg = parse(null, message, placeholders);
        }
        send(sender, msg);
    }

    public void sendParsed(@NonNull CommandSender sender, @NonNull String message) {
        sendParsed(sender, message, null);
    }

    public void send(@NonNull CommandSender sender, @NonNull String message) {
        send(sender, basePlugin.miniMessage().parse(message));
    }

    public Title getTitle(@NonNull String title, @NonNull String subTitle, @NonNull ChronoUnit fadeInTimeUnit, int fadeInTime, @NonNull ChronoUnit stayTimeUnit, int stayTime, @NonNull ChronoUnit fadeOutTimeUnit, int fadeOutTime) {
        final Component titleComponent = basePlugin.miniMessage().parse(title);
        final Component subTitleComponent = basePlugin.miniMessage().parse(subTitle);
        return Title.title(titleComponent, subTitleComponent, Title.Times.of(Duration.of(fadeInTime, fadeInTimeUnit), Duration.of(stayTime, stayTimeUnit), Duration.of(fadeOutTime, fadeOutTimeUnit)));
    }

    public Title getTitleSeconds(@NonNull String title, @NonNull String subTitle, int fadeInTime, int stayTime, int fadeOutTime) {
        return getTitle(title, subTitle, ChronoUnit.SECONDS, fadeInTime, ChronoUnit.SECONDS, stayTime, ChronoUnit.SECONDS, fadeOutTime);
    }

    public void showTitle(@NonNull Player player, @NonNull String title, @NonNull String subTitle, @NonNull ChronoUnit fadeInTimeUnit, int fadeInTime, @NonNull ChronoUnit stayTimeUnit, int stayTime, @NonNull ChronoUnit fadeOutTimeUnit, int fadeOutTime) {
        showTitle(player, getTitle(title, subTitle, fadeInTimeUnit, fadeInTime, stayTimeUnit, stayTime, fadeOutTimeUnit, fadeOutTime));
    }

    public void showTitle(@NonNull Player player, @NonNull Title title) {
        basePlugin.audiences().player(player).showTitle(title);
    }

    public void sendActionBar(@NonNull Player player, @NonNull String text) {
        basePlugin.audiences().player(player).sendActionBar(basePlugin.miniMessage().parse(text));
    }

    public BukkitTask sendActionBar(@NonNull Player player, int durationSeconds, @NonNull String text) {
        final BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(basePlugin, () -> sendActionBar(player, text), 0, 20L * 2);
        Bukkit.getScheduler().runTaskLaterAsynchronously(basePlugin, task::cancel, 20L * durationSeconds);
        return task;
    }

    /**
     * Get the amount of spaces needed to center the message
     *
     * @param message MiniMessage formatted message
     * @return String of spaces
     */
    public static String getCenteredSpacePrefix(@NonNull String message) {
        return ChatCentering.spacePrefix(MiniMessage.get().parse(message));
    }

    /**
     * Get a MiniMessage string prefixed with spaces to center it
     *
     * @param message MiniMessage formatted message
     * @return Centered MiniMessage
     */
    public static String getCenteredMessage(@NonNull String message) {
        return getCenteredSpacePrefix(message) + message;
    }

    /**
     * Center a list of MiniMessage Strings
     *
     * @param messages List of MiniMessage Strings
     * @return Centered MiniMessage Strings
     */
    public static List<String> getCenteredMessage(@NonNull List<String> messages) {
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
    public String parse(@Nullable Player player, @NonNull String message, @Nullable Map<String, String> placeholders) {
        return papiParse(player, TextUtil.replacePlaceholders(message, placeholders));
    }

    /**
     * Parse the given strings with PAPI, Prisma, and Placeholders
     *
     * @param player       The player
     * @param messages     The message
     * @param placeholders Placeholders
     * @return Parsed messages
     */
    public List<String> parse(@Nullable Player player, @NonNull List<String> messages, @Nullable Map<String, String> placeholders) {
        final ArrayList<String> l = new ArrayList<>();
        for (String m : messages) {
            l.add(parse(player, m, placeholders));
        }
        return l;
    }
}
