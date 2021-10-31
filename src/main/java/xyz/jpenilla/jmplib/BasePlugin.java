package xyz.jpenilla.jmplib;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import xyz.jpenilla.jmplib.compatability.JMPLibPAPIHook;

import java.nio.file.Path;

public abstract class BasePlugin extends JavaPlugin {
    private static BasePlugin basePlugin;
    private Chat chat;
    private JMPLibPAPIHook papi = null;
    private BukkitAudiences audiences;
    private ConversationFactory conversationFactory;
    private final MiniMessage miniMessage;
    private Path dataPath;

    public BasePlugin() {
        super();
        this.miniMessage = MiniMessage.miniMessage();
    }

    public static BasePlugin getBasePlugin() {
        return BasePlugin.basePlugin;
    }

    @Override
    public final void onEnable() {
        basePlugin = this;

        this.dataPath = getDataFolder().toPath();
        this.audiences = BukkitAudiences.create(this);
        this.conversationFactory = new ConversationFactory(this);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.papi = new JMPLibPAPIHook();
        }

        this.chat = new Chat(this);

        this.onPluginEnable();
    }

    public abstract void onPluginEnable();

    public @NonNull Chat chat() {
        return this.chat;
    }

    public @Nullable JMPLibPAPIHook papiHook() {
        return this.papi;
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
