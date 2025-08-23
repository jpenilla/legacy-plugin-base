package xyz.jpenilla.pluginbase.legacy;

import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class MiniMessageUtil {
    private MiniMessageUtil() {
    }

    public static String miniMessageToLegacy(final String message) {
        return miniMessageToLegacy(message, false);
    }

    public static String miniMessageToLegacy(final String message, final boolean disableItalics) {
        final LegacyComponentSerializer serializer = ComponentUtil.legacySerializer();
        final Component comp = PluginBase.instance().miniMessage()
                .deserialize(PluginBase.instance().chat().parse(null, message, null));
        return serializer.serialize(disableItalics ? ComponentUtil.disableItalics(comp) : comp);
    }

    public static List<String> miniMessageToLegacy(final List<String> messages) {
        return miniMessageToLegacy(messages, false);
    }

    public static List<String> miniMessageToLegacy(final List<String> messages, final boolean disableItalics) {
        final List<String> result = new ArrayList<>();
        for (final String message : messages) {
            result.add(miniMessageToLegacy(message));
        }
        return result;
    }
}
