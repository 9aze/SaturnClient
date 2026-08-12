package lat.saturn.mixin.game;

import io.netty.channel.ChannelHandlerContext;
import lat.saturn.SaturnClient;
import lat.saturn.api.event.net.EventPacket;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin {
    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", at = @At(value = "HEAD"), cancellable = true)
    public void onReceive(ChannelHandlerContext ctx, Packet<?> packet, CallbackInfo ci) {
        EventPacket.Receive event = new EventPacket.Receive(packet, (ClientConnection) (Object) this);
        SaturnClient.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V", at = @At("HEAD"), cancellable = true)
    public void onSendHead(Packet<?> packet, PacketCallbacks callbacks, CallbackInfo ci) {
        EventPacket.Send event = new EventPacket.Send(packet, (ClientConnection) (Object) this);
        SaturnClient.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V", at = @At("TAIL"))
    public void onSendTail(Packet<?> packet, PacketCallbacks callbacks, CallbackInfo ci) {
        EventPacket.Sent event = new EventPacket.Sent(packet, (ClientConnection) (Object) this);
        SaturnClient.EVENT_BUS.post(event);
    }
}
