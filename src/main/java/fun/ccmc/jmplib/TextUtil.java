package fun.ccmc.jmplib;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Text Utilities
 */
public class TextUtil {
    /**
     * Colorize the given string using the and symbol color code
     *
     * @param string The string to colorize
     * @return The colorized string
     */
    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Colorize the given array of strings using the and symbol color code
     *
     * @param strings The array of strings to colorize
     * @return The colorized string array
     */
    public static String[] colorize(String[] strings) {
        return (String[]) Arrays.stream(strings).map(TextUtil::colorize).toArray();
    }

    /**
     * Colorize the given list of strings using the and symbol color code
     *
     * @param stringList The list of strings to colorize
     * @return The colorized strings as an ArrayList
     */
    public static ArrayList<String> colorize(List<String> stringList) {
        return stringList.stream().map(TextUtil::colorize).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Checks a list of strings for the given string,
     * ignoring case.
     *
     * @param string The string to look for
     * @param list The list to look for the string in
     * @return True if the list contains the string case-insensitive
     */
    public static boolean containsCaseInsensitive(String string, List<String> list){
        return list.stream().anyMatch(x -> x.equalsIgnoreCase(string));
    }
}
