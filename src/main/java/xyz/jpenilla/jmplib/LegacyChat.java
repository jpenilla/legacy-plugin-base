package xyz.jpenilla.jmplib;

import lombok.NonNull;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class LegacyChat {
    private final static int CENTER_PX = 154;

    /**
     * Send a ComponentBuilder message to a player
     *
     * @param p The player to send to
     * @param c The ComponentBuilder
     */
    public static void sendMsg(@NonNull Player p, @NonNull ComponentBuilder c) {
        p.spigot().sendMessage(c.create());
    }

    /**
     * Colorize and then broadcast a message to the server
     *
     * @param plugin instance of plugin main class
     * @param msg    the message to broadcast
     */
    public static void broadcastMsg(@NonNull JavaPlugin plugin, @NonNull String msg) {
        if (!msg.equals("")) {
            plugin.getServer().broadcastMessage(TextUtil.colorize(msg));
        }
    }

    /**
     * Sends a message msg to Player player.
     * Colorizes the message.
     *
     * @param player The player to send the message to
     * @param msg    The message to colorize and send
     */
    public static void sendMsg(@NonNull Player player, @NonNull String msg) {
        player.sendMessage(TextUtil.colorize(msg));
    }

    /**
     * Sends an array of messages msg to Player player
     * Colorizes the messages.
     *
     * @param player The player to send the messages to
     * @param msg    The messages to colorize and send
     */
    public static void sendMsg(@NonNull Player player, @NonNull String[] msg) {
        player.sendMessage(TextUtil.colorize(msg));
    }

    /**
     * Sends a message msg to CommandSender sender.
     * Colorizes the message.
     *
     * @param sender The CommandSender to send the message to
     * @param msg    The message to colorize and send
     */
    public static void sendMsg(@NonNull CommandSender sender, @NonNull String msg) {
        sender.sendMessage(TextUtil.colorize(msg));
    }

    /**
     * Sends an array of messages msg to CommandSender sender.
     * Colorizes the messages.
     *
     * @param sender The CommandSender to send the messages to
     * @param msg    The messages to colorize and send
     */
    public static void sendMsg(@NonNull CommandSender sender, @NonNull String[] msg) {
        sender.sendMessage(TextUtil.colorize(msg));
    }

    /**
     * Sends a colorized and centered message to a player
     *
     * @param player  The player to send the centered message to
     * @param message The message to colorize and center and send
     */
    public static void sendCenteredMessage(@NonNull Player player, @NonNull String message) {
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '&') {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        sendMsg(player, sb.toString() + message);
    }

    /**
     * Sends an array of colorized and centered messages to a player
     *
     * @param player  The player to send the centered messages to
     * @param message The messages to colorize and center and send
     */
    public static void sendCenteredMessage(@NonNull Player player, @NonNull String[] message) {
        for (String s : message) {
            sendCenteredMessage(player, s);
        }
    }

    /**
     * Sends a colorized and centered message to a CommandSender
     * Only centers if the CommandSender is a Player
     *
     * @param sender  The CommandSender to send the centered message to
     * @param message The message to colorize and center and send
     */
    public static void sendCenteredMessage(@NonNull CommandSender sender, @NonNull String message) {
        if (sender instanceof Player) {
            sendCenteredMessage((Player) sender, message);
        } else {
            sendMsg(sender, message);
        }
    }

    /**
     * Sends an array of colorized and centered message to a CommandSender
     * Only centers if the CommandSender is a Player
     *
     * @param sender  The CommandSender to send the centered message to
     * @param message The message to colorize and center and send
     */
    public static void sendCenteredMessage(@NonNull CommandSender sender, @NonNull String[] message) {
        if (sender instanceof Player) {
            sendCenteredMessage((Player) sender, message);
        } else {
            sendMsg(sender, message);
        }
    }

    public enum DefaultFontInfo {
        A('A', 5),
        a('a', 5),
        B('B', 5),
        b('b', 5),
        C('C', 5),
        c('c', 5),
        D('D', 5),
        d('d', 5),
        E('E', 5),
        e('e', 5),
        F('F', 5),
        f('f', 4),
        G('G', 5),
        g('g', 5),
        H('H', 5),
        h('h', 5),
        I('I', 3),
        i('i', 1),
        J('J', 5),
        j('j', 5),
        K('K', 5),
        k('k', 4),
        L('L', 5),
        l('l', 1),
        M('M', 5),
        m('m', 5),
        N('N', 5),
        n('n', 5),
        O('O', 5),
        o('o', 5),
        P('P', 5),
        p('p', 5),
        Q('Q', 5),
        q('q', 5),
        R('R', 5),
        r('r', 5),
        S('S', 5),
        s('s', 5),
        T('T', 5),
        t('t', 4),
        U('U', 5),
        u('u', 5),
        V('V', 5),
        v('v', 5),
        W('W', 5),
        w('w', 5),
        X('X', 5),
        x('x', 5),
        Y('Y', 5),
        y('y', 5),
        Z('Z', 5),
        z('z', 5),
        NUM_1('1', 5),
        NUM_2('2', 5),
        NUM_3('3', 5),
        NUM_4('4', 5),
        NUM_5('5', 5),
        NUM_6('6', 5),
        NUM_7('7', 5),
        NUM_8('8', 5),
        NUM_9('9', 5),
        NUM_0('0', 5),
        EXCLAMATION_POINT('!', 1),
        AT_SYMBOL('@', 6),
        NUM_SIGN('#', 5),
        DOLLAR_SIGN('$', 5),
        PERCENT('%', 5),
        UP_ARROW('^', 5),
        AMPERSAND('&', 5),
        ASTERISK('*', 5),
        LEFT_PARENTHESIS('(', 4),
        RIGHT_PERENTHESIS(')', 4),
        MINUS('-', 5),
        UNDERSCORE('_', 5),
        PLUS_SIGN('+', 5),
        EQUALS_SIGN('=', 5),
        LEFT_CURL_BRACE('{', 4),
        RIGHT_CURL_BRACE('}', 4),
        LEFT_BRACKET('[', 3),
        RIGHT_BRACKET(']', 3),
        COLON(':', 1),
        SEMI_COLON(';', 1),
        DOUBLE_QUOTE('"', 3),
        SINGLE_QUOTE('\'', 1),
        LEFT_ARROW('<', 4),
        RIGHT_ARROW('>', 4),
        QUESTION_MARK('?', 5),
        SLASH('/', 5),
        BACK_SLASH('\\', 5),
        LINE('|', 1),
        TILDE('~', 5),
        TICK('`', 2),
        PERIOD('.', 1),
        COMMA(',', 1),
        SPACE(' ', 3),
        DEFAULT('a', 4);

        private final char character;
        private final int length;

        DefaultFontInfo(char character, int length) {
            this.character = character;
            this.length = length;
        }

        public static DefaultFontInfo getDefaultFontInfo(char c) {
            for (DefaultFontInfo dFI : DefaultFontInfo.values()) {
                if (dFI.getCharacter() == c) {
                    return dFI;
                }
            }
            return DefaultFontInfo.DEFAULT;
        }

        public char getCharacter() {
            return this.character;
        }

        public int getLength() {
            return this.length;
        }

        public int getBoldLength() {
            if (this == DefaultFontInfo.SPACE) {
                return this.getLength();
            }
            return this.length + 1;
        }
    }
}
