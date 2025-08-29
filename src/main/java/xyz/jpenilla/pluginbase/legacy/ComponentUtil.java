package xyz.jpenilla.pluginbase.legacy;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ComponentUtil {
    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.builder()
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();
    private static final LegacyComponentSerializer DOWNSAMPLING_SERIALIZER = LegacyComponentSerializer.legacySection();

    private ComponentUtil() {
    }

    public static Component disableItalics(final Component component) {
        if (component.decoration(TextDecoration.ITALIC) == TextDecoration.State.NOT_SET) {
            return component.decoration(TextDecoration.ITALIC, false);
        }
        return component;
    }

    public static LegacyComponentSerializer legacySerializer() {
        return Environment.majorMinecraftVersion() >= 16 ? SERIALIZER : DOWNSAMPLING_SERIALIZER;
    }
}
