package zoeyow.elytraboost.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import zoeyow.elytraboost.eventhandlers.ServerEventHandler;

public class ServerProxy extends CommonProxy {
    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
    }
}