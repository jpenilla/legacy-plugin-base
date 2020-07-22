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
    @Getter private MiniMessage miniMessage;
    @Getter private ConversationFactory conversationFactory;

    @Override
    public final void onEnable() {
        basePlugin = this;
        this.audience = BukkitAudiences.create(this);
        this.miniMessage = MiniMessage.get();
        this.conversationFactory = new ConversationFactory(this);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.papi = new JMPLibPAPIHook();
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Prisma")) {
            this.prisma = new JMPLibPrismaHook();
        }

        this.chat = new Chat(this);

        onPluginEnable();
    }

    public abstract void onPluginEnable();
}
