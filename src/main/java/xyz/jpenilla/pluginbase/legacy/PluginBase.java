package xyz.jpenilla.pluginbase.legacy;

import io.papermc.paper.threadedregions.RegionizedServerInitEvent;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import xyz.jpenilla.pluginbase.legacy.compatability.PlaceholderAPIHook;

import java.nio.file.Path;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class PluginBase extends JavaPlugin {
    private static PluginBase instance;
    private PlaceholderAPIHook placeholderApi = null;
    private BukkitAudiences audiences;
    private final MiniMessage miniMessage;
    private Path dataPath;
    private final Queue<Runnable> initTasks = new ConcurrentLinkedQueue<>();

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
        this.setupInitListener();

        this.dataPath = this.getDataFolder().toPath();
        this.audiences = BukkitAudiences.create(this);

        this.checkForPlaceholderApi();
        // In case the softdepend is missing...
        this.initTasks.add(this::checkForPlaceholderApi);

        this.enable();
    }

    public abstract void enable();

    @Override
    public final void onDisable() {
        this.disable();
        instance = null;
        if (this.audiences != null) {
            this.audiences.close();
            this.audiences = null;
        }
    }

    public abstract void disable();

    private void checkForPlaceholderApi() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.placeholderApi = new PlaceholderAPIHook();
        } else {
            this.placeholderApi = null;
        }
    }

    public @Nullable PlaceholderAPIHook placeholderApi() {
        return this.placeholderApi;
    }

    public @NonNull BukkitAudiences audiences() {
        return this.audiences;
    }

    public @NonNull MiniMessage miniMessage() {
        return this.miniMessage;
    }

    public @NonNull Path dataPath() {
        return this.dataPath;
    }

    private void setupInitListener() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            this.getServer().getPluginManager().registerEvents(new Listener() {
                @EventHandler
                public void handle(final RegionizedServerInitEvent event) {
                    PluginBase.this.onInit();
                }
            }, this);
        } catch (final ClassNotFoundException ex) {
            this.getServer().getScheduler().runTask(this, this::onInit);
        }
    }

    private void onInit() {
        for (Runnable task; (task = this.initTasks.poll()) != null; ) {
            task.run();
        }
    }
}
