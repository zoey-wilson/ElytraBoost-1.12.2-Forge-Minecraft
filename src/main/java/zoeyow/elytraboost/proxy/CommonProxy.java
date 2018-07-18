package zoeyow.elytraboost.proxy;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import zoeyow.elytraboost.Config;
import zoeyow.elytraboost.ModInfo;
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
    }

    public void init(FMLInitializationEvent e) {
    }

    public void postInit(FMLPostInitializationEvent e) {
        if (config.hasChanged()) {
            config.save();
        }
        ElytraBoostPacketHandler.INSTANCE.registerMessage(VelocityAddMessage.VelocityAddMessageHandler.class, VelocityAddMessage.class, 0, Side.SERVER);
    }
}