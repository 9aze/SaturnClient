package lat.saturn.mixin.game;

import lat.saturn.SaturnClient;
import lat.saturn.api.event.world.EventTick;
import lat.saturn.api.util.render.FrameUtils;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Unique private long lastTime;
    @Unique private boolean firstFrame;

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(CallbackInfo info) {
        long time = System.currentTimeMillis();

        if (firstFrame) {
            lastTime = time;
            firstFrame = false;
        }

        FrameUtils.frameTime = (time - lastTime) / 1000.0;
        lastTime = time;
    }

    @Inject(method="tick", at = @At("HEAD"))
    private void onTickStart(CallbackInfo ci) {
        SaturnClient.EVENT_BUS.post(new EventTick(EventTick.Phase.START));
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void onTickEnd(CallbackInfo ci) {
        SaturnClient.EVENT_BUS.post(new EventTick(EventTick.Phase.END));
    }
}