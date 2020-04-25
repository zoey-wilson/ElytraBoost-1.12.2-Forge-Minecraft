package zoeyow.elytraboost.networking;

import c4.colytra.common.capabilities.CapabilityColytraFlying;
import c4.colytra.util.ColytraUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zoeyow.elytraboost.proxy.CommonProxy;

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
                //add support for Colytra
                if (CommonProxy.colytraLoaded) {
                    CapabilityColytraFlying.IColytraFlying flying = CapabilityColytraFlying.getColytraCap(serverPlayer);
                    if (flying != null) {
                        flying.clearColytraFlying();
                    }
                }
            } else if (!serverPlayer.isElytraFlying() && message.toggleElytra
                    && !serverPlayer.onGround && !serverPlayer.isRiding()) {
                boolean elytraAvailable = false;
                boolean colytraAvailable = false;
                //add support for Colytra
                if (CommonProxy.colytraLoaded) {
                    ItemStack colytra = ColytraUtil.wornElytra(serverPlayer);
                    if (colytra != ItemStack.EMPTY && colytra.getItem() != Items.ELYTRA && ColytraUtil.isUsable(colytra)) {
                        colytraAvailable = true;
                    }
                }
                ItemStack elytra = serverPlayer.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                if (elytra.getItem() == Items.ELYTRA && ItemElytra.isUsable(elytra)) {
                    elytraAvailable = true;
                }
                if (elytraAvailable || colytraAvailable) {
                    serverPlayer.getServerWorld().addScheduledTask(serverPlayer::setElytraFlying);
                }
                if (colytraAvailable) {
                    CapabilityColytraFlying.IColytraFlying flying = CapabilityColytraFlying.getColytraCap(serverPlayer);
                    if (flying != null) {
                        flying.setColytraFlying();
                    }
                }
            }
            //serverPlayer.sendMessage(new TextComponentString("toggle elytra received"));
            return null;
        }
    }
}
