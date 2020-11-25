package xyz.jpenilla.jmplib;

import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.jpenilla.jmplib.compatability.JMPLibPAPIHook;
import xyz.jpenilla.jmplib.compatability.JMPLibPrismaHook;

import java.nio.file.Path;

public abstract class BasePlugin extends JavaPlugin {
    @Getter private static BasePlugin basePlugin;
    @Getter private Chat chat;
    @Getter private JMPLibPAPIHook papi = null;
    @Getter private JMPLibPrismaHook prisma = null;
    @Getter private BukkitAudiences audience = null;
    @Getter private ConversationFactory conversationFactory;
    @Getter private boolean isPaperServer;
    @Getter private final String serverPackageName;
    @Getter private final String serverApiVersion;
    @Getter private final int majorMinecraftVersion;
    @Getter private final MiniMessage miniMessage;
    @Getter private Path dataPath;

    public BasePlugin() {
        super();
        this.miniMessage = MiniMessage.get();

        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            this.isPaperServer = true;
        } catch (ClassNotFoundException e) {
            this.isPaperServer = false;
        }

        this.serverPackageName = this.getServer().getClass().getPackage().getName();
        this.serverApiVersion = this.serverPackageName.substring(this.serverPackageName.lastIndexOf('.') + 1);
        this.majorMinecraftVersion = Integer.parseInt(this.serverApiVersion.split("_")[1]);
    }

    @Override
    public final void onEnable() {
        basePlugin = this;

        this.dataPath = getDataFolder().toPath();
        this.audience = BukkitAudiences.create(this);
        this.conversationFactory = new ConversationFactory(this);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.papi = new JMPLibPAPIHook();
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Prisma")) {
            this.prisma = new JMPLibPrismaHook();
        }

        this.chat = new Chat(this);

        this.onPluginEnable();
    }

    public abstract void onPluginEnable();
}
