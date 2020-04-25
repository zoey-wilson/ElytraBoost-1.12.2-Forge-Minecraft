package zoeyow.elytraboost.eventhandlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zoeyow.elytraboost.Config;
import zoeyow.elytraboost.networking.*;
import zoeyow.elytraboost.proxy.ClientProxy;
import zoeyow.elytraboost.util.VelocityUtil;


public class ClientEventHandler {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onEvent(InputEvent.KeyInputEvent event) {
        KeyBinding[] keyBindings = ClientProxy.keyBindings;
        EntityPlayer player = Minecraft.getMinecraft().player;
        //give player an impulse when pressing a key during elytra flight
        if (keyBindings[0].isPressed()) {
            //with the config syncing system, motion can be handled client side
            /*
            if (!Config.ignoreServer) {
                if (player.isElytraFlying()) {
                    ElytraBoostPacketHandler.INSTANCE.sendToServer(new VelocityAddMessage(Config.velocityToAdd));
                } else {
                    //do not send message
                    //player.sendMessage(new TextComponentTranslation("feedback.boost.fail"));
                }
            } else {
                if (player.isElytraFlying()) {
                    double xValue = player.getLookVec().x * MathHelper.clamp(Config.velocityToAdd, 0.1, 10);
                    double yValue = player.getLookVec().y * MathHelper.clamp(Config.velocityToAdd, 0.1, 10);
                    double zValue = player.getLookVec().z * MathHelper.clamp(Config.velocityToAdd, 0.1, 10);
                    player.addVelocity(xValue, yValue, zValue);
                } else {
                    //do not send message
                    //player.sendMessage(new TextComponentTranslation("feedback.boost.fail"));
                }
            }
            */
            //now the calculation uses the client set of variables
            if (player.isElytraFlying()) {
                double xValue = player.getLookVec().x * MathHelper.clamp(Config.velocityToAddClient, 0.1, 10);
                double yValue = player.getLookVec().y * MathHelper.clamp(Config.velocityToAddClient, 0.1, 10);
                double zValue = player.getLookVec().z * MathHelper.clamp(Config.velocityToAddClient, 0.1, 10);
                player.addVelocity(xValue, yValue, zValue);
            }
        }
        //a key to switch flight state, enabling player to use elytra whenever he want and quit elytra flight mid-air
        if (keyBindings[1].isPressed()) {
            ElytraBoostPacketHandler.INSTANCE.sendToServer(new ToggleElytraMessage(!player.isElytraFlying()));
            //player.sendMessage(new TextComponentString("toggle elytra pressed"));
        }
    }

    //provides a smoother way to accelerate and decelerate during flight
    //sending messages every tick to keep sync seems to be laggy. SO SELF DISCIPLINE!
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onPlayerUpdate(TickEvent.PlayerTickEvent event) {
        //added this check to prevent event being triggered twice a tick
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        EntityPlayer player = event.player;
        if (!player.isElytraFlying()) {
            return;
        }
        //with the config syncing system, motion can be handled client side
        //this should be done at client side, otherwise game will become way too laggy due to frequent messages?
        /*
        if (!Config.ignoreServer) {
            ElytraBoostPacketHandler.INSTANCE.sendToServer(new ExtraMotionMessage(
                    player.moveForward, Config.velocityCap,
                    Config.accelerationProportion, Config.decelerationProportion, Config.sprintingFactor));
        } else {
            Vec3d velocityToAdd = VelocityUtil.calculateVelocityToAdd(
                    player, player.moveForward, Config.velocityCap,
                    Config.accelerationProportion, Config.decelerationProportion, Config.sprintingFactor);
            player.addVelocity(velocityToAdd.x, velocityToAdd.y, velocityToAdd.z);
        }
        */
        //now the calculation uses the client set of variables
        Vec3d velocityToAdd = VelocityUtil.calculateVelocityToAdd(
                player, player.moveForward, Config.velocityCapClient,
                Config.accelerationProportionClient, Config.decelerationProportionClient, Config.sprintingFactorClient);
        player.addVelocity(velocityToAdd.x, velocityToAdd.y, velocityToAdd.z);
        //tell the server to apply exhaustion to player
        /**
         * remember to use CLIENT set of variables! (a note to my self) !!!!!!
         */
        if (Config.applyExhaustion && player.moveForward > 0) {
            ElytraBoostPacketHandler.INSTANCE.sendToServer(new
                    ApplyExhaustionMessage(Config.exhaustionFactorClient * (float) velocityToAdd.lengthVector()));
        }
    }
}
