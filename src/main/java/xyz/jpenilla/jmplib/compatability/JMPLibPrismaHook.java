package xyz.jpenilla.jmplib.compatability;

import org.checkerframework.checker.nullness.qual.NonNull;
import us.eunoians.prisma.ColorProvider;
import us.eunoians.prisma.PrismaColor;

import java.util.concurrent.ThreadLocalRandom;

public class JMPLibPrismaHook {
    public String translate(@NonNull String message) {
        return ColorProvider.translatePrismaToHex(message);
    }

    public PrismaColor randomEnumColor() {
        return PrismaColor.values()[ThreadLocalRandom.current().nextInt(0, PrismaColor.values().length)];
    }

    public String randomEnumColorHex() {
        return randomEnumColor().getHex();
    }
}
