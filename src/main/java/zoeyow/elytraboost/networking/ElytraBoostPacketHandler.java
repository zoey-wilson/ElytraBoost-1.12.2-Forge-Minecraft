package zoeyow.elytraboost.networking;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import zoeyow.elytraboost.ModInfo;

public class ElytraBoostPacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.MODID);

    public static void register() {
        INSTANCE.registerMessage(
                VelocityAddMessage.VelocityAddMessageHandler.class, VelocityAddMessage.class, 0, Side.SERVER);
        INSTANCE.registerMessage(
                ExtraMotionMessage.ExtraMotionMessageHandler.class, ExtraMotionMessage.class, 1, Side.SERVER);
        INSTANCE.registerMessage(
                ToggleElytraMessage.ToggleElytraMessageHandler.class, ToggleElytraMessage.class, 2, Side.SERVER);
    }
}
