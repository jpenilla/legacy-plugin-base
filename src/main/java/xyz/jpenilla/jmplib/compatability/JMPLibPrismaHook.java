package xyz.jpenilla.jmplib.compatability;

import lombok.NonNull;
import us.eunoians.prisma.ColorProvider;

public class JMPLibPrismaHook {
    public String translate(@NonNull String message) {
        return ColorProvider.translatePrismaToHex(message);
    }
}
