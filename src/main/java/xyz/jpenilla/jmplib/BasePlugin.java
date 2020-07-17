package xyz.jpenilla.jmplib;

import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.jpenilla.jmplib.compatability.JMPLibPAPIHook;
import xyz.jpenilla.jmplib.compatability.JMPLibPrismaHook;

public class BasePlugin extends JavaPlugin {
    @Getter private static BasePlugin basePlugin;
    @Getter private Chat chat;
    @Getter private JMPLibPAPIHook papi = null;
    @Getter private JMPLibPrismaHook prisma = null;
    @Getter private MiniMessage miniMessage;

    @Override
    public void onEnable() {
        basePlugin = this;
        miniMessage = MiniMessage.get();

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.papi = new JMPLibPAPIHook();
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Prisma")) {
            this.prisma = new JMPLibPrismaHook();
        }

        this.chat = new Chat(this);
    }
}
