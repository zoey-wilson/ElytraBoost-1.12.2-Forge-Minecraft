package zoeyow.elytraboost.eventhandlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zoeyow.elytraboost.Config;
import zoeyow.elytraboost.networking.ElytraBoostPacketHandler;
import zoeyow.elytraboost.networking.VelocityAddMessage;
import zoeyow.elytraboost.proxy.ClientProxy;


public class ClientEventHandler {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onEvent(InputEvent.KeyInputEvent event) {
        KeyBinding[] keyBindings = ClientProxy.keyBindings;
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (keyBindings[0].isPressed()) {
            if (!Config.ignoreServer) {
                if (player.isElytraFlying()) {
                    ElytraBoostPacketHandler.INSTANCE.sendToServer(new VelocityAddMessage(Config.velocityToAdd));
                } else {
                    player.sendMessage(new TextComponentTranslation("feedback.boost.fail"));
                }
            } else {
                if (player.isElytraFlying()) {
                    double xValue = player.getLookVec().x * MathHelper.clamp(Config.velocityToAdd, 0.1, 10);
                    double yValue = player.getLookVec().y * MathHelper.clamp(Config.velocityToAdd, 0.1, 10);
                    double zValue = player.getLookVec().z * MathHelper.clamp(Config.velocityToAdd, 0.1, 10);
                    player.addVelocity(xValue, yValue, zValue);
                } else {
                    player.sendMessage(new TextComponentTranslation("feedback.boost.fail"));
                }

            }
        }
    }
}
