package xyz.jpenilla.pluginbase.legacy;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Text Utilities
 */
public class TextUtil {
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
    public static String replacePlaceholders(@NonNull String message, @Nullable Map<String, String> placeholders, boolean curlyBrackets) {
        if (placeholders == null) {
            return message;
        }
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
