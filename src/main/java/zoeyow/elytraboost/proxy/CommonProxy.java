package zoeyow.elytraboost.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import zoeyow.elytraboost.Config;
import zoeyow.elytraboost.ModInfo;
import zoeyow.elytraboost.eventhandlers.CommonEventHandler;
import zoeyow.elytraboost.eventhandlers.ServerEventHandler;
import zoeyow.elytraboost.networking.ElytraBoostPacketHandler;
import zoeyow.elytraboost.networking.VelocityAddMessage;

import java.io.File;

@Mod.EventBusSubscriber
public class CommonProxy {

    public static Configuration config;

    public void preInit(FMLPreInitializationEvent e) {
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), ModInfo.MODID + ".cfg"));
        Config.readConfig();
        //sync the "physical client / server"'s two set of variables
        Config.syncClientConfigVariables(Config.velocityToAdd, Config.serverOverride, Config.ignoreServer,
                Config.velocityCap, Config.accelerationProportion, Config.decelerationProportion, Config.sprintingFactor);
    }

    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
    }

    public void postInit(FMLPostInitializationEvent e) {
        if (config.hasChanged()) {
            config.save();
        }
        ElytraBoostPacketHandler.register();
    }
}