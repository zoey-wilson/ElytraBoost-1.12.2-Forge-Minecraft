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
    //more configs, see below for their functions, or see util/VelocityUtil
    public static float velocityCap = 1.0f;
    public static float accelerationProportion = 0.02f;
    public static float decelerationProportion = 0.03f;
    public static float sprintingFactor = 1.33f;

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
        velocityToAdd = cfg.getFloat(
                "velocitymultiplier", CATEGORY_GENERAL, 0.5f, 0.1f, 10.0f,
                "The amount of velocity added by the keybind while gliding.");
        serverOverride = cfg.getBoolean(
                "serverOverride", CATEGORY_GENERAL, false,
                "Whether the servereventhandler should override client config (serverside only)");
        ignoreServer = cfg.getBoolean(
                "ignoreServer", CATEGORY_GENERAL, false,
                "Try working without contacting the server? If contact, may feel laggy (player motion sync). " +
                        "This is more like a self-disciplinary config. Yes, you can make your elytra super powerful.");
        //more configs
        velocityCap = cfg.getFloat(
                "velocityCap", CATEGORY_GENERAL, 1.0f, 0.0f, 100.0f,
                "Determines the maximum forward speed obtainable via holding forward key, blocks / tick");
        accelerationProportion = cfg.getFloat(
                "accelerationProportion", CATEGORY_GENERAL, 0.02f, 0.0f, 1.0f,
                "Determines the acceleration strength, the proportion of speed gap covered / tick");
        decelerationProportion = cfg.getFloat(
                "decelerationProportion", CATEGORY_GENERAL, 0.03f, 0.0f, 1.0f,
                "Determines the deceleration strength, the proportion of speed gap covered / tick");
        sprintingFactor = cfg.getFloat(
                "sprintingFactor", CATEGORY_GENERAL, 1.33f, 0.0f, 10.0f,
                "The factor multiplied to the velocity cap when sprinting");
    }
}
