package lat.saturn.mixin.net;

import com.mojang.patchy.BlockedServers;
import lat.saturn.feature.module.misc.AnarchyMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockedServers.class)
public class BlockedServersMixin {
    @Inject(method = "isBlockedServerHostName", at = @At("RETURN"), cancellable = true, remap = false)
    public void isBlockedServerHostName(String server, CallbackInfoReturnable<Boolean> cir) {
        if(!AnarchyMod.INSTANCE.isToggled() || !AnarchyMod.INSTANCE.unblockServers.getValue()) return;
        cir.setReturnValue(false);
    }
}