package lat.saturn.mixin.input;

import lat.saturn.SaturnClient;
import lat.saturn.api.event.input.EventKey;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {

    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        EventKey event = EventKey.get(key, scancode, action, modifiers);
        SaturnClient.EVENT_BUS.post(event);

        if(event.shouldCancel())
            ci.cancel();
    }
}
