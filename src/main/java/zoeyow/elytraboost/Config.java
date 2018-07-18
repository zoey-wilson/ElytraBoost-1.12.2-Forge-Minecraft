package zoeyow.elytraboost;

import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import zoeyow.elytraboost.proxy.CommonProxy;

public class Config {

    private static final String CATEGORY_GENERAL = "general";
    private static final String CATEGORY_DIMENSIONS = "dimensions";

    public static float velocityToAdd = 0.5f;
    public static boolean serverOverride = false;
    public static boolean ignoreServer = false;

    // Call this from CommonProxy.preInit(). It will create our config if it doesn't
    // exist yet and read the values if it does exist.
    public static void readConfig() {
        Configuration cfg = CommonProxy.config;
        try {
            cfg.load();
            initGeneralConfig(cfg);
        } catch (Exception e1) {
            ElytraBoost.logger.log(Level.ERROR, "Problem loading config file!", e1);
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
    }

    private static void initGeneralConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
        velocityToAdd = cfg.getFloat("velocitymultiplier",CATEGORY_GENERAL, 0.5f, 0.1f, 10.0f,"The amount of velocity added by the keybind while gliding.");
        serverOverride = cfg.getBoolean("serverOverride",CATEGORY_GENERAL, false, "Whether the servereventhandler should override client config (serverside only)");
        ignoreServer = cfg.getBoolean("ignoreServer",CATEGORY_GENERAL, false, "Try working without contacting the server?");
}
}
