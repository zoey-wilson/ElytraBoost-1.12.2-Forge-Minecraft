package zoeyow.elytraboost.eventhandlers;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zoeyow.elytraboost.Config;
import zoeyow.elytraboost.networking.ConfigSyncMessage;
import zoeyow.elytraboost.networking.ElytraBoostPacketHandler;

public class ServerEventHandler {

    //this event's function is to send a packet of server config to client and sync config
    @SideOnly(Side.SERVER)
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        ElytraBoostPacketHandler.INSTANCE.sendTo(
                new ConfigSyncMessage(Config.velocityToAdd, Config.serverOverride, Config.ignoreServer,
                        Config.velocityCap, Config.accelerationProportion,
                        Config.decelerationProportion, Config.sprintingFactor),
                (EntityPlayerMP) event.player);
    }
}
