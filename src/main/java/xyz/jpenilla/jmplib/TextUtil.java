package xyz.jpenilla.jmplib;

import org.bukkit.ChatColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Text Utilities
 *
 * @author jmp
 */
public class TextUtil {
    /**
     * Colorize the given string using the {@literal &} color code
     *
     * @param string The string to colorize
     * @return The colorized string
     */
    public static @NonNull String colorize(@NonNull String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Colorize the given array of strings using the {@literal &} color code
     *
     * @param strings The array of strings to colorize
     * @return The colorized string array
     */
    public static @NonNull String[] colorize(@NonNull String[] strings) {
        return Arrays.stream(strings).map(TextUtil::colorize).toArray(String[]::new);
    }

    /**
     * Colorize the given list of strings using the {@literal &} color code
     *
     * @param stringList The list of strings to colorize
     * @return The colorized strings as an ArrayList
     */
    public static @NonNull ArrayList<String> colorize(@NonNull List<String> stringList) {
        return stringList.stream().map(TextUtil::colorize).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Checks a list of strings for the given string,
     * ignoring case.
     *
     * @param string The string to look for
     * @param list   The list to look for the string in
     * @return True if the list contains the string case-insensitive
     */
    public static boolean containsCaseInsensitive(String string, @NonNull List<String> list) {
        return list.stream().anyMatch(x -> x.equalsIgnoreCase(string));
    }

    /**
     * Replace placeholders in a string from a given Map. Replaces {key} with value
     *
     * @param message      The message to parse
     * @param placeholders The placeholder Map
     * @return The parsed message
     */
    public static String replacePlaceholders(@NonNull String message, @Nullable Map<String, String> placeholders) {
        return replacePlaceholders(message, placeholders, true);
    }

    /**
     * Replace placeholders in a string from a given Map
     *
     * @param message       The message to parse
     * @param placeholders  The placeholder Map
     * @param curlyBrackets Whether to check for {} around the key
     * @return The parsed message
     */
    public static String replacePlaceholders(@NonNull String message, @Nullable Map<String, String> placeholders, @NonNull boolean curlyBrackets) {
        if (placeholders == null) {
            return message;
        } else {
            String finalMessage = message;
            for (Map.Entry<String, String> placeholder : placeholders.entrySet()) {
                String key = placeholder.getKey();
                if (curlyBrackets) {
                    key = "{" + key + "}";
                }
                finalMessage = finalMessage.replace(key, placeholder.getValue());
            }
            return finalMessage;
        }
    }

    /**
     * Replace placeholders in a List of strings from a given Map
     *
     * @param messages     The messages to parse
     * @param placeholders The placeholder Map
     * @return The parsed messages
     */
    public static List<String> replacePlaceholders(@NonNull List<String> messages, @Nullable Map<String, String> placeholders) {
        List<String> l = new ArrayList<>();
        for (String message : messages) {
            l.add(replacePlaceholders(message, placeholders));
        }
        return l;
    }
}
