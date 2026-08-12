package lat.saturn.mixin.render;

import lat.saturn.api.manager.Managers;
import lat.saturn.feature.module.render.Fullbright;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
    @Redirect(method = "update(F)V", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 1))
    private float modifyGamma(Double original) {
        return ((Fullbright) Managers.MODULE_MANAGER.getByClass(Fullbright.class)).getGamma(original.floatValue());
    }
}