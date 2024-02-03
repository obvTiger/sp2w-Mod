package net.shieldbreak.stopp2w;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StopP2W implements ClientModInitializer {
    private static Logger logger = LogManager.getLogger("Server Blacklist");
    private static String blacklist;

    public static Boolean enabled;


    public enum State {
        ENABLED,
        UNTESTED,
        DISABLED
    }

    public static State state ;
    public static Logger logger() {
        return logger;
    }
    public static String blacklist() {
        return blacklist;
    }
    @Override
    public void onInitializeClient() {
        state = State.ENABLED;
    }
}