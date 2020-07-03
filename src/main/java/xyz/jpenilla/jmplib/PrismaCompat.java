package xyz.jpenilla.jmplib;

import lombok.NonNull;
import us.eunoians.prisma.ColorProvider;

public class PrismaCompat {
    public String translate(@NonNull String message) {
        return ColorProvider.translatePrismaToHex(message);
    }
}
