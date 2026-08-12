package lat.saturn.mixin.accessor;

import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientWorld.class)
public interface AccessorClientWorld {
    @Accessor("clientWorldProperties")
    ClientWorld. Properties getClientWorldProperties();
}