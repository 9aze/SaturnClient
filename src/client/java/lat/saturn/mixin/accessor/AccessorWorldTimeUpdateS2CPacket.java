package lat.saturn.mixin.accessor;

import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldTimeUpdateS2CPacket.class)
public interface AccessorWorldTimeUpdateS2CPacket {
    @Mutable
    @Accessor("timeOfDay")
    void setTimeOfDay(long timeOfDay);
}