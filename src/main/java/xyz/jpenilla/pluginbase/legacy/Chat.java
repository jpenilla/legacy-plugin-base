package xyz.jpenilla.pluginbase.legacy;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import xyz.jpenilla.pluginbase.legacy.compatability.PlaceholderAPIHook;

@DefaultQualifier(NonNull.class)
@SuppressWarnings("unused")
public class Chat {
    private final PluginBase plugin;

    public Chat(final PluginBase plugin) {
        this.plugin = plugin;
    }

    /**
     * Parse a string with PlaceholderAPI returning an unchanged string if Player is null
     *
     * @param player  The player
     * @param message The message to parse
     * @return The parsed message
     */
    public String papiParse(final @Nullable Player player, final String message) {
        final @Nullable PlaceholderAPIHook hook = this.plugin.placeholderApi();
        if (player != null && hook != null) {
            return hook.replacePlaceholders(player, message);
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
    public List<String> papiParse(final @Nullable Player player, final List<String> messages) {
        if (player != null && this.plugin.placeholderApi() != null) {
            ArrayList<String> l = new ArrayList<>();
            for (String m : messages) {
                l.add(this.papiParse(player, m));
            }
            return l;
        } else {
            return messages;
        }
    }

    public void playSounds(Player player, boolean randomize, String... sounds) {
        if (randomize) {
            this.playSound(player, sounds[ThreadLocalRandom.current().nextInt(sounds.length)]);
        } else {
            for (String sound : sounds) {
                this.playSound(player, sound);
            }
        }
    }

    public void playSounds(Player player, boolean randomize, String sounds) {
        String[] s = sounds.split(",");
        this.playSounds(player, randomize, s);
    }

    public void playSound(Player player, String sound) {
        this.plugin.audiences().player(player).playSound(Sound.sound(Key.key(sound), Sound.Source.MASTER, 1.0f, 1.0f));
    }

    public void sendParsed(CommandSender sender, List<String> messages) {
        this.sendParsed(sender, messages, null);
    }

    public void sendParsed(CommandSender sender, List<String> messages, @Nullable Map<String, String> placeholders) {
        for (String message : messages) {
            this.sendParsed(sender, message, placeholders);
        }
    }

    public void broadcast(String message) {
        this.plugin.audiences().players().sendMessage(this.plugin.miniMessage().deserialize(message));
    }

    public void broadcast(List<String> messages) {
        for (String message : messages) {
            this.broadcast(message);
        }
    }

    public void send(CommandSender sender, ComponentLike component) {
        if (sender instanceof Player) {
            this.plugin.audiences().player((Player) sender).sendMessage(component);
        } else {
            this.plugin.audiences().sender(sender).sendMessage(component);
        }
    }

    public void send(CommandSender sender, List<?> messages) {
        if (!messages.isEmpty()) {
            if (messages.get(0) instanceof String) {
                for (Object message : messages) {
                    this.send(sender, (String) message);
                }
            } else {
                for (Object message : messages) {
                    this.send(sender, (Component) message);
                }
            }
        }
    }

    public void sendParsed(CommandSender sender, String message, @Nullable Map<String, String> placeholders) {
        final String msg;
        if (sender instanceof Player) {
            msg = this.parse((Player) sender, message, placeholders);
        } else {
            msg = this.parse(null, message, placeholders);
        }
        this.send(sender, msg);
    }

    public void sendParsed(CommandSender sender, String message) {
        this.sendParsed(sender, message, null);
    }

    public void send(CommandSender sender, String message) {
        this.send(sender, this.plugin.miniMessage().deserialize(message));
    }

    public Title getTitle(String title, String subTitle, ChronoUnit fadeInTimeUnit, int fadeInTime, ChronoUnit stayTimeUnit, int stayTime, ChronoUnit fadeOutTimeUnit, int fadeOutTime) {
        final Component titleComponent = this.plugin.miniMessage().deserialize(title);
        final Component subTitleComponent = this.plugin.miniMessage().deserialize(subTitle);
        return Title.title(
                titleComponent,
                subTitleComponent,
                Title.Times.times(
                        Duration.of(fadeInTime, fadeInTimeUnit),
                        Duration.of(stayTime, stayTimeUnit),
                        Duration.of(fadeOutTime, fadeOutTimeUnit)
                )
        );
    }

    public Title getTitleSeconds(String title, String subTitle, int fadeInTime, int stayTime, int fadeOutTime) {
        return this.getTitle(title, subTitle, ChronoUnit.SECONDS, fadeInTime, ChronoUnit.SECONDS, stayTime, ChronoUnit.SECONDS, fadeOutTime);
    }

    public void showTitle(Player player, String title, String subTitle, ChronoUnit fadeInTimeUnit, int fadeInTime, ChronoUnit stayTimeUnit, int stayTime, ChronoUnit fadeOutTimeUnit, int fadeOutTime) {
        this.showTitle(player, this.getTitle(title, subTitle, fadeInTimeUnit, fadeInTime, stayTimeUnit, stayTime, fadeOutTimeUnit, fadeOutTime));
    }

    public void showTitle(Player player, Title title) {
        this.plugin.audiences().player(player).showTitle(title);
    }

    public void sendActionBar(Player player, String text) {
        this.plugin.audiences().player(player).sendActionBar(this.plugin.miniMessage().deserialize(text));
    }

    @Deprecated
    public BukkitTask sendActionBar(Player player, int durationSeconds, String text) {
        final BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, () -> this.sendActionBar(player, text), 0, 20L * 2);
        Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, task::cancel, 20L * durationSeconds);
        return task;
    }

    /**
     * Get the amount of spaces needed to center the message
     *
     * @param message MiniMessage formatted message
     * @return String of spaces
     */
    public static String getCenteredSpacePrefix(String message) {
        return ChatCentering.spacePrefix(PluginBase.instance().miniMessage().deserialize(message));
    }

    /**
     * Get a MiniMessage string prefixed with spaces to center it
     *
     * @param message MiniMessage formatted message
     * @return Centered MiniMessage
     */
    public static String getCenteredMessage(String message) {
        return getCenteredSpacePrefix(message) + message;
    }

    /**
     * Center a list of MiniMessage Strings
     *
     * @param messages List of MiniMessage Strings
     * @return Centered MiniMessage Strings
     */
    public static List<String> getCenteredMessage(List<String> messages) {
        ArrayList<String> l = new ArrayList<>();
        for (String message : messages) {
            l.add(getCenteredMessage(message));
        }
        return l;
    }

    /**
     * Parse the given string with PAPI, and Placeholders
     *
     * @param player       The player
     * @param message      The message
     * @param placeholders Placeholders
     * @return Parsed Message
     */
    public String parse(@Nullable Player player, String message, @Nullable Map<String, String> placeholders) {
        return this.papiParse(player, TextUtil.replacePlaceholders(message, placeholders));
    }

    /**
     * Parse the given strings with PAPI, and Placeholders
     *
     * @param player       The player
     * @param messages     The message
     * @param placeholders Placeholders
     * @return Parsed messages
     */
    public List<String> parse(@Nullable Player player, List<String> messages, @Nullable Map<String, String> placeholders) {
        final ArrayList<String> l = new ArrayList<>();
        for (String m : messages) {
            l.add(this.parse(player, m, placeholders));
        }
        return l;
    }
}
