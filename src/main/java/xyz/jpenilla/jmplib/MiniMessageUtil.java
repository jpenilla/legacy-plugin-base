package xyz.jpenilla.jmplib;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class MiniMessageUtil {
    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build();
    private static final LegacyComponentSerializer DOWNSAMPLING_SERIALIZER = LegacyComponentSerializer.legacySection();
    private static final MiniMessage miniMessage = MiniMessage.get();

    public static String miniMessageToLegacy(final @NonNull String message) {
        final LegacyComponentSerializer serializer = Environment.majorMinecraftVersion() >= 16 ? SERIALIZER : DOWNSAMPLING_SERIALIZER;
        return serializer.serialize(miniMessage.parse(BasePlugin.getBasePlugin().chat().parse(null, message, null)));
    }

    public static List<String> miniMessageToLegacy(final @NonNull List<String> messages) {
        final List<String> result = new ArrayList<>();
        for (final String message : messages) {
            result.add(miniMessageToLegacy(message));
        }
        return result;
    }
}
