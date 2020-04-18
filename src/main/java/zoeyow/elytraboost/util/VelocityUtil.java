package zoeyow.elytraboost.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class VelocityUtil {
    //this method is used to 'pull' player's velocity to a certain state
    //the state is a certain velocity forward when player presses forward key
    //the state is still when player presses backward key
    //the acceleration / deceleration is performed in a exponential way with different factors
    //sprinting will adjust the target velocity forward when pressing forward key
    public static Vec3d calculateVelocityToAdd(EntityPlayer player, float moveForward, float velocityCap,
                                               float accelerationProportion, float decelerationProportion,
                                               float sprintingFactor) {
        if (player.isSprinting()) {
            velocityCap *= MathHelper.clamp(sprintingFactor, 0.0f, 10.0f);
        }
        Vec3d velocity = new Vec3d(player.motionX, player.motionY, player.motionZ);
        Vec3d velocityParallel = player.getLookVec().scale(velocity.dotProduct(player.getLookVec()));
        Vec3d velocityToAdd = new Vec3d(0, 0, 0);
        if (moveForward > 0 && velocity.dotProduct(player.getLookVec()) < velocityCap) {
            velocityToAdd = player.getLookVec().scale(velocityCap).subtract(velocityParallel);
        }
        if (moveForward < 0 && velocity.dotProduct(player.getLookVec()) > 0) {
            velocityToAdd = velocityParallel.scale(-1);
        }
        if (moveForward > 0) {
            velocityToAdd = velocityToAdd.scale(accelerationProportion);
        } else if (moveForward < 0) {
            velocityToAdd = velocityToAdd.scale(decelerationProportion);
        }
        return velocityToAdd;
    }
}
