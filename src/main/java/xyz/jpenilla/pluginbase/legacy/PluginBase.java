package xyz.jpenilla.pluginbase.legacy;

import java.nio.file.Path;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import xyz.jpenilla.pluginbase.legacy.compatability.PlaceholderAPIHook;

public abstract class PluginBase extends JavaPlugin {
    private static PluginBase instance;
    private Chat chat;
    private PlaceholderAPIHook placeholderApi = null;
    private BukkitAudiences audiences;
    private ConversationFactory conversationFactory;
    private final MiniMessage miniMessage;
    private Path dataPath;

    public PluginBase() {
        super();
        this.miniMessage = MiniMessage.miniMessage();
    }

    public static PluginBase instance() {
        return instance;
    }

    @Override
    public final void onEnable() {
        instance = this;

        this.dataPath = getDataFolder().toPath();
        this.audiences = BukkitAudiences.create(this);
        this.conversationFactory = new ConversationFactory(this);

        this.checkForPlaceholderApi();
        // In case the softdepend is missing...
        this.getServer().getScheduler().runTask(this, this::checkForPlaceholderApi);

        this.chat = new Chat(this);

        this.enable();
    }

    private void checkForPlaceholderApi() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.placeholderApi = new PlaceholderAPIHook();
        } else {
            this.placeholderApi = null;
        }
    }

    public abstract void enable();

    public @NonNull Chat chat() {
        return this.chat;
    }

    public @Nullable PlaceholderAPIHook placeholderApi() {
        return this.placeholderApi;
    }

    public @NonNull BukkitAudiences audiences() {
        return this.audiences;
    }

    public @NonNull ConversationFactory conversationFactory() {
        return this.conversationFactory;
    }

    public @NonNull MiniMessage miniMessage() {
        return this.miniMessage;
    }

    public @NonNull Path dataPath() {
        return this.dataPath;
    }
}
