package zoeyow.elytraboost.eventhandlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import zoeyow.elytraboost.Config;
import zoeyow.elytraboost.util.VelocityUtil;

public class CommonEventHandler {
    //deprecated, still don't know what's the 'legal' way to modify player motion, so just leave it here.
    /*
    @SubscribeEvent
    public void onPlayerUpdate(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (!player.isElytraFlying()) {
            return;
        }
        float velocityCapClamped = MathHelper.clamp(Config.velocityCap, 0.0f, 100.0f);
        float accelerationProportionClamped = MathHelper.clamp(Config.accelerationProportion, 0.0f, 1.0f);
        Vec3d velocityToAdd = VelocityUtil.calculateVelocityToAdd(
                player, player.moveForward, velocityCapClamped, accelerationProportionClamped);
        player.addVelocity(velocityToAdd.x, velocityToAdd.y, velocityToAdd.z);
    }
    */
}
