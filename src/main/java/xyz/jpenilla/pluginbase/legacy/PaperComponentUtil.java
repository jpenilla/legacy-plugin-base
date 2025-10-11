package xyz.jpenilla.pluginbase.legacy;

import com.google.gson.JsonElement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import xyz.jpenilla.pluginbase.legacy.environment.Environment;

import java.lang.reflect.Method;

@NullMarked
public final class PaperComponentUtil {
    private static final String KYORI_PREFIX = String.join(".", "net", "kyori");
    private static final @Nullable Object NATIVE_GSON;
    private static final @Nullable Method NATIVE_DESERIALIZE;
    private static final @Nullable Method NATIVE_SERIALIZE;

    static {
        if (!Environment.paper()) {
            NATIVE_GSON = null;
            NATIVE_DESERIALIZE = null;
            NATIVE_SERIALIZE = null;
        } else {
            try {
                final Class<?> gsonClass = Class.forName(String.join(".", "net", "kyori", "adventure", "text", "serializer", "gson", "GsonComponentSerializer"));
                NATIVE_DESERIALIZE = gsonClass.getMethod("deserializeFromTree", JsonElement.class);
                NATIVE_SERIALIZE = gsonClass.getMethod("serializeToTree", nativeAdventureComponentClass());
                NATIVE_GSON = gsonClass.getDeclaredMethod("gson").invoke(null);
            } catch (final ReflectiveOperationException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private PaperComponentUtil() {
    }

    public static Object toNative(final Component component) {
        if (component.getClass().getName().startsWith(KYORI_PREFIX)) {
            return component;
        }
        if (NATIVE_GSON == null || NATIVE_DESERIALIZE == null) {
            throw new IllegalStateException();
        }
        try {
            return NATIVE_DESERIALIZE.invoke(NATIVE_GSON, GsonComponentSerializer.gson().serializeToTree(component));
        } catch (final ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }

    @SuppressWarnings("unused")
    public static Component fromNative(final Object component) {
        if (Component.class.getName().startsWith(KYORI_PREFIX)) {
            return (Component) component;
        }
        if (NATIVE_GSON == null || NATIVE_SERIALIZE == null) {
            throw new IllegalStateException();
        }
        try {
            return GsonComponentSerializer.gson().deserializeFromTree((JsonElement) NATIVE_SERIALIZE.invoke(NATIVE_GSON, component));
        } catch (final ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Class<?> nativeAdventureComponentClass() {
        try {
            return Class.forName(String.join(".", "net", "kyori", "adventure", "text", "Component"));
        } catch (final ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}
