package zoeyow.elytraboost.proxy;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;
import zoeyow.elytraboost.eventhandlers.ClientEventHandler;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    public static KeyBinding[] keyBindings;

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        keyBindings = new KeyBinding[2];

        //used to provide an impulse (boost) towards facing direction
        keyBindings[0] = new KeyBinding("binding.boost.description", Keyboard.KEY_B, "binding.boost.category");
        //used to switch on / off elytra flight
        keyBindings[1] = new KeyBinding("binding.toggleElytraFlight.description", Keyboard.KEY_R, "binding.boost.category");

        for (int i = 0; i < keyBindings.length; ++i)
        {
            ClientRegistry.registerKeyBinding(keyBindings[i]);
        }
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }
}
