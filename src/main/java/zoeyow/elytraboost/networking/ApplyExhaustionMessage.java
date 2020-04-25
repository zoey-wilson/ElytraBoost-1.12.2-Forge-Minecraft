package zoeyow.elytraboost.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ApplyExhaustionMessage implements IMessage {
    private float exhaustion;

    public ApplyExhaustionMessage() {
    }

    public ApplyExhaustionMessage(float exhaustion) {
        this.exhaustion = exhaustion;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        exhaustion = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(exhaustion);
    }

    public static class ApplyExhaustionMessageHandler implements IMessageHandler<ApplyExhaustionMessage, IMessage> {

        @Override
        public IMessage onMessage(ApplyExhaustionMessage message, MessageContext ctx) {
            EntityPlayerMP serverPlayer = ctx.getServerHandler().player;

            if (!serverPlayer.isCreative()) {
                serverPlayer.getServerWorld().addScheduledTask(() ->  {
                    serverPlayer.addExhaustion(message.exhaustion);
                });
                //test
                //serverPlayer.sendMessage(new TextComponentString("add exhaustion" + message.exhaustion));
            }
            return null;
        }
    }
}
