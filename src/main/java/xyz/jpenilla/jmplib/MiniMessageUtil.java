package xyz.jpenilla.jmplib;

import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

public class MiniMessageUtil {
    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build();
    private static final LegacyComponentSerializer DOWNSAMPLING_SERIALIZER = LegacyComponentSerializer.legacySection();
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public static String miniMessageToLegacy(final @NonNull String message) {
        return miniMessageToLegacy(message, false);
    }

    public static String miniMessageToLegacy(final @NonNull String message, final boolean disableItalics) {
        final LegacyComponentSerializer serializer = Environment.majorMinecraftVersion() >= 16 ? SERIALIZER : DOWNSAMPLING_SERIALIZER;
        final Component comp = miniMessage.deserialize(BasePlugin.getBasePlugin().chat().parse(null, message, null));
        return serializer.serialize(disableItalics ? ItemBuilder.removeItalics(comp) : comp);
    }

    public static List<String> miniMessageToLegacy(final @NonNull List<String> messages) {
        return miniMessageToLegacy(messages, false);
    }

    public static List<String> miniMessageToLegacy(final @NonNull List<String> messages, final boolean disableItalics) {
        final List<String> result = new ArrayList<>();
        for (final String message : messages) {
            result.add(miniMessageToLegacy(message));
        }
        return result;
    }
}
