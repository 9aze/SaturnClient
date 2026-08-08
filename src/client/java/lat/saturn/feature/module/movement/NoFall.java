package lat.saturn.feature.module.movement;

import lat.saturn.api.manager.module.Category;
import lat.saturn.api.manager.module.Module;
import lat.saturn.api.manager.module.RegisterModule;
import lat.saturn.api.setting.settings.EnumSetting;
import org.lwjgl.glfw.GLFW;

@RegisterModule(name="NoFall", description = "Cancels fall damage.", category = Category.MOVEMENT, toggled = false)
public class NoFall extends Module {
    public enum Mode {
        GroundSpoof,
    }

    private static NoFall INSTANCE;

    public NoFall() {
        INSTANCE = this;
    }

    EnumSetting<Mode> mode = new EnumSetting<>("Mode", "Mode of nofall", Mode.GroundSpoof);

    @Override
    public void onEnable() {
        INSTANCE = this;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }


    public static boolean groundSpoof() {
        return INSTANCE != null
                && INSTANCE.isToggled()
                && INSTANCE.mode.getValue() == Mode.GroundSpoof
                && INSTANCE.mc.player != null
                && !INSTANCE.mc.player.getAbilities().creativeMode
                && INSTANCE.mc.player.getVelocity().y < -0.5
                && !INSTANCE.mc.player.isGliding();
    }
}
