package zoeyow.elytraboost.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zoeyow.elytraboost.Config;
import zoeyow.elytraboost.util.VelocityUtil;

/**
 * with the config syncing, motion can be totally handle on client side, so this message is no more needed
*/
//this message is used for players to send their attempt to accelerate or decelerate to server when ignore server is false
//still, passing packets every tick to keep player motion in sync feels laggy in game.
public class ExtraMotionMessage implements IMessage {
    private float moveForward;
    private float clientVelocityCap;
    private float clientAccelerationProportion;
    private float clientDecelerationProportion;
    private float clientSprintingFactor;

    public ExtraMotionMessage() {
    }

    public ExtraMotionMessage(float moveForwardIn, float clientVelocityCapIn,
                              float clientAccelerationProportionIn, float clientDecelerationProportionIn,
                              float clientSprintingFactorIn) {
        this.moveForward = moveForwardIn;
        this.clientVelocityCap = clientVelocityCapIn;
        this.clientAccelerationProportion = clientAccelerationProportionIn;
        this.clientDecelerationProportion = clientDecelerationProportionIn;
        this.clientSprintingFactor = clientSprintingFactorIn;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        moveForward = buf.readInt();
        clientVelocityCap = buf.readFloat();
        clientAccelerationProportion = buf.readFloat();
        clientDecelerationProportion = buf.readFloat();
        clientSprintingFactor = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(moveForward);
        buf.writeFloat(clientVelocityCap);
        buf.writeFloat(clientAccelerationProportion);
        buf.writeFloat(clientDecelerationProportion);
        buf.writeFloat(clientSprintingFactor);
    }

    public static class ExtraMotionMessageHandler implements IMessageHandler<ExtraMotionMessage, IMessage> {

        @Override
        public IMessage onMessage(ExtraMotionMessage message, MessageContext ctx) {
            EntityPlayerMP serverPlayer = ctx.getServerHandler().player;

            if (serverPlayer.isElytraFlying() && !Config.serverOverride) {
                Vec3d velocityToAdd = VelocityUtil.calculateVelocityToAdd(
                        serverPlayer, message.moveForward, message.clientVelocityCap,
                        message.clientAccelerationProportion, message.clientDecelerationProportion,
                        message.clientSprintingFactor);

                serverPlayer.getServerWorld().addScheduledTask(() -> {

                    serverPlayer.addVelocity(velocityToAdd.x, velocityToAdd.y, velocityToAdd.z);
                    if (serverPlayer instanceof EntityPlayerMP) {
                        EntityPlayerMP playerMP = (EntityPlayerMP) serverPlayer;
                        playerMP.connection.sendPacket(new SPacketEntityVelocity(playerMP));
                    }

                });
            } else if (serverPlayer.isElytraFlying() && Config.serverOverride) {
                Vec3d velocityToAdd = VelocityUtil.calculateVelocityToAdd(
                        serverPlayer, message.moveForward, Config.velocityCap,
                        Config.accelerationProportion, Config.decelerationProportion, Config.sprintingFactor);

                serverPlayer.getServerWorld().addScheduledTask(() -> {

                    serverPlayer.addVelocity(velocityToAdd.x, velocityToAdd.y, velocityToAdd.z);
                    if (serverPlayer instanceof EntityPlayerMP) {
                        EntityPlayerMP playerMP = (EntityPlayerMP) serverPlayer;
                        playerMP.connection.sendPacket(new SPacketEntityVelocity(playerMP));
                    }

                });
            } else if (!serverPlayer.isElytraFlying()) {
                //do not send message
                //serverPlayer.sendMessage(new TextComponentString(TextFormatting.RED + serverPlayer.getName() + TextFormatting.RESET + " " + I18n.format("=The servereventhandler doesn't think you're using an elytra... so no boost for you")));
            }
            return null;
        }
    }
}
