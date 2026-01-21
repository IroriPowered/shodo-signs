package cc.irori.shodo.sign.util;

import com.hypixel.hytale.logger.HytaleLogger;

public class Logs {

    private static final String LOGGER_NAME = "ShodoSigns";

    public static HytaleLogger logger() {
        return HytaleLogger.get(LOGGER_NAME);
    }
}
