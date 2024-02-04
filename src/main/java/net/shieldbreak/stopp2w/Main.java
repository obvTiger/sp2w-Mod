package net.shieldbreak.stopp2w;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("SP2W");
    public enum State {
        ENABLED,
        UNTESTED,
        DISABLED
    }

    public static State state;
    @Override
    public void onInitializeClient() {
        state = State.ENABLED;
    }

}
