package zoeyow.elytraboost.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

//this message is used to send players' attempt to toggle flight state to server, i.e. start / quit elytra flight
public class ToggleElytraMessage implements IMessage {
    private boolean toggleElytra;

    public ToggleElytraMessage() {
    }

    public ToggleElytraMessage(boolean toggleElytraIn) {
        this.toggleElytra = toggleElytraIn;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        toggleElytra = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(toggleElytra);
    }

    public static class ToggleElytraMessageHandler implements IMessageHandler<ToggleElytraMessage, IMessage> {

        @Override
        public IMessage onMessage(ToggleElytraMessage message, MessageContext ctx) {
            EntityPlayerMP serverPlayer = ctx.getServerHandler().player;

            if (serverPlayer.isElytraFlying() && !message.toggleElytra) {
                serverPlayer.getServerWorld().addScheduledTask(serverPlayer::clearElytraFlying);
            } else if (!serverPlayer.isElytraFlying() && message.toggleElytra) {
                serverPlayer.getServerWorld().addScheduledTask(serverPlayer::setElytraFlying);
            }
            //serverPlayer.sendMessage(new TextComponentString("toggle elytra received"));
            return null;
        }
    }
}
