package lat.saturn.feature.module.render;

import lat.saturn.api.event.net.EventPacket;
import lat.saturn.api.event.world.EventTick;
import lat.saturn.api.manager.module.Category;
import lat.saturn.api.manager.module.Module;
import lat.saturn.api.manager.module.RegisterModule;
import lat.saturn.api.setting.settings.DoubleSetting;
import lat.saturn.api.setting.settings.EnumSetting;
import lat.saturn.mixin.accessor.AccessorClientWorld;
import lat.saturn.mixin.accessor.AccessorWorldTimeUpdateS2CPacket;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

@RegisterModule(name = "Fullbright", description = "Makes you see in the dark", category = Category.RENDER)
public class Fullbright extends Module {
    public EnumSetting<Mode> mode = new EnumSetting<>("Mode", "Fullbright brightness mode", Mode.GAMMA);
    DoubleSetting gamma = new DoubleSetting("Gamma", "Gamma value to use", 8.0, 0.0, 10.0).visible(() -> mode.getValue() == Mode.GAMMA);
    DoubleSetting time = new DoubleSetting("Time", "Time of day", 0.0, -20.0, 20.0).visible(() -> mode.getValue() == Mode.Time);

    private Mode lastMode = null;
    private double lastGamma = Double.NaN;
    private double lastTime = Double.NaN;

    @Override
    public void onEnable() {
        super.onEnable();
        lastMode = null;
        apply();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (mc.player != null) {
            mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
        }
    }

    @EventHandler
    public void onTick(EventTick event) {
        if (mode.getValue() == Mode.Time) {
            apply();
            return;
        }

        if (mode.getValue() != lastMode || gamma.getValue() != lastGamma) {
            apply();
        }
    }

    @EventHandler
    public void onPacketRecieve(EventPacket.Receive event) {
        if (mode.getValue() == Mode.Time && event.packet instanceof WorldTimeUpdateS2CPacket) {
            AccessorWorldTimeUpdateS2CPacket packet = (AccessorWorldTimeUpdateS2CPacket) event.packet;
            packet.setTimeOfDay((long) (time.getValue() * 1000.0));
        }
    }

    private void apply() {
        if (mc.player == null || mc.world == null) {
            return;
        }

        switch (mode.getValue()) {
            case Mode.GAMMA -> mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
            case Mode.STATUS_EFFECT -> mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, -1, 1, true, false, false));
            case Mode.Time -> ((AccessorClientWorld) mc.world).getClientWorldProperties().setTimeOfDay((long) (time.getValue() * 1000.0));
            default -> throw new IllegalStateException("unexpected value (how the fuck)");
        }

        lastMode = (Mode) mode.getValue();
        lastGamma = gamma.getValue();
        lastTime = time.getValue();
    }

    public float getGamma(float original) {
        if (mode.getValue() == Mode.GAMMA && this.isToggled()) {
            double g = gamma.getValue();
            return (float) g;
        }
        return original;
    }

    public enum Mode {
        GAMMA("Gamma"), STATUS_EFFECT("Status effect"), Time("Time Change");
        private final String displayName;
        Mode(String displayName) {this.displayName = displayName;}

        @Override
        public String toString() { return displayName; }
    }
}