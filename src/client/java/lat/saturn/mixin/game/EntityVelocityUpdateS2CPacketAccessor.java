package lat.saturn.mixin.game;

import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityVelocityUpdateS2CPacket.class)
public interface EntityVelocityUpdateS2CPacketAccessor {
    @Accessor("velocityX") void setVelocityX(int x);
    @Accessor("velocityY") void setVelocityY(int y);
    @Accessor("velocityZ") void setVelocityZ(int z);
}