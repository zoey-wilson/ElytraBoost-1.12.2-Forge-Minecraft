package zoeyow.elytraboost;

import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import zoeyow.elytraboost.proxy.CommonProxy;

public class Config {

    private static final String CATEGORY_GENERAL = "general";
    private static final String CATEGORY_DIMENSIONS = "dimensions";

    //this is the set of variables that are updated from config file, intended to be used server side
    public static float velocityToAdd = 0.5f;
    public static boolean serverOverride = false;
    public static boolean ignoreServer = false;
    //more configs, see below for their functions, or see util/VelocityUtil
    public static float velocityCap = 1.0f;
    public static float accelerationProportion = 0.04f;
    public static float decelerationProportion = 0.06f;
    public static float sprintingFactor = 1.33f;
    public static boolean applyExhaustion = true;
    public static float exhaustionFactor = 0.8f;

    //this is the set of variables that are synced when logging in a world, intended to be used client side
    public static float velocityToAddClient = 0.5f;
    public static boolean serverOverrideClient = false;
    public static boolean ignoreServerClient = false;
    //more configs, see below for their functions, or see util/VelocityUtil
    public static float velocityCapClient = 1.0f;
    public static float accelerationProportionClient = 0.04f;
    public static float decelerationProportionClient = 0.06f;
    public static float sprintingFactorClient = 1.33f;
    public static boolean applyExhaustionClient = true;
    public static float exhaustionFactorClient = 0.8f;

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

    //Call this when the game starts, at the phase of reading configs
    //Sync the "physical client"'s two sets of variables and "physical server"'s two sets of variables respectively.
    //And call this when player joins the world
    //The server will send a packet to the client, and the client will sync according to config settings.
    public static void syncClientConfigVariables(float velocityToAdd, boolean serverOverride, boolean ignoreServer,
                                                 float velocityCap, float accelerationProportion,
                                                 float decelerationProportion, float sprintingFactor,
                                                 boolean applyExhaustion, float exhaustionFactor) {
        velocityToAddClient = velocityToAdd;
        serverOverrideClient = serverOverride;
        ignoreServerClient = ignoreServer;
        velocityCapClient = velocityCap;
        accelerationProportionClient = accelerationProportion;
        decelerationProportionClient = decelerationProportion;
        sprintingFactorClient = sprintingFactor;
        applyExhaustionClient = applyExhaustion;
        exhaustionFactorClient = exhaustionFactor;
    }

    private static void initGeneralConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
        velocityToAdd = cfg.getFloat(
                "velocitymultiplier", CATEGORY_GENERAL, 0.5f, 0.1f, 10.0f,
                "The amount of velocity added by pressing the boost key while gliding. [Legacy feature] " +
                        "i.e. the boost given when press B (default). Unit: blocks / tick");
        serverOverride = cfg.getBoolean(
                "serverOverride", CATEGORY_GENERAL, false,
                "Whether the server should forcibly override client config. " +
                        "(serverside config, useless in single player)");
        ignoreServer = cfg.getBoolean(
                "ignoreServer", CATEGORY_GENERAL, false,
                "Try using local config instead of server config. If the server set serverOverride to true, " +
                        "the config will still be overridden by server when joining a game. (useless in single player)");
        //more configs
        velocityCap = cfg.getFloat(
                "velocityCap", CATEGORY_GENERAL, 1.0f, 0.0f, 10.0f,
                "Determines the maximum forward speed obtainable via holding forward key. Unit is: blocks / tick");
        accelerationProportion = cfg.getFloat(
                "accelerationProportion", CATEGORY_GENERAL, 0.04f, 0.0f, 1.0f,
                "Determines the acceleration strength. Unit is: the proportion of speed gap covered / tick");
        decelerationProportion = cfg.getFloat(
                "decelerationProportion", CATEGORY_GENERAL, 0.06f, 0.0f, 1.0f,
                "Determines the deceleration strength. Unit is: the proportion of speed gap covered / tick");
        sprintingFactor = cfg.getFloat(
                "sprintingFactor", CATEGORY_GENERAL, 1.33f, 0.0f, 10.0f,
                "The factor multiplied to the velocity cap when sprinting.");
        applyExhaustion = cfg.getBoolean(
                "applyExhaustion", CATEGORY_GENERAL, true,
                "Whether acceleration will add exhaustion. (lose hunger proportional to acceleration)");
        exhaustionFactor = cfg.getFloat(
                "exhaustionFactor", CATEGORY_GENERAL, 0.8f, 0.0f, 10.0f,
                "The factor used when calculating hunger loss due to acceleration." +
                        "Unit: blocks / tick (acceleration) --> hunger points (1 = half the chicken leg icon)");
    }
}
