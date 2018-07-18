package zoeyow.elytraboost.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zoeyow.elytraboost.Config;


public class VelocityAddMessage implements IMessage {
    private float clientVelocityMultiplier;

    public VelocityAddMessage() {
    }

    public VelocityAddMessage(float clientVelocityMultiplier) {
        this.clientVelocityMultiplier = clientVelocityMultiplier;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        clientVelocityMultiplier = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(clientVelocityMultiplier);
    }

    public static class VelocityAddMessageHandler implements IMessageHandler<VelocityAddMessage, IMessage> {

        @Override
        public IMessage onMessage(VelocityAddMessage message, MessageContext ctx) {
            EntityPlayerMP serverPlayer = ctx.getServerHandler().player;


            if (serverPlayer.isElytraFlying() && !Config.serverOverride) {
                double clientVelocityMultiplierValue = message.clientVelocityMultiplier;

                double xValue = serverPlayer.getLookVec().x * MathHelper.clamp(clientVelocityMultiplierValue, 0.1, 10);
                double yValue = serverPlayer.getLookVec().y * MathHelper.clamp(clientVelocityMultiplierValue, 0.1, 10);
                double zValue = serverPlayer.getLookVec().z * MathHelper.clamp(clientVelocityMultiplierValue, 0.1, 10);

                serverPlayer.getServerWorld().addScheduledTask(() -> {

                    serverPlayer.addVelocity(xValue, yValue, zValue);
                    if (serverPlayer instanceof EntityPlayerMP) {
                        EntityPlayerMP playerMP = (EntityPlayerMP) serverPlayer;
                        playerMP.connection.sendPacket(new SPacketEntityVelocity(playerMP));
                    }

                });
            } else if (serverPlayer.isElytraFlying() && Config.serverOverride) {
                double xValue = serverPlayer.getLookVec().x * Config.velocityToAdd;
                double yValue = serverPlayer.getLookVec().y * Config.velocityToAdd;
                double zValue = serverPlayer.getLookVec().z * Config.velocityToAdd;


                serverPlayer.getServerWorld().addScheduledTask(() -> {


                    serverPlayer.addVelocity(xValue, yValue, zValue);
                    if (serverPlayer instanceof EntityPlayerMP) {
                        EntityPlayerMP playerMP = (EntityPlayerMP) serverPlayer;
                        playerMP.connection.sendPacket(new SPacketEntityVelocity(playerMP));
                    }

                });
            } else if (!serverPlayer.isElytraFlying()) {
                serverPlayer.sendMessage(new TextComponentString(TextFormatting.RED + serverPlayer.getName() + TextFormatting.RESET + " " + I18n.format("=The servereventhandler doesn't think you're using an elytra... so no boost for you")));
            }
            return null;
        }
    }

}
