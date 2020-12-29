package xyz.jpenilla.jmplib;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.ArrayList;
import java.util.List;

public class MiniMessageUtil {
    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build();
    private static final LegacyComponentSerializer DOWNSAMPLING_SERIALIZER = LegacyComponentSerializer.builder().build();
    private static final MiniMessage miniMessage = MiniMessage.get();

    /**
     * This sucks avoid using it at all costs
     *
     * @param message The MiniMessage text to be translated
     * @return The legacy text
     */
    public static String miniMessageToLegacy(String message) {
        final LegacyComponentSerializer serializer = Environment.majorMinecraftVersion() >= 16 ? SERIALIZER : DOWNSAMPLING_SERIALIZER;
        return serializer.serialize(miniMessage.parse(BasePlugin.getBasePlugin().chat().parse(null, message, null)));
    }

    /**
     * This sucks avoid using it at all costs
     *
     * @param messages The MiniMessage texts to be translated
     * @return The legacy texts
     */
    public static List<String> miniMessageToLegacy(List<String> messages) {
        final List<String> l = new ArrayList<>();
        for (String message : messages) {
            l.add(miniMessageToLegacy(message));
        }
        return l;
    }
}
