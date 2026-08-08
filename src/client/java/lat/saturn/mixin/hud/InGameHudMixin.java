package lat.saturn.mixin.hud;

import lat.saturn.SaturnClient;
import lat.saturn.api.event.render.EventRender2D;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        SaturnClient.EVENT_BUS.post(EventRender2D.get(context, context.getScaledWindowWidth(), context.getScaledWindowHeight(), tickCounter.getTickDelta(false)));
    }
}