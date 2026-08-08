package lat.saturn.mixin.game;

import lat.saturn.feature.module.movement.NoFall;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerMoveC2SPacket.class)
public class PlayerMoveC2SMixin {
    @Shadow @Final @Mutable private boolean onGround;

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void onConstruct(CallbackInfo ci) {
        if (NoFall.groundSpoof()) {
            this.onGround = true;
        } else { System.out.println(false); }
    }
}
