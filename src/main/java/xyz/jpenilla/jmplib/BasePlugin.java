package xyz.jpenilla.jmplib;

import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.jpenilla.jmplib.compatability.JMPLibPAPIHook;
import xyz.jpenilla.jmplib.compatability.JMPLibPrismaHook;

public abstract class BasePlugin extends JavaPlugin {
    @Getter private static BasePlugin basePlugin;
    @Getter private Chat chat;
    @Getter private JMPLibPAPIHook papi = null;
    @Getter private JMPLibPrismaHook prisma = null;
    @Getter private BukkitAudiences audience = null;
    @Getter private ConversationFactory conversationFactory;
    @Getter private boolean isPaperServer;
    @Getter private String serverPackageName;
    @Getter private String serverApiVersion;
    @Getter private int majorMinecraftVersion;
    @Getter private final MiniMessage miniMessage;

    public BasePlugin() {
        super();
        this.miniMessage = MiniMessage.get();

        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            isPaperServer = true;
        } catch (ClassNotFoundException e) {
            isPaperServer = false;
        }
    }

    @Override
    public final void onEnable() {
        basePlugin = this;

        serverPackageName = this.getServer().getClass().getPackage().getName();
        serverApiVersion = serverPackageName.substring(serverPackageName.lastIndexOf('.') + 1);
        majorMinecraftVersion = Integer.parseInt(serverApiVersion.split("_")[1]);

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
